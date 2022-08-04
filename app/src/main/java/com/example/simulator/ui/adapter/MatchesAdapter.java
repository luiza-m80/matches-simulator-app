package com.example.simulator.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.simulator.databinding.MatchItemBinding;
import com.example.simulator.domain.Match;
import com.example.simulator.ui.DetailActivity;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    private final List<Match> matches;

    public MatchesAdapter(List<Match> matches) {
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        MatchItemBinding binding = MatchItemBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context = viewHolder.itemView.getContext();
        Match match = matches.get(position);

        //Adapta os dados da partida (recuparada da API) para o layout.
        Glide.with(context).load(match.getHomeTeam().getImage()).into(viewHolder.binding.imageViewHomeTeam);
        viewHolder.binding.textViewHomeTeam.setText(match.getHomeTeam().getName());
        if (match.getHomeTeam().getScore() != null) {
            viewHolder.binding.textViewHomeTeamScore.setText(String.valueOf(match.getHomeTeam().getScore()));
        }
        Glide.with(context).load(match.getAwayTeam().getImage()).circleCrop().into(viewHolder.binding.imageViewAwayTeam);
        viewHolder.binding.textViewAwayTeam.setText(match.getAwayTeam().getName());
        if (match.getAwayTeam().getScore() != null) {
            viewHolder.binding.textViewAwayTeamScore.setText(String.valueOf(match.getAwayTeam().getScore()));
        }

        viewHolder.itemView.setOnClickListener( view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.Extras.MATCH, match);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MatchItemBinding binding;

        public ViewHolder(MatchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
