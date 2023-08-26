package com.example.click_v1.activities;

import static com.example.click_v1.utilities.Common.getBitmapFromEncodedString;
import static com.example.click_v1.utilities.Common.getReadableDateTime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.click_v1.R;
import com.example.click_v1.adapters.ChatAdapter;
import com.example.click_v1.databinding.ActivityChatBinding;
import com.example.click_v1.models.ChatMessage;
import com.example.click_v1.models.User;
import com.example.click_v1.network.ApiClient;
import com.example.click_v1.network.ApiService;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private User receiverUser;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private String conversationId = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, getBitmapFromEncodedString(receiverUser.image), preferenceManager.getString(Constants.KEY_USER_ID));
        binding.chatRecyclerView.setAdapter(chatAdapter);
        binding.chatRecyclerView.setVisibility(View.VISIBLE);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        // put message into database
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if (conversationId != null) {
            updateConversation(binding.inputMessage.getText().toString());
        } else {
            HashMap<String, Object> conversation = new HashMap<>();
            conversation.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            conversation.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            conversation.put(Constants.KEY_SENDER_IAMGE, preferenceManager.getString(Constants.KEY_IMAGE));
            conversation.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversation.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
            conversation.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
            conversation.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversation.put(Constants.KEY_TIMESTAMP, new Date());
            addConversation(conversation);
        }
        // step 3 check if receiver is available before sending notification
        if (!isReceiverAvailable) {
            try {
                JSONArray tokens = new JSONArray();
                tokens.put(receiverUser.token);

                // prepare notification data, tokens as json object
                JSONObject data = new JSONObject();
                data.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                data.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA, data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                sendNotification(body.toString());
            } catch (Exception ignored) {
            }
        }
        binding.inputMessage.setText(null);
    }

    // Notification step 2
    private void sendNotification(String messageBody) {
        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getRemoteMsgHeaders(), messageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                Toast.makeText(ChatActivity.this, "error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    private void listenAvailabilityOfReceiver() {
        database.collection(Constants.KEY_COLLECTION_USERS).document(receiverUser.id).addSnapshotListener(ChatActivity.this, (value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
                    int availability = Objects.requireNonNull(value.getLong(Constants.KEY_AVAILABILITY)).intValue();
                    isReceiverAvailable = availability == 1;
                }
                receiverUser.token = value.getString(Constants.KEY_FCM_TOKEN);
                if (receiverUser.image == null) {
                    receiverUser.image = value.getString(Constants.KEY_IMAGE);
                    chatAdapter.setReceiverProfileImage(getBitmapFromEncodedString(receiverUser.image));
                    chatAdapter.notifyItemRangeInserted(0, chatMessages.size());
                }
            }
            if (isReceiverAvailable) {
                binding.textAvailability.setVisibility(View.VISIBLE);
            } else {
                binding.textAvailability.setVisibility(View.GONE);
            }
        });
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID)).whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id).addSnapshotListener(eventListener);

        database.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id).whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID)).addSnapshotListener(eventListener);
    }


    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            // value contains data of changed document
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                // Check if event type is added type
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    // get document change event created date as readable format
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    // get document change event created date object
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            // sort message by date and time
            chatMessages.sort(Comparator.comparing(obj -> obj.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                // if user has new messages, scroll to the latest message
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            // set message recycler to be visible
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        // set message recycler to be invisible
        binding.processBar.setVisibility(View.GONE);
        if (conversationId == null) {
            checkForConversation();
        }
    };

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
        binding.receiverNameText.setText(receiverUser.name);
        binding.receiverImage.setImageBitmap(getBitmapFromEncodedString(receiverUser.image));
    }

    @SuppressLint("NonConstantResourceId")
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
        binding.sendStarBtn.setOnClickListener(v -> {
            if (binding.buyStarLayout.getVisibility() == View.VISIBLE) {
                binding.buyStarLayout.setVisibility(View.GONE);
            } else {
                binding.buyStarLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.buyNowBtn.setOnClickListener(v -> {
            int selectedId = binding.radioGroup.getCheckedRadioButtonId();
            if (selectedId >= 1) {
                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                intent.putExtra(Constants.KEY_RECEIVER, receiverUser);
                int stars;
                switch (selectedId) {
                    case R.id.radioOption1:
                        stars = 50;
                        intent.putExtra(Constants.KEY_STARS, stars);
                        break;
                    case R.id.radioOption2:
                        stars = 100;
                        intent.putExtra(Constants.KEY_STARS, stars);
                        break;
                    case R.id.radioOption3:
                        stars = 200;
                        intent.putExtra(Constants.KEY_STARS, stars);
                        break;
                }
                startActivity(intent);
                binding.buyStarLayout.setVisibility(View.GONE);
            }
        });
        binding.radioOption1.setOnClickListener(this::onRadioButtonClicked);
        binding.radioOption2.setOnClickListener(this::onRadioButtonClicked);
        binding.radioOption3.setOnClickListener(this::onRadioButtonClicked);
        binding.radioOption4.setOnClickListener(this::onRadioButtonClicked);
    }

    @SuppressLint("NonConstantResourceId")
    private void onRadioButtonClicked(View view) {
        binding.radioOption1.getId();
        switch (view.getId()) {
            case R.id.radioOption1:
            case R.id.radioOption2:
            case R.id.radioOption3:
            case R.id.radioOption4:
                break;
        }
    }

    private void addConversation(HashMap<String, Object> conversation) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).add(conversation).addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String message) {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message, Constants.KEY_TIMESTAMP, new Date());
    }

    private void checkForConversation() {
        if (chatMessages.size() != 0) {
            checkForConversationRemotely(preferenceManager.getString(Constants.KEY_USER_ID), receiverUser.id);
            checkForConversationRemotely(receiverUser.id, preferenceManager.getString(Constants.KEY_USER_ID));
        }
    }

    private void checkForConversationRemotely(String senderId, String receiverId) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).whereEqualTo(Constants.KEY_SENDER_ID, senderId).whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId).get().addOnCompleteListener(conversationOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}