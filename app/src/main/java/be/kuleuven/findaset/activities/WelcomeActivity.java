package be.kuleuven.findaset.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
            JSONObject object = new JSONObject(json); // this will get you the entire JSON node
            //handle object
            JSONArray device = object.getJSONArray("device");
            Random rd = new Random();
            int id = rd.nextInt(9000) + 1000;
            object.getJSONArray("device").getJSONObject(0).put("thisDevice",Integer.toString(id));
            updateCredentials(object);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateCredentials(JSONObject object) throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        BufferedWriter output = new BufferedWriter(new FileWriter(s));
        output.write(object.toString());
        output.close();
    }

    private void setTest(String str) {
        TextView testTv = (TextView) findViewById(R.id.testTextWelcome);
        testTv.setText(str);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick_Login(View caller) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_Board(View caller) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}