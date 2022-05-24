package be.kuleuven.findaset.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.model.FindAll;
import be.kuleuven.findaset.model.FindLearning;
import be.kuleuven.findaset.model.FindTen;
import be.kuleuven.findaset.model.InterfaceFindASet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Integer[] cardPicturesIds = {
            R.drawable.ovaal1groen, R.drawable.ovaal2groen, R.drawable.ovaal3groen,
            R.drawable.ovaal1rood, R.drawable.ovaal2rood, R.drawable.ovaal3rood,
            R.drawable.ovaal1paars, R.drawable.ovaal2paars, R.drawable.ovaal3paars,
            R.drawable.ruit1groen, R.drawable.ruit2groen, R.drawable.ruit3groen,
            R.drawable.ruit1rood, R.drawable.ruit2rood, R.drawable.ruit3rood,
            R.drawable.ruit1paars, R.drawable.ruit2paars, R.drawable.ruit3paars,
            R.drawable.tilde1groen, R.drawable.tilde2groen, R.drawable.tilde3groen,
            R.drawable.tilde1rood, R.drawable.tilde2rood, R.drawable.tilde3rood,
            R.drawable.tilde1paars, R.drawable.tilde2paars, R.drawable.tilde3paars};
    private InterfaceFindASet gameModel;
    private ImageView[] cardImages;
    private Button[] featureBoxes;
    private TextView learningContinue;
    private TextView foundedNumber;
    private Chronometer stopWatch;
    private String dialogTitleStr;
    private String dialogContentStr;
    private Button refreshBtn;
    private String username;
    private int[] highscore; // {best time, hints}
    private TextView txtInfo;
    private int mode;
    private Button hintBtn;

    /**
     * Firstly bound all fields with UI components.
     * <p>
     * Then bound gameModel with new FindASet()
     * to use the method in FindASet.
     *
     * @param savedInstanceState saved content
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init stopWatch
        stopWatch = findViewById(R.id.stopWatch);

        foundedNumber = findViewById(R.id.foundedCards);
        learningContinue = findViewById(R.id.tvLearningModeContinue);
        learningContinue.setVisibility(View.INVISIBLE);
        refreshBtn = findViewById(R.id.refreshBtn);
        hintBtn = findViewById(R.id.hintBtn);

        cardImages = new ImageView[12];
        cardImages[0] = findViewById(R.id.card1);
        cardImages[1] = findViewById(R.id.card2);
        cardImages[2] = findViewById(R.id.card3);
        cardImages[3] = findViewById(R.id.card4);
        cardImages[4] = findViewById(R.id.card5);
        cardImages[5] = findViewById(R.id.card6);
        cardImages[6] = findViewById(R.id.card7);
        cardImages[7] = findViewById(R.id.card8);
        cardImages[8] = findViewById(R.id.card9);
        cardImages[9] = findViewById(R.id.card10);
        cardImages[10] = findViewById(R.id.card11);
        cardImages[11] = findViewById(R.id.card12);

        featureBoxes = new Button[4];
        featureBoxes[0] = findViewById(R.id.sizeBtn);
        featureBoxes[1] = findViewById(R.id.colorBtn);
        featureBoxes[2] = findViewById(R.id.shadingBtn);
        featureBoxes[3] = findViewById(R.id.typeBtn);

        for (int i = 0; i < 12; i++) {
            cardImages[i].setOnClickListener(this);
            cardImages[i].setBackground(getDrawable(R.drawable.imageview_shadow));
        }

        try {
            username = getUsername();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.txtInfo = (TextView) findViewById(R.id.userText);

        InterfaceFindASet findASet = null;
        Bundle extras = getIntent().getExtras();
        this.mode = extras.getInt("mode");

        this.highscore = new int[2];

        if(mode == 1){
            findASet = new FindAll();
            try {
                getHighscore();
            } catch (IOException e) {
                e.printStackTrace();
            }
            notifyFeatureBoxGone();
            dialogTitleStr = getString(R.string.main_more_findAll_title);
            dialogContentStr = getString(R.string.main_more_findAll_content);
        }
        else if(mode == 2){
            findASet = new FindTen();
            try {
                getHighscore();
            } catch (IOException e) {
                e.printStackTrace();
            }
            notifyFeatureBoxGone();
            dialogTitleStr = getString(R.string.main_more_findTen_title);
            dialogContentStr = getString(R.string.main_more_findTen_content);
        }
        else if(mode == 3){
            findASet = new FindLearning();
            try {
                getHighscore();
            } catch (IOException e) {
                e.printStackTrace();
            }
            notifyFeatureBoxGrey();
            dialogTitleStr = getString(R.string.feature_box_explanation_title);
            dialogContentStr = getString(R.string.feature_box_explanation_content);
            stopWatch.setVisibility(View.INVISIBLE);
        }

        setGameModel(findASet);
        gameModel.startNewGame();
        notifyStartStopWatch();
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

    private String getUsername() throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        String username = "";
        json = getJSONString(reader);
        try {
            JSONObject object = new JSONObject(json);

            JSONArray session = object.getJSONArray("session");
            username = session.getJSONObject(0).getString("username");
            if(username.equals(" ")){
                username = "guest";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return username;
    }

    public void setGameModel(InterfaceFindASet newGameModel) {
        this.gameModel = newGameModel;
        this.gameModel.setUI(this);
    }

    public void onClick_refreshBtn (View caller) {
        hintBtn.setEnabled(true);
        for (int i = 0; i < gameModel.getSelectedCardsIndex().size(); i++) {
            gameModel.unselect(gameModel.getSelectedCardsIndex().get(i));
        }
        notifyDisableHint();
        gameModel.startNewGame();
        notifyStartStopWatch();
        notifyFoundedCardsChange(0);
    }

    public void onClick_hintBtn (View caller) {
        notifyHint();
        gameModel.showSet();
    }

    public void onClick_More (View caller) {
        showDialog();
    }

    public void onClick_Back(View caller) {
        finish();
    }

    /**
     * Display images of all cards.
     */
    public void notifyNewGame(int sizeOfCards) {
        for (int i = 0; i < sizeOfCards; i++) {
            cardImages[i].setEnabled(true);
            cardImages[i].setVisibility(View.VISIBLE);
            notifyCard(i);
        }
    }

    public void notifyStartStopWatch() {
        //Init stopWatch
        stopWatch.setBase(SystemClock.elapsedRealtime());
        stopWatch.start();
    }

    /**
     * Display images of one card according to index.
     * 1. Gets the cards created on init
     * 2. Creates Image bitmap based on the card images
     */
    public void notifyCard(int index) {
        int nextCardId = gameModel.getCardsIdTable().get(index);
        cardImages[index].setImageBitmap(combineImageIntoOne(setBitmaps(nextCardId)));
        //cardTexts[index].setText(nextCard.toString());
    }

    /**
     * First get the index of which call the method.
     * <p>
     * If there are already 3 selected cards, remove the first.
     * <p>
     * After that, call checkSet() to check if there is set.
     * <p>
     * If there is, call updateTable().
     */
    @Override
    public void onClick(View clickedView) {
        int index = Arrays.asList(cardImages).indexOf(clickedView);
        gameModel.toggle(index);
    }

    /**
     * If there is no green border, add one.
     * If there is already one green border, delete it.
     */
    public void notifySelect(int index) {
        cardImages[index].setBackground(getDrawable(R.drawable.selected_card));
    }

    public void notifyUnselect(int index) {
        //cardImages[index].setBackgroundColor(getColor(R.color.transparent));
        cardImages[index].setBackground(getDrawable(R.drawable.imageview_shadow));
    }

    public void notifyHint() {
        ArrayList<Integer> set = gameModel.getFirstSetOnPage();
        for (int i=0; i<3; i++) {
            int index = set.get(i);
            if(!gameModel.getIsCardSelected(index))
                gameModel.unselect(set.get(i));
            cardImages[index].setBackground(getDrawable(R.drawable.show_set));
        }
    }

    public void notifyDisableHint() {
        ArrayList<Integer> set = gameModel.getFirstSetOnPage();
        for (int i=0; i<3; i++) {
            int index = set.get(i);
            cardImages[index].setBackground(getDrawable(R.drawable.imageview_shadow));
            if(gameModel.getIsCardSelected(index))
                gameModel.select(set.get(i));
        }
    }

    public void notifyFoundedCardsChange(int newNumber) {
        String number = "";
        if (newNumber < 10)
            number = "0" + newNumber;
        else
            number = String.valueOf(newNumber);
        foundedNumber.setText(number);
    }

    public void notifyUnavailable(int index) {
        cardImages[index].setEnabled(false);
        cardImages[index].setVisibility(View.INVISIBLE);
    }

    public void notifyDisabled(int index) {
        cardImages[index].setEnabled(false);
    }

    /**
     * Retrieves the shape
     * cardPicturesIds[] is an array that stores all Ids of basic components drawables.
     * <p>
     * According to the feature IDs, the index of basic component in the array can be
     * derived.
     *
     * @param card Object in AbstractCard class.
     * @return newBitMap - An arraylist with the size equals to card.getSize(),
     * which stores basic components.
     */
    private ArrayList<Bitmap> setBitmaps(int card) {
        int color = (card%1000)/100 - 1;
        int shading = (card%100)/10 - 1;
        int shape = card%10 - 1;
        int index = shape * 9 + color * 3 + shading;
        Bitmap component = BitmapFactory.decodeResource(getResources(), cardPicturesIds[index]);
        ArrayList<Bitmap> newBitMap = new ArrayList<>();
        for (int i = 0; i < card/1000; i++) {
            newBitMap.add(component);
        }
        return newBitMap;
    }

    /**
     * Forms image
     * Combine basic components to final image of each card according to the ShapeCountInt.
     * <p>
     * Set the gap between components to make sure the width of new Bitmap equals to
     * 4.5 times of width of basic components, which is fixed no matter how many components.
     *
     * @param bitmap the basic component of
     * @return a new Bitmap contained several components
     */
    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int width = bitmap.get(0).getWidth();
        int height = bitmap.get(0).getHeight();
        int size = bitmap.size();

        Bitmap temp = Bitmap.createBitmap((int) (4.5 * width), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        float left = 0;
        if (size == 3) {
            for (int i = 0; i < size; i++) {
                left = (i == 0 ? 0.25f * width : left + 1.5f * width);
                canvas.drawBitmap(bitmap.get(i), left, 0f, null);
            }
        } else if (size == 2) {
            for (int i = 0; i < size; i++) {
                left = (i == 0 ? 0.5f * width : left + 2.5f * width);
                canvas.drawBitmap(bitmap.get(i), left, 0f, null);
            }
        } else {
            left = 1.75f * width;
            canvas.drawBitmap(bitmap.get(0), left, 0f, null);
        }

        return temp;
    }

    public void notifyWin() throws IOException {
        hintBtn.setEnabled(false);
        stopWatch.stop();
        if (mode != 3){
            long elapsedMillis = SystemClock.elapsedRealtime() - stopWatch.getBase();
            long minutes = (elapsedMillis / 1000)  / 60;
            int seconds = (int)((elapsedMillis / 1000) % 60);
            String winMessage = "You won!! \nElapsed time " + Long.toString(minutes) + "'" + Integer.toString(seconds) + "''";

            int highScoreTime = highscore[0];
            int highScoreHints = highscore[1];

            // First time of a player
            if(highScoreHints == -1){
                highscore[0] = (int) elapsedMillis;
                highscore[1] = gameModel.getHints();
                setNewHighscore((int) elapsedMillis, gameModel.getHints());
            }
            // compare millis with credentials and update if lower
            else if (gameModel.getHints() < highScoreHints){
                if(elapsedMillis < highScoreTime){
                    highscore[0] = (int) elapsedMillis;
                    highscore[1] = gameModel.getHints();
                    setNewHighscore((int) elapsedMillis, gameModel.getHints());
                }
            }
            notifyWinDialog(winMessage);
        }
        else {
            String winMessage = "You found all possible sets in 81 cards";
            notifyWinDialog(winMessage);
        }
    }

    private void notifyWinDialog(String winMessage) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_win);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_custom_borders);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Congrats!");
        TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
        dialogText.setText(winMessage);
        dialog.show();
    }


    private void setNewHighscore(int highScoreTime, int highScoreHints) throws IOException {
        if(isLogged()){
            String baseURL = "https://studev.groept.be/api/a21pt113/";
            String requestURL = null;

            if (mode == 2) {
                requestURL = baseURL + "updateFindTenRecord" + "/" + highScoreTime + "/" + highScoreHints + "/" + username;
            }
            else if (mode == 1){
                requestURL = baseURL + "updateFindAllRecord" + "/" + highScoreTime + "/" + highScoreHints + "/" + username;
            }

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                    response -> {
                        try {
                            JSONArray responseArray = new JSONArray(response);
                        }
                        catch(JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
                    },

                    error -> {
                        Throwable e = new Throwable();
                        Log.e( "Database", e.getMessage(), e );
                    }
            );

            requestQueue.add(submitRequest);
        }
        else{
            updateDeviceCredentials();
        }
    }

    private void writeCredentials(JSONObject object) throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        BufferedWriter output = new BufferedWriter(new FileWriter(s));
        output.write(object.toString());
        output.close();
    }

    private void getDeviceCredentials() throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        json = getJSONString(reader);

        try {
            JSONObject object = new JSONObject(json);
            JSONArray device = object.getJSONArray("device");
            if(mode == 1){
                JSONArray findAllScore = device.getJSONObject(0).getJSONArray("FindAllScore");
                String time = findAllScore.getString(0);
                String hintNum = findAllScore.getString(1);
                if(time.equals(" ")){
                    highscore[0] = -1;
                    if(hintNum.equals(" ")){
                        highscore[1] = -1;
                    }
                }
                else{
                    highscore[0] = Integer.parseInt(time);
                    highscore[1] = Integer.parseInt(hintNum);
                }
            }
            else if(mode == 2){
                JSONArray findTenScore = device.getJSONObject(0).getJSONArray("FindTenScore");
                String time = findTenScore.getString(0);
                String hintNum = findTenScore.getString(1);
                if(time.equals(" ")){
                    highscore[0] = -1;
                    if(hintNum.equals(" ")){
                        highscore[1] = -1;
                    }
                }
                else{
                    highscore[0] = Integer.parseInt(time);
                    highscore[1] = Integer.parseInt(hintNum);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        txtInfo.setText(username);

    }

    private void updateDeviceCredentials() throws IOException {

        int highScoreTime = highscore[0];
        int highScoreHints = highscore[1];

        String s = getFilesDir() + "/" + "credentials";
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        json = getJSONString(reader);

        try {
            JSONObject object = new JSONObject(json);
            JSONArray device = object.getJSONArray("device");
            JSONArray newArray = new JSONArray();
            if(mode == 1){
                newArray.put(highScoreTime);
                newArray.put(highScoreHints);
                newArray.put("2022-01-01");
                device.getJSONObject(0).put("FindAllScore", newArray);
                writeCredentials(object);
            }
            else if (mode == 2){
                newArray.put(highScoreTime);
                newArray.put(highScoreHints);
                newArray.put("2022-01-01");
                device.getJSONObject(0).put("FindTenScore", newArray);
                writeCredentials(object);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isLogged() throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        json = getJSONString(reader);

        try {
            JSONObject object = new JSONObject(json);
            JSONArray session = object.getJSONArray("session");
            String user = session.getJSONObject(0).getString("username");
            if(user.equals(" ")){
                return false;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void getHighscore() throws IOException {

        if(!isLogged()) {
            getDeviceCredentials(); // and sets the field highscore
        }
        else {
            String baseURL = "https://studev.groept.be/api/a21pt113/";
            String requestURL = null;

            if (mode == 2) {
                requestURL = baseURL + "findTenRecord" + "/" + username;
            }
            else if (mode == 1){
                requestURL = baseURL + "findAllRecord" + "/" + username;
            }

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                    response -> {
                        try {
                            JSONArray responseArray = new JSONArray(response);
                            JSONObject curObject = responseArray.getJSONObject( 0 );

                            if (mode == 1){
                                highscore[0] = Integer.parseInt(curObject.getString("allSetsRecord"));
                                highscore[1] = Integer.parseInt(curObject.getString("hintsAllSets"));
                            }
                            else if (mode == 2) {
                                highscore[0] = Integer.parseInt(curObject.getString("tenSetsRecord"));
                                highscore[1] = Integer.parseInt(curObject.getString("hintsTenSets"));
                            }
                            // testing register and login
                            txtInfo.setText(username);
                        }
                        catch(JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
                    },

                    error -> {
                        Throwable e = new Throwable();
                        Log.e( "Database", e.getMessage(), e );
                    }
            );

            requestQueue.add(submitRequest);
        }
    }

    public void notifyFeatureSame(int index) {
        featureBoxes[index].setBackgroundColor(getColor(R.color.light_green));
    }

    public void notifyFeatureDifferent(int index) {
        featureBoxes[index].setBackgroundColor(getColor(R.color.light_yellow));
    }

    public void notifyFeatureBoxGrey() {
        for (Button box : featureBoxes) {
            box.setVisibility(View.VISIBLE);
            box.setBackgroundColor(getColor(R.color.metal_grey));
        }
    }

    public void notifyFeatureBoxGone() {
        for (Button box : featureBoxes) {
            box.setVisibility(View.INVISIBLE);
        }
    }

    public void notifyLearningModeFindASet() {
        learningContinue.setVisibility(View.VISIBLE);
        for (ImageView cards : cardImages) {
            cards.setClickable(false);
        }
        refreshBtn.setEnabled(false);
        hintBtn.setEnabled(false);
    }

    public void onClick_continueLearningMode (View caller) {
        gameModel.updateTable(gameModel.getSelectedCardsIndex());
        notifyFeatureBoxGrey();
        gameModel.getSelectedCardsIndex().clear();
        for (ImageView cards : cardImages) {
            cards.setClickable(true);
        }
        learningContinue.setVisibility(View.INVISIBLE);
        refreshBtn.setEnabled(true);
        hintBtn.setEnabled(true);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_main);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_custom_borders);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(dialogTitleStr);
        TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
        dialogText.setText(dialogContentStr);
        dialog.show();
    }
}