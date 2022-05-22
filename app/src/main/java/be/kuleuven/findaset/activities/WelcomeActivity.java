package be.kuleuven.findaset.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.Random;

import be.kuleuven.findaset.R;

public class WelcomeActivity extends AppCompatActivity {
    private TextView testTv;
    private ImageButton infoFindAllDialog;
    private ImageButton infoFindTen;
    private ImageButton infoFindLearning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        infoFindAllDialog = findViewById(R.id.infoFindAll);
        infoFindTen = findViewById(R.id.infoFindTen);
        infoFindLearning = findViewById(R.id.infoFindLearning);

        infoFindAllDialog.setOnClickListener(new View.OnClickListener() {
            int dialogIndex = 0;
            @Override
            public void onClick(View view) {
                showFindAllDialog(dialogIndex);
            }
        });

        infoFindTen.setOnClickListener(new View.OnClickListener() {
            int dialogIndex = 1;
            @Override
            public void onClick(View view) {
                showFindAllDialog(dialogIndex);
            }
        });

        infoFindLearning.setOnClickListener(new View.OnClickListener() {
            int dialogIndex = 2;
            @Override
            public void onClick(View view) {
                showFindAllDialog(dialogIndex);
            }
        });

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

    private void showFindAllDialog(int dialogIndex) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.find_all_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_custom_borders);
        if (dialogIndex == 0){
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
            dialogTitle.setText("Find All");
            TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
            dialogText.setText("In Find All mode the player has to find all possible sets\n" +
                    "        within the 81 cards generated throughout the game.");

        }
        else if (dialogIndex == 1){
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
            dialogTitle.setText("Something else");
            TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
            dialogText.setText("Lorem Ipsum");

        }
        else if (dialogIndex == 2){
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
            dialogTitle.setText("Bla Bla");
            TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
            dialogText.setText("Dolor sit amet");

        }
        dialog.show();
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

    public void onClick_FindAll(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick_FindTen(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick_Learning(View caller) {
        Intent intent = new Intent(this, LearningActivity.class);
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
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onClick_Board(View caller) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}