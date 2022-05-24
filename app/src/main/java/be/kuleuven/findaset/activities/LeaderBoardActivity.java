package be.kuleuven.findaset.activities;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.base.RVAdapterHighScore;
import be.kuleuven.findaset.base.RVAdapterNormal;

public class LeaderBoardActivity extends AppCompatActivity {
    private TextView tvAll;
    private TextView tvTen;
    private TextView tvHighScore;
    private ConstraintLayout tabModes;
    private RecyclerView recyclerView;
    private RVAdapterNormal allAdapter;
    private RVAdapterNormal tenAdapter;
    private RVAdapterHighScore highScoreAdapter;
    private String[] namesAll;
    private String[] hintsAll;
    private String[] timesAll;
    private String[] rankingsAll;
    private String[] namesTen;
    private String[] hintsTen;
    private String[] timesTen;
    private String[] rankingsTen;
    private String[] modes;
    private String[] hints;
    private String[] scores;
    private String [] dates;
    private String [] rankingsHighScore;
    private RequestQueue requestQueue;
    private String baseURL = "https://studev.groept.be/api/a21pt113/";
    private String [][] data;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        tvAll = findViewById(R.id.tvAll);
        tvTen = findViewById(R.id.tvTen);
        tvHighScore = findViewById(R.id.tvHighScore);
        tabModes = findViewById(R.id.tabModes);
        recyclerView = findViewById(R.id.rvBoard);
        requestQueue = Volley.newRequestQueue( this );

        getRankingsAll();
        getRankingsTen();
        recyclerView = findViewById(R.id.rvBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // TODO - bind data from credentials to ranking
        data = new String[2][3];
        rankingsHighScore = new String[2];
        try {
            readCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void readCredentials() throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        json = getJSONString(reader);
        try {
            JSONObject object = new JSONObject(json);

            JSONArray session = object.getJSONArray("session");
            String username = session.getJSONObject(0).getString("username");
            if(username.equals(" ")){
                JSONArray device = object.getJSONArray("device");

                JSONArray findAllScore = device.getJSONObject(0).getJSONArray("FindAllScore");
                String timeAll = findAllScore.getString(0);
                String hintNumAll = findAllScore.getString(1);
                String dateAll = findAllScore.getString(2);

                timeAll = formatTime(timeAll);

                data[0][0] = timeAll;
                data[0][1] = hintNumAll;
                data[0][2] = dateAll;

                JSONArray findTenScore = device.getJSONObject(0).getJSONArray("FindTenScore");
                String timeTen = findTenScore.getString(0);
                String hintNumTen = findTenScore.getString(1);
                String dateTen = findTenScore.getString(2);

                timeTen = formatTime(timeTen);

                data[1][0] = timeTen;
                data[1][1] = hintNumTen;
                data[1][2] = dateTen;

                rankingsHighScore = new String[]{"1","2"};

                modes = new String[]{"Find All", "Find Ten"};
                scores = new String[]{data[0][0], data[1][0]};
                hints = new String[]{data[0][1], data[1][1]};
                dates = new String[]{data[0][2], data[1][2]};
                highScoreAdapter = new RVAdapterHighScore(modes, hints, scores, dates, rankingsHighScore, true);
            }
            else {
                getRankingsLoggedUser(username);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String formatTime(String timeAll) {
        int elapsedMillis;
        if (timeAll.equals(" ") || timeAll.equals("-1")) {
            return "";
        }
        elapsedMillis = Integer.valueOf(timeAll);
        int minutes = (elapsedMillis / 1000) / 60;
        int seconds = (int) ((elapsedMillis / 1000) % 60);

        return Integer.toString(minutes) + "'" + Integer.toString(seconds) + "''";
    }

    private void getRankingsLoggedUser(String username) {
        String recordsUrl = baseURL + "highScoreRecord/" + username;
        JsonArrayRequest recordAll = new JsonArrayRequest(Request.Method.GET, recordsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject o = response.getJSONObject(0);

                    data[0][0] = formatTime(o.getString("allSetsRecord"));
                    data[1][0] = formatTime(o.getString("tenSetsRecord"));
                    data[0][1] = (String) o.get("hintsAllSets");
                    data[1][1] = (String) o.get("hintsTenSets");
                    data[0][2] = (String) o.get("dateAllSetsRecord");
                    data[1][2] = (String) o.get("dateTenSetsRecord");
                    rankingsHighScore[0] = (String) o.get("rankingAll");
                    rankingsHighScore[1] = (String) o.get("rankingTen");
                    if (data[0][1].equals("-1")) {
                        data[0][0] = "";
                        data[0][1] = "";
                        data[0][2] = "";
                        rankingsHighScore[0] = "";
                    }
                    if (data[1][1].equals("-1")) {
                        data[1][0] = "";
                        data[1][1] = "";
                        data[1][2] = "";
                        rankingsHighScore[1] = "";
                    }

                    modes = new String[]{"Find All", "Find Ten"};
                    scores = new String[]{data[0][0], data[1][0]};
                    hints = new String[]{data[0][1], data[1][1]};
                    dates = new String[]{data[0][2], data[1][2]};
                    highScoreAdapter = new RVAdapterHighScore(modes, hints, scores, dates, rankingsHighScore, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Database", "onErrorResponse: " + error);
            }
        });
        requestQueue.add(recordAll);
    }

    private String getJSONString(BufferedReader reader) throws IOException {
        String json = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            json = sb.toString();
        } finally {
            reader.close();
        }

        return json;
    }

    private void getRankingsAll() {
        String allSetsUrl = baseURL + "boardFindAll";
        JsonArrayRequest rankingRequestAll = new JsonArrayRequest(Request.Method.GET, allSetsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                namesAll = new String[response.length()];
                hintsAll = new String[response.length()];
                timesAll = new String[response.length()];
                rankingsAll = new String[response.length()];
                for (int i=0; i<response.length(); i++) {
                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);
                        namesAll[i] = (String) o.get("username");
                        hintsAll[i] = (String) o.get("hintsAllSets");
                        timesAll[i] = formatTime((String) o.get("allSetsRecord"));
                        rankingsAll[i] = (String) o.get("rankingAll");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                allAdapter = new RVAdapterNormal(namesAll, hintsAll, timesAll, rankingsAll);
                recyclerView.setAdapter(allAdapter);
                rvSetAnimation();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Database", "onErrorResponse: " + error);
            }
        });
        requestQueue.add(rankingRequestAll);
    }

