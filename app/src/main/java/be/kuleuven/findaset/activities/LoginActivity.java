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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import be.kuleuven.findaset.R;

public class LoginActivity extends AppCompatActivity {
    private TextView usernameRegister;
    private RequestQueue requestQueue;
    private String baseURL = "https://studev.groept.be/api/a21pt113/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    public void onClick_Join(View caller) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onBtnEnter_Clicked(View caller) {
        EditText loginUsername = (EditText) findViewById(R.id.etUsernameLogin);
        EditText loginPassword = (EditText) findViewById(R.id.etPasswordLogin);

        String username = loginUsername.getText().toString();
        String pass = loginPassword.getText().toString();

        requestQueue = Volley.newRequestQueue( this );
        String requestURL = baseURL + "login" + "/" + username;

        Intent intent = new Intent(this, MainActivity.class);

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
                            updateCredentials(username,hash);
                            responseString = curObject.getString( "username" );
                        }

                        intent.putExtra("LoginInfo",responseString);
                        startActivity(intent);
                    }
                    catch(JSONException | FileNotFoundException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> {
                    JSONException e = null;
                    Log.e( "Database", e.getMessage(), e );
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

    private void updateCredentials(String username, String hash) throws FileNotFoundException {
        JSONObject json = new JSONObject();
        try {
            json.put("loginCredentials", new String[]{username, hash});
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try (PrintWriter out = new PrintWriter(new FileWriter("credentials.txt"))) {
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}