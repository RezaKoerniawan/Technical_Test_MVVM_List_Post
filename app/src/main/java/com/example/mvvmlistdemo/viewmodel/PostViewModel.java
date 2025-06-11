package com.example.mvvmlistdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmlistdemo.model.Post;
import com.example.mvvmlistdemo.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    private PostRepository repository;
    private MutableLiveData<List<Post>> postsLiveData;
    private MutableLiveData<List<Post>> filteredPostsLiveData;
    private MutableLiveData<Post> selectedPost = new MutableLiveData<>();
    private List<Post> allPosts;

    public PostViewModel() {
        repository = PostRepository.getInstance();
        postsLiveData = new MutableLiveData<>();
        filteredPostsLiveData = new MutableLiveData<>();
        allPosts = new ArrayList<>();
    }

    public LiveData<List<Post>> getPosts() {
        if (postsLiveData.getValue() == null) {
            loadPosts();
        }
        return postsLiveData;
    }

    public LiveData<List<Post>> getFilteredPosts() {
        return filteredPostsLiveData;
    }

    private void loadPosts() {
        repository.getPosts().observeForever(posts -> {
            if (posts != null) {
                allPosts = posts;
                postsLiveData.setValue(posts);
                filteredPostsLiveData.setValue(posts);
            }
        });
    }

    public void searchPosts(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredPostsLiveData.setValue(allPosts);
        } else {
            List<Post> filtered = new ArrayList<>();
            for (Post post : allPosts) {
                if (post.getTitle() != null && post.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(post);
                }
            }
            filteredPostsLiveData.setValue(filtered);
        }
    }

    public LiveData<Post> getSelectedPost() {
        return selectedPost;
    }

    public void fetchPostById(int id) {
        repository.getPostById(id, selectedPost);
    }

    public void clearSelectedPost() {
        selectedPost.setValue(null);
    }
}
