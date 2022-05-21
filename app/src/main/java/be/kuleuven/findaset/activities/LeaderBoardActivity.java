package be.kuleuven.findaset.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.base.RecyclerViewAdapter;

public class LeaderBoardActivity extends AppCompatActivity {
    private TextView tvAll;
    private TextView tvTen;
    private ConstraintLayout tabModes;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter allAdapter;
    private RecyclerViewAdapter tenAdapter;
    private String[] namesAll;
    private String[] timesAll;
    private String[] rankingsAll;
    private String[] namesTen;
    private String[] timesTen;
    private String[] rankingsTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        tvAll = findViewById(R.id.tvAll);
        tvTen = findViewById(R.id.tvTen);
        tabModes = findViewById(R.id.tabModes);
        recyclerView = findViewById(R.id.rvBoard);

        namesAll = new String[]{"Sandro", "Arthur","Koen"};
        timesAll = new String[]{"1'30", "2'30","3'30"};
        rankingsAll = new String[]{"1","2","5"};
        namesTen = new String[]{"Sandro2", "Arthur","Koen"};
        timesTen = new String[]{"1'30", "2'30","3'30"};
        rankingsTen = new String[]{"1","2","5"};

        recyclerView = findViewById(R.id.rvBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allAdapter = new RecyclerViewAdapter(namesAll, timesAll, rankingsAll);
        tenAdapter = new RecyclerViewAdapter(namesTen, timesTen, rankingsTen);

        recyclerView.setAdapter(allAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvSetAnimation();
    }

    public void onClick_Back(View caller) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onClick_All(View caller) {
        setConstraintGravity(R.id.tvAll);

        recyclerView.setAdapter(allAdapter);
        rvSetAnimation();
    }

    public void onClick_Ten(View caller) {
        setConstraintGravity(R.id.tvTen);

        recyclerView.setAdapter(tenAdapter);
        rvSetAnimation();
    }

    private void rvSetAnimation() {
        LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(anim);
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
}