    private void getRankingsTen() {
        String tenSetsUrl = baseURL + "boardFindTen";
        JsonArrayRequest rankingRequestTen = new JsonArrayRequest(Request.Method.GET, tenSetsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                namesTen = new String[response.length()];
                hintsTen = new String[response.length()];
                timesTen = new String[response.length()];
                rankingsTen = new String[response.length()];
                for (int i=0; i<response.length(); i++) {
                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);
                        namesTen[i] = (String) o.get("username");
                        hintsTen[i] = (String) o.get("hintsTenSets");
                        timesTen[i] = formatTime((String) o.get("tenSetsRecord"));
                        rankingsTen[i] = (String) o.get("rankingTen");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tenAdapter = new RVAdapterNormal(namesTen, hintsTen, timesTen, rankingsTen);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Database", "onErrorResponse: " + error);
            }
        });
        requestQueue.add(rankingRequestTen);
    }

    public void onClick_Back(View caller) {
        finish();
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

    public void onClick_HighScore(View caller) {
        setConstraintGravity(R.id.tvHighScore);

        recyclerView.setAdapter(highScoreAdapter);
        rvSetAnimation();
    }

    private void rvSetAnimation() {
        LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(anim);
    }

    private void setConstraintGravity(int viewId) {
        tvAll.setTextColor(getColor(R.color.textColorSecondary));
        tvTen.setTextColor(getColor(R.color.textColorSecondary));
        tvHighScore.setTextColor(getColor(R.color.textColorSecondary));

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