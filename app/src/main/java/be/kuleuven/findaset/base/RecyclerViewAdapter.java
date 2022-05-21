package be.kuleuven.findaset.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import be.kuleuven.findaset.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String[] userScores;

    public RecyclerViewAdapter(String[] dataSet) {
        userScores = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_leaderboard, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTvUserName().setText(userScores[position]);
        viewHolder.getTvTimes().setText(userScores[position]);
    }

    @Override
    public int getItemCount() {
        return userScores.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;
        private final TextView tvTimes;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            tvTimes = (TextView) view.findViewById(R.id.tvTimes);
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public TextView getTvTimes() {
            return tvTimes;
        }
    }

}
