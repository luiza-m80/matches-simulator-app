package com.example.simulator.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.simulator.R;
import com.example.simulator.data.MatchesAPI;
import com.example.simulator.databinding.ActivityMainBinding;
import com.example.simulator.domain.Match;
import com.example.simulator.ui.adapter.MatchesAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityJava extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MatchesAPI matchesAPI;
    private MatchesAdapter matchesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHttpClient();
        setupMatchesList();
        setupMatchesRefresh();
        setupFloatingActionButton();
    }

    private void setupHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luiza-m80.github.io/matches-simulator-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        matchesAPI = retrofit.create(MatchesAPI.class);
    }

    private void setupFloatingActionButton() {
        binding.floatingActionButton.setOnClickListener(view -> view.animate().rotationBy(360).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                for (int i = 0; i < matchesAdapter.getItemCount(); i++) {
                    Match match = matchesAdapter.getMatches().get(i);
                    Random random = new Random();
                    match.getHomeTeam().setScore(random.nextInt(match.getHomeTeam().getStars() + 1));
                    match.getAwayTeam().setScore(random.nextInt(match.getAwayTeam().getStars() + 1));
                    matchesAdapter.notifyItemChanged(i);
                }
            }

        }).start());
    }

    private void setupMatchesList() {
        binding.recyclerViewMatches.setHasFixedSize(true);
        binding.recyclerViewMatches.setLayoutManager(new LinearLayoutManager(this));
        findMatchesFromAPI();
    }

    private void setupMatchesRefresh() {
        binding.swipeRefreshLayoutMatches.setOnRefreshListener(this::findMatchesFromAPI);
    }

    private void findMatchesFromAPI() {
        binding.swipeRefreshLayoutMatches.setRefreshing(true);
        matchesAPI.getMatches().enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.isSuccessful()) {
                    matchesAdapter = new MatchesAdapter(response.body());
                    binding.recyclerViewMatches.setAdapter(matchesAdapter);
                } else {
                    showErrorMessage();
                }
                binding.swipeRefreshLayoutMatches.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                showErrorMessage();
                binding.swipeRefreshLayoutMatches.setRefreshing(false);
            }
        });
    }

    private void showErrorMessage() {
        Snackbar.make(binding.recyclerViewMatches, R.string.error_api, Snackbar.LENGTH_SHORT).show();
    }


}
