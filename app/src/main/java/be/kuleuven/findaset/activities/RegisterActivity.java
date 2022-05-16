package be.kuleuven.findaset.activities;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import be.kuleuven.findaset.R;

public class RegisterActivity extends AppCompatActivity {
    private TextView usernameRegister;
    private RequestQueue requestQueue;
    private String baseURL = "https://studev.groept.be/api/a21pt113/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onBtnRegister_Clicked(View caller) throws NoSuchAlgorithmException {
        EditText registeredUsername = (EditText) findViewById(R.id.editTextTextPersonNameRegister);
        EditText registeredPassword = (EditText) findViewById(R.id.editTextTextPasswordRegister);
        EditText registeredConfirmPassword = (EditText) findViewById(R.id.editTextTextPasswordConfirm);

        String username = registeredUsername.getText().toString();
        String pass = registeredPassword.getText().toString();
        String passConfirm = registeredPassword.getText().toString();

        if(pass.equals(passConfirm)){
            String hash = get_SHA_1_SecurePassword(pass);
            String requestURL = baseURL + "register" + "/" + username + "/" + hash;

            requestQueue = Volley.newRequestQueue( this );

            StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                    response -> {
                        try {
                            JSONArray responseArray = new JSONArray(response);
                            String responseString = "";
                            for( int i = 0; i < responseArray.length(); i++ )
                            {
                                JSONObject curObject = responseArray.getJSONObject( i );
                                Log.e( "Database", responseString);
                            }

                        }
                        catch( JSONException e )
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
}