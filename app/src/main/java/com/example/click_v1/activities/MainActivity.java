package com.example.click_v1.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.click_v1.R;
import com.example.click_v1.adapters.ProfileAdapter;
import com.example.click_v1.adapters.RecentConversationAdapter;
import com.example.click_v1.databinding.ActivityMainBinding;
import com.example.click_v1.fragement.ChatFragment;
import com.example.click_v1.fragement.MapFragment;
import com.example.click_v1.fragement.MeFragment;
import com.example.click_v1.listeners.ConversationListener;
import com.example.click_v1.models.ChatMessage;
import com.example.click_v1.models.Profile;
import com.example.click_v1.models.User;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationsAdapter;
    private FirebaseFirestore database;

    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        replaceFragment(new ChatFragment());
        init();
        loadUserDetails();
        getToken();
        setListeners();
        showToast("asking for permistion");
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA},
                225);
//        listenConversations();
    }



    private void init() {
        conversations = new ArrayList<>();
//        conversationsAdapter = new RecentConversationAdapter(conversations, this);
//        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void setListeners() {
        binding.imageSignOut.setOnClickListener(v -> signOut());
//        binding.fabNewChat.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));

        // listener for smoothBar
        binding.smoothBottomBar.setOnItemSelectedListener(this::switchActivity);
    }

    private void switchActivity(int position) {
        switch (position) {
            case 0:
                replaceFragment(new ChatFragment());
                break;
            case 1:
                binding.textName.setText( "Find Partners");
                replaceFragment(new MapFragment());
                break;
            case 2:
                replaceFragment(new MeFragment());
                break;
            case 3:
                replaceFragment(new MeFragment());
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void loadUserDetails() {
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    private void listenConversations() {
//        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
//                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                .addSnapshotListener(eventListener);
//        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
//                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                .addSnapshotListener(eventListener);
//    }

//    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
//        if (error != null) {
//            return;
//        }
//        if (value != null) {
//            // listen to conversations record, and set chat message by conversations
//            for (DocumentChange documentChange : value.getDocumentChanges()) {
//                if (documentChange.getType() == DocumentChange.Type.ADDED) {
//                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                    ChatMessage chatMessage = new ChatMessage();
//                    chatMessage.senderId = senderId;
//                    chatMessage.receiverId = receiverId;
//                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
//                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
//                        chatMessage.conversationName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
//                        chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                    } else {
//                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IAMGE);
//                        chatMessage.conversationName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
//                        chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    }
//                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
//                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//                    conversations.add(chatMessage);
//                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
//                    for (int i = 0; i < conversations.size(); i++) {
//                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
//                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
//                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//                            break;
//                        }
//                    }
//                }
//            }
//            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
//            conversationsAdapter.notifyDataSetChanged();
//            binding.conversationsRecyclerView.smoothScrollToPosition(0);
//            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
//            binding.progressBar.setVisibility(View.GONE);
//        }
//    };

    private void getToken() {
        // get token from firebase messaging and pass token to updateToken
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // get user data from database with collection name and user ID
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        // save FCM token to database
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // get user data from database with collection name and user ID
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        // create a map with key fcm token and empty value
        HashMap<String, Object> updates = new HashMap<>();
        // empty key fcm token in database
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            preferenceManager.clear();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }).addOnFailureListener(e -> showToast("Unable to sign out"));
    }

//    @Override
//    public void onConversationClicked(User user) {
//        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
//        intent.putExtra(Constants.KEY_USER, user);
//        startActivity(intent);
//    }

}
