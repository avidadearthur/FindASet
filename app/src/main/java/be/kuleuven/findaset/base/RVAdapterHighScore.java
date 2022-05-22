package be.kuleuven.findaset.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import be.kuleuven.findaset.R;

public class RVAdapterHighScore extends RecyclerView.Adapter<RVAdapterHighScore.ViewHolder> {
    private String[] modes;
    private String[] scores;
    private String[] dates;
    private String[] rankings;

    public RVAdapterHighScore(String[] modes, String[] scores, String[] dates, String[] rankings) {
        this.modes = modes;
        this.scores = scores;
        this.dates = dates;
        this.rankings = rankings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_high_score, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvMode.setText(modes[position]);
        viewHolder.tvScore.setText(scores[position]);
        viewHolder.tvDate.setText(dates[position]);
        viewHolder.tvRanking.setText(rankings[position]);

        viewHolder.tvRanking.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return modes.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMode;
        TextView tvScore;
        TextView tvDate;
        TextView tvRanking;
        ConstraintLayout layoutHighScore;

        public ViewHolder(View view) {
            super(view);
            tvMode = (TextView) view.findViewById(R.id.tvMode);
            tvScore = (TextView) view.findViewById(R.id.tvHigScore);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvRanking = (TextView) view.findViewById(R.id.tvRankingHighScore);
            layoutHighScore = (ConstraintLayout) view.findViewById(R.id.rlContentHighScore);
        }
    }
}
