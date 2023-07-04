package com.example.click_v1.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click_v1.databinding.ItemContainerRecentConverationBinding;
import com.example.click_v1.models.ChatMessage;

import java.util.List;


public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversationViewHolder>{

    private final List<ChatMessage> chatMessages;

    public RecentConversationAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create new view holder need to pass layout inflate, means call inflate on
        // layout binding and pass LayoutInflater, parent and false to it
        return new ConversationViewHolder(
                ItemContainerRecentConverationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationAdapter.ConversationViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    class ConversationViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConverationBinding binding;

        ConversationViewHolder(ItemContainerRecentConverationBinding itemContainerRecentConverationBinding) {
            super(itemContainerRecentConverationBinding.getRoot());
            binding = itemContainerRecentConverationBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConversationImage(chatMessage.conversationImage));
            binding.textName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);
        }
    }

    private Bitmap getConversationImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
    };

}
