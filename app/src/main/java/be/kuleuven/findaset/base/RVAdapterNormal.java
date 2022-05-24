package be.kuleuven.findaset.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import be.kuleuven.findaset.R;

public class RVAdapterNormal extends RecyclerView.Adapter<RVAdapterNormal.ViewHolder> {
    private String[] userNames;
    private String[] userHints;
    private String[] userTimes;
    private String[] userRankings;

    public RVAdapterNormal(String[] userNames, String[] userHints, String[] userTimes, String[] userRankings) {
        this.userNames = userNames;
        this.userHints = userHints;
        this.userTimes = userTimes;
        this.userRankings = userRankings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_leaderboard, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvUserName.setText(userNames[position]);
        viewHolder.tvHints.setText("Hints:" + userHints[position]);
        viewHolder.tvTimes.setText("Times:" + userTimes[position]);
        viewHolder.tvRanking.setText(userRankings[position]);

        String ranking = userRankings[position];
        if (ranking.equals("1") || ranking.equals("2") || ranking.equals("3")) {
            //viewHolder.layoutBoard.setBackgroundColor(Color.rgb(140, 188, 255));
            viewHolder.layoutBoard.setBackground(viewHolder.bg_blue);
        } else {
            //viewHolder.layoutBoard.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.layoutBoard.setBackground(viewHolder.bg_white);
        }
    }

    @Override
    public int getItemCount() {
        return userNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvHints;
        TextView tvTimes;
        TextView tvRanking;
        ConstraintLayout layoutBoard;
        Drawable bg_white, bg_blue;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvHints = (TextView) view.findViewById(R.id.tvHints);
            tvTimes = (TextView) view.findViewById(R.id.tvTimes);
            tvRanking = (TextView) view.findViewById(R.id.tvRanking);
            layoutBoard = (ConstraintLayout) view.findViewById(R.id.rlContent);
            bg_white = view.getContext().getDrawable(R.drawable.imageview_shadow);
            bg_blue = view.getContext().getDrawable(R.drawable.imageview_shadow_blue);
        }
    }
}
