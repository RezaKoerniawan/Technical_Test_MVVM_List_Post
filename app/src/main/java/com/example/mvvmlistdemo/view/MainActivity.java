package com.example.mvvmlistdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mvvmlistdemo.databinding.ActivityMainBinding;
import com.example.mvvmlistdemo.model.Post;
import com.example.mvvmlistdemo.view.adapter.PostAdapter;
import com.example.mvvmlistdemo.viewmodel.PostViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PostViewModel viewModel;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        setupViewModel();
        setupSearch();
    }

    private void setupRecyclerView() {
        adapter = new PostAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(post -> {
            int postId = post.getId();

            viewModel.fetchPostById(postId);

            viewModel.getSelectedPost().observe(MainActivity.this, new Observer<Post>() {
                @Override
                public void onChanged(Post postDetail) {
                    if (postDetail != null) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("id", postDetail.getId());
                        intent.putExtra("title", postDetail.getTitle());
                        intent.putExtra("body", postDetail.getBody());
                        startActivity(intent);

                        viewModel.getSelectedPost().removeObserver(this);
                        viewModel.clearSelectedPost();
                    }
                }
            });

        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        viewModel.getPosts().observe(this, posts -> {
            binding.progressBar.setVisibility(View.GONE);
            if (posts != null) {
                adapter.setUsers(posts);
            } else {
                Toast.makeText(this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getFilteredPosts().observe(this, posts -> {
            adapter.setUsers(posts);
        });
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchPosts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}