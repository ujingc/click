package com.example.click_v1.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click_v1.databinding.ItemContainerUserBinding;
import com.example.click_v1.models.User;

import java.util.List;


// Recycle view class extends data model
// UserViewHolder, super itemView receive from binding or Inflate item layout, bind view data to this class
// define onCreateViewHolder, pass Inflate item layout or binding to UserViewHolder
// onBindViewHolder, receive ViewHolder and set data to holder by its position
// getItemCount return data size
// needs a ViewHolder that binds fields to view content

public class  UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    private final List<User> users;
    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get item binding directly from layout and its parent layout inflater.
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext())
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUserBinding binding;
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }
        // set user data method for onBindViewHolder
        void setUserData(User user) {
             binding.textName.setText(user.name);
             binding.textEmail.setText(user.name);
             if(null != user.image) {
                binding.imageProfile.setImageBitmap(getUserImage(user.image));
             }
        }
    }

    private Bitmap getUserImage( String encodedImage) {
        // convert 64base image to bitmap
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        // to convert to bitmap, require decoded bytes and BitFactory
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
