package be.kuleuven.findaset.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.base.RecyclerViewAdapter;

public class LeaderBoardActivity extends AppCompatActivity {
    private TextView tvAll;
    private TextView tvTen;
    private ConstraintLayout tabModes;
    private RecyclerViewAdapter boardAdapter;
    private String[] dateSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        tvAll = findViewById(R.id.tvAll);
        tvTen = findViewById(R.id.tvTen);
        tabModes = findViewById(R.id.tabModes);

        dateSet = new String[]{"Sandro", "1.30"};
        RecyclerView recyclerView = findViewById(R.id.rvBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        boardAdapter = new RecyclerViewAdapter(dateSet);
        recyclerView.setAdapter(boardAdapter);
    }

    public void onClick_Back(View caller) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onClick_All(View caller) {
        setConstraintGravity(R.id.tvAll);
    }

    public void onClick_Ten(View caller) {
        setConstraintGravity(R.id.tvTen);
    }

    private void setConstraintGravity(int viewId) {
        tvAll.setTextColor(getColor(R.color.textColorSecondary));
        tvTen.setTextColor(getColor(R.color.textColorSecondary));

        ConstraintSet tabCons = new ConstraintSet();
        tabCons.clone(tabModes);
        TextView chosen = (TextView) findViewById(viewId);
        chosen.setTextColor(getColor(R.color.colorPrimary));
        tabCons.connect(
                R.id.tab,
                ConstraintSet.START,
                viewId,
                ConstraintSet.START,
                0
        );
        tabCons.connect(
                R.id.tab,
                ConstraintSet.END,
                viewId,
                ConstraintSet.END,
                0
        );
        tabCons.connect(
                R.id.tab,
                ConstraintSet.TOP,
                viewId,
                ConstraintSet.BOTTOM,
                0
        );
        animate(tabModes, tabCons);
    }

    private void animate(ConstraintLayout cl, ConstraintSet set) {
        TransitionManager.beginDelayedTransition(cl, new MyTransition());
        set.applyTo(cl);
    }

    static public class MyTransition extends TransitionSet {
        {
            setDuration(500);
            setOrdering(ORDERING_SEQUENTIAL);
            addTransition(new TransitionSet() {
                {
                    addTransition(new Fade(Fade.OUT));
                    addTransition(new ChangeBounds());
                    addTransition(new Fade(Fade.IN));
                }
            });
        }
    }

    /*
    private var mLeaderBoard = LearnerRecyclerViewAdapter<LearnPeople>(R.layout.learn_item_leaderboard
            , onBind = { view: View, people: LearnPeople, i: Int ->
            view.ivUser.loadImageFromResources(this,people.img)
    view.tvUserName.text = people.name
    view.tvPoints.text = "${Random().nextInt(200000)} Points"
    view.tvLeaderBoardNumber.text = (i + 1).toString()
            if (i == 0) {
        view.rlContent.setBackgroundColor(learnAppColor(R.color.learn_color_light_yellow))
        view.tvLeaderBoardNumber.learAapplyStrokedBackground(learnAppColor(R.color.learn_color_musturd_yellow))
        view.tvLeaderBoardNumber.setTextColor(learnAppColor(R.color.learn_white))
    } else {
        if (i == 1 || i == 2) {
            view.tvLeaderBoardNumber.learAapplyStrokedBackground(
                    learnAppColor(R.color.learn_transparent),
                    learnAppColor(R.color.learn_color_musturd_yellow)
            )
            view.tvLeaderBoardNumber.setTextColor(learnAppColor(R.color.learn_color_musturd_yellow))
        } else {
            view.tvLeaderBoardNumber.learAapplyStrokedBackground(
                    learnAppColor(R.color.learn_transparent),
                    learnAppColor(R.color.learn_mettal_grey)
            )
            view.tvLeaderBoardNumber.setTextColor(learnAppColor(R.color.learn_textColorSecondary))
        }
        view.rlContent.setBackgroundColor(Color.TRANSPARENT)
    }
})

     */
}