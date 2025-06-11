package com.example.mvvmlistdemo.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmlistdemo.databinding.ItemUserBinding;
import com.example.mvvmlistdemo.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewHolder> {

    private List<Post> posts = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<Post> posts) {
        this.posts = posts != null ? posts : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new postViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class postViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding binding;

        public postViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(posts.get(position));
                }
            });
        }

        public void bind(Post post) {
            binding.tvId.setText(String.valueOf(post.getId()));

            if (post.getTitle() != null) {
                binding.tvTitle.setText(post.getTitle());
            } else {
                binding.tvTitle.setText("No Title");
            }
        }
    }
}
