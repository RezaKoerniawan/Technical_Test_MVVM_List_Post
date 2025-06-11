package com.example.mvvmlistdemo.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmlistdemo.model.Post;
import com.example.mvvmlistdemo.network.ApiService;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostRepository {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private ApiService apiService;
    private static PostRepository instance;

    private PostRepository() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Post>> getPosts() {
        MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();

        apiService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postsLiveData.setValue(response.body());
                } else {
                    postsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                postsLiveData.setValue(null);
            }
        });

        return postsLiveData;
    }

    public void getPostById(int id, final MutableLiveData<Post> postLiveData) {
        apiService.getPostById(id).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postLiveData.setValue(response.body());
                } else {
                    postLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                postLiveData.setValue(null);
            }
        });
    }


}
