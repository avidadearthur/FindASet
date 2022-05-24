package be.kuleuven.findaset.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import be.kuleuven.findaset.R;

public class LoginActivity extends AppCompatActivity {
    private TextView loginError;
    private RequestQueue requestQueue;
    private String baseURL = "https://studev.groept.be/api/a21pt113/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginError = findViewById(R.id.failureLogin);
        loginError.setVisibility(View.INVISIBLE);
    }

    public void onClick_Join(View caller) {
        //finish();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onBtnEnter_Clicked(View caller) {
        EditText loginUsername = (EditText) findViewById(R.id.etUsernameLogin);
        EditText loginPassword = (EditText) findViewById(R.id.etPasswordLogin);

        String username = loginUsername.getText().toString().toLowerCase(Locale.ROOT);
        String pass = loginPassword.getText().toString();

        requestQueue = Volley.newRequestQueue( this );
        String requestURL = baseURL + "login" + "/" + username;

        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,
                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        JSONObject curObject = responseArray.getJSONObject( 0 );
                        String responseString = curObject.getString( "hash" );

                        String hash = get_SHA_1_SecurePassword(pass);

                        if(hash.equals(responseString)){
                            //Login success
                            //Save the username and the hash in the credentials
                            updateCredentials(username);
                            //responseString = curObject.getString( "username" );
                            loginError.setVisibility(View.INVISIBLE);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            loginError.setText(R.string.login_error);
                            loginError.setVisibility(View.VISIBLE);
                        }
                    }
                    catch(JSONException | IOException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                        loginError.setText(R.string.login_error);
                        loginError.setVisibility(View.VISIBLE);
                    }
                },

                error -> {
                    loginError.setText(getString(R.string.error_database));
                    loginError.setVisibility(View.VISIBLE);
                }
        );

        requestQueue.add(submitRequest);
    }

    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static String get_SHA_1_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
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

    private void updateCredentials(String username) throws IOException {
        String s = getFilesDir() + "/" + "credentials";
        //https://stackoverflow.com/questions/33638765/how-to-read-json-data-from-txt-file-in-java
        BufferedReader reader = new BufferedReader(new FileReader(s));
        String json = "";
        json = getJSONString(reader);

        try {
            JSONObject object = new JSONObject(json);
            JSONArray session = object.getJSONArray("session");
            session.getJSONObject(0).put("username",username);
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

    public void onClick_Back(View caller) {
        finish();
    }
}