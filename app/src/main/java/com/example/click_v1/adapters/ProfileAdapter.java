package com.example.click_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.click_v1.R;
import com.example.click_v1.models.Profile;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private  final List<Profile> profiles;
    public ProfileAdapter(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @androidx.annotation.NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_profile,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ProfileViewHolder holder, int position) {
        holder.setProfile(profiles.get(position));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder  {
        private final RoundedImageView imagePoster;
        private final TextView textName, textCategory, textDate;
        private final RatingBar ratingBar;

        public ProfileViewHolder(View view) {
            super(view);
            imagePoster = view.findViewById(R.id.imagePoster);
            textName = view.findViewById(R.id.textName);
            textCategory = view.findViewById(R.id.textCategory);
            textDate = view.findViewById(R.id.textDate);
            ratingBar = view.findViewById(R.id.ratingBar);
        }

        void setProfile(Profile profile) {
            imagePoster.setImageResource(profile.imagePoster);
            textName.setText(profile.name);
            textCategory.setText(profile.category);
            textDate.setText(profile.date);
            ratingBar.setRating(profile.rating);
        }

    }
}
