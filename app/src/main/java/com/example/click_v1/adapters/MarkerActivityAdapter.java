package com.example.click_v1.adapters;

import static com.example.click_v1.utilities.Common.getBitmapFromEncodedString;
import static com.example.click_v1.utilities.Common.getDateDiff;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click_v1.R;
import com.example.click_v1.listeners.UserListener;
import com.example.click_v1.models.MarkerActivity;
import com.example.click_v1.models.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MarkerActivityAdapter extends RecyclerView.Adapter<MarkerActivityAdapter.MarkerActivityViewHolder> {

    private final List<MarkerActivity> markerActivities;

    private final UserListener userListener;

    private final String senderId;

    public MarkerActivityAdapter(List<MarkerActivity> markerActivities, UserListener userListener, String senderId) {
        this.markerActivities = markerActivities;
        this.userListener = userListener;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public MarkerActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MarkerActivityAdapter.MarkerActivityViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_selected_activity,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MarkerActivityViewHolder holder, int position) {
        holder.setData(markerActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return markerActivities.size();
    }


    class MarkerActivityViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView activityCardRecyclerView;
        private final LinearLayout sendBtn;

        private final TextView topicText, titleText, descriptionText, hoursText, minutesText, secondsText, creatorNameText;

        private AppCompatImageView sendStarBtn;

        private final RoundedImageView creatorImage;

        private CountDownTimer localTimer;

        private Long countDownLeftTime;

        public MarkerActivityViewHolder(View view) {
            super(view);
            activityCardRecyclerView = view.findViewById(R.id.activityCardRecyclerView);
            sendBtn = view.findViewById(R.id.sendBtn);
            topicText = view.findViewById(R.id.topicText);
            titleText = view.findViewById(R.id.titleText);
            descriptionText = view.findViewById(R.id.descriptionText);
            hoursText = view.findViewById(R.id.hoursText);
            minutesText = view.findViewById(R.id.minutesText);
            secondsText = view.findViewById(R.id.secondsText);
            creatorNameText = view.findViewById(R.id.creatorNameText);
            creatorImage = view.findViewById(R.id.creatorImage);
            sendStarBtn = view.findViewById(R.id.sendStarBtn);
        }

        void setData(MarkerActivity markerActivity) {
            if (localTimer != null) {
                localTimer.cancel();
            }
            Date now = new Date();
            countDownLeftTime = getDateDiff(markerActivity.dateObject, now, TimeUnit.MILLISECONDS);
            localTimer = getDownTimer(countDownLeftTime);
            localTimer.start();

            topicText.setText(markerActivity.topic);
            titleText.setText(markerActivity.title);
            descriptionText.setText(markerActivity.description);
            creatorImage.setImageBitmap(getBitmapFromEncodedString(markerActivity.creatorImage));
            creatorNameText.setText(markerActivity.creatorName);
            if (!Objects.equals(senderId, markerActivity.creatorId)) {
                User user = new User();
                user.name = markerActivity.creatorName;
                user.image = markerActivity.creatorImage;
                user.id = markerActivity.creatorId;
                user.email = markerActivity.email;
                user.location = markerActivity.location;
                user.country = markerActivity.country;
                user.gender = markerActivity.gender;
                user.token = markerActivity.token;
                user.selfIntroduction = markerActivity.selfIntroduction;
                sendBtn.setVisibility(View.VISIBLE);
                sendBtn.setOnClickListener(v -> userListener.onUserClick(user));
                sendBtn.setOnClickListener(v -> userListener.onUserClick(user));
            }
        }

        private CountDownTimer getDownTimer(Long millionSecond) {
            return new CountDownTimer(millionSecond, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long second = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                    long hours = (millisUntilFinished / (1000 * 60 * 60)) % 60;
                    secondsText.setText(String.valueOf(second));
                    minutesText.setText(String.valueOf(minutes));
                    hoursText.setText(String.valueOf(hours));
                }

                @Override
                public void onFinish() {
                    activityCardRecyclerView.setVisibility(View.GONE);
                }
            };
        }
    }
}
