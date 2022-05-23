package be.kuleuven.findaset.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.model.FindAll;
import be.kuleuven.findaset.model.InterfaceFindASet;

public class WelcomeActivity extends AppCompatActivity {
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
    private ImageView[] randomCards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        randomCards = new ImageView[3];
        randomCards[0] = findViewById(R.id.ivRandomCard1);
        randomCards[1] = findViewById(R.id.ivRandomCard2);
        randomCards[2] = findViewById(R.id.ivRandomCard3);
        setRandomCards();

        try {
            checkCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            readCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkCredentials() throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        try{
            new BufferedReader(new FileReader(s));
        }
        catch (Exception e){
            generateCredentials();
        }
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

    private void generateCredentials() throws IOException {
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("credentials")));
        String json = "";
        json = getJSONString(reader);

        try {
            JSONObject object = new JSONObject(json);
            Random rd = new Random();
            int id = rd.nextInt(9000) + 1000;
            JSONArray device = object.getJSONArray("device");
            device.getJSONObject(0).put("thisDevice",Integer.toString(id));
            writeCredentials(object);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeCredentials(JSONObject object) throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        BufferedWriter output = new BufferedWriter(new FileWriter(s));
        output.write(object.toString());
        output.close();
    }

    private void setTest(String str) {
        TextView testTv = (TextView) findViewById(R.id.testTextWelcome);
        testTv.setText(str);
    }

    private void updateCredentials(String username, String hash) throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        json = getJSONString(reader);

        try {
            JSONObject object = new JSONObject(json);
            JSONArray session = object.getJSONArray("session");
            session.getJSONObject(0).put("username",username);
            session.getJSONObject(0).put("hash",hash);
            writeCredentials(object);
        }
        catch (JSONException e) {
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

            JSONArray device = object.getJSONArray("device");
            String deviceId = device.getJSONObject(0).getString("thisDevice");
            setTest(deviceId);

            JSONArray session = object.getJSONArray("session");
            String username = session.getJSONObject(0).getString("username");
            if(!username.equals(" ")){

                TextView helloNote = (TextView) findViewById(R.id.tvHelloNote);
                helloNote.setText("Hello "+username+"! ");
                LinearLayout layout = findViewById(R.id.helloLayout);
                layout.setVisibility(View.VISIBLE);

                TextView tvLogin = (TextView) findViewById(R.id.tvLogin);
                tvLogin.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick_Info(View caller) {
        showInfoDialog();
    }

    private void showInfoDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_main);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_custom_borders);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getString(R.string.welcome_info_title));
        TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
        dialogText.setText(getString(R.string.welcome_info_content));
        dialog.show();
    }

    public void onClick_FindAll(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }

    public void onClick_FindTen(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 2);
        startActivity(intent);
    }

    public void onClick_Learning(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 3);
        startActivity(intent);
    }

    public void onClick_Login(View caller) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_Logout(View caller) {
        try {
            updateCredentials(" ", " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onClick_Board(View caller) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }

    public void setRandomCards() {
        InterfaceFindASet findASet = new FindAll();
        int[][] featureMatrix = findASet.getFeatureMatrix();
        for (int i=0; i<3; i++) {
            int nextCardId = featureMatrix[i][0] * 1000 + featureMatrix[i][1] * 100 + featureMatrix[i][2] * 10 + featureMatrix[i][3];
            randomCards[i].setImageBitmap(combineImageIntoOne(setBitmaps(nextCardId)));
            randomCards[i].setBackground(getDrawable(R.drawable.imageview_shadow));
        }
    }

    // TODO refactor to a new class
    private ArrayList<Bitmap> setBitmaps(int card) {
        int color = (card%1000)/100 - 1;
        int shading = (card%100)/10 - 1;
        int shape = card%10 - 1;
        int index = shape * 9 + color * 3 + shading;
        Bitmap test = BitmapFactory.decodeResource(getResources(), cardPicturesIds[index]);
        ArrayList<Bitmap> newBitMap = new ArrayList<>();
        for (int i = 0; i < card/1000; i++) {
            newBitMap.add(test);
        }
        return newBitMap;
    }

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
}