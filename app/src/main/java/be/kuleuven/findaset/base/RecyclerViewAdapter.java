package be.kuleuven.findaset.base;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import be.kuleuven.findaset.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String[] userNames;
    private final String[] userTimes;
    private final int[] userRankings;

    public RecyclerViewAdapter(String[] userNames, String[] userTimes, int[] userRankings) {
        this.userNames = userNames;
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
        viewHolder.getTvUserName().setText(userNames[position]);
        viewHolder.getTvTimes().setText(userTimes[position]);
        viewHolder.getTvNumber().setText(userRankings[position]);
        /*
        if (position == 0) {
            viewHolder.getLayoutBoard().setBackgroundColor(Color.parseColor("FFF1C2"));
            viewHolder.getTvNumber().setBackgroundColor(Color.parseColor("FFD75A"));
            viewHolder.getTvNumber().setTextColor(Color.parseColor("FFFFFF"));
        } else {
            if (position == 1 || position == 2) {
                viewHolder.getTvNumber().setBackgroundColor(Color.parseColor("18CF18"));
            } else {
                viewHolder.getTvNumber().setBackgroundColor(Color.parseColor("549AFC"));
            }
            viewHolder.getTvNumber().setTextColor(Color.parseColor("FFFFFF"));
            viewHolder.getLayoutBoard().setBackgroundColor(Color.TRANSPARENT);
        }

         */
    }

    @Override
    public int getItemCount() {
        return userNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;
        private final TextView tvTimes;
        private final TextView tvNumber;
        private final ConstraintLayout layoutBoard;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvTimes = (TextView) view.findViewById(R.id.tvTimes);
            tvNumber = (TextView) view.findViewById(R.id.tvNumber);
            layoutBoard = (ConstraintLayout) view.findViewById(R.id.rlContent);
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public TextView getTvTimes() {
            return tvTimes;
        }

        public TextView getTvNumber() {
            return tvNumber;
        }

        public ConstraintLayout getLayoutBoard() {
            return layoutBoard;
        }
    }

}
