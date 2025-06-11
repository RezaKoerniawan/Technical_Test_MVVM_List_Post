package com.example.mvvmlistdemo.network;

import com.example.mvvmlistdemo.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{id}")
    Call<Post> getPostById(@retrofit2.http.Path("id") int id);
}
