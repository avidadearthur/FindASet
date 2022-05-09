package be.kuleuven.findaset.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import be.kuleuven.findaset.R;

public class RegisterLoginActivity extends AppCompatActivity {
    private TextView usernameRegister;
    private RequestQueue requestQueue;
    private static final String SUBMIT_URL = "https://studev.groept.be/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
    }

    public void onBtnGuest_Clicked(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBtnLogin_Clicked(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBtnRegister_Clicked(View caller) {
        EditText registeredUsername = (EditText) findViewById(R.id.editTextTextPersonNameRegister);
        EditText registeredPassword = (EditText) findViewById(R.id.editTextTextPasswordRegister);
        EditText registeredConfirmPassword = (EditText) findViewById(R.id.editTextTextPasswordConfirm);

        //check if username already exists
        requestQueue = Volley.newRequestQueue(this);
        String requestURL = SUBMIT_URL  + "checkUsername" + "/" + registeredUsername;

        System.out.println(requestURL);

        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,SUBMIT_URL,null, response -> {
        }, error -> Toast.makeText(RegisterLoginActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL, response -> {
            Toast.makeText(RegisterLoginActivity.this, "Valid Username", Toast.LENGTH_SHORT).show();
            requestQueue.add(queueRequest);
        }, error -> Toast.makeText(RegisterLoginActivity.this, "Username already exists", Toast.LENGTH_LONG).show());

        System.out.println(submitRequest);

        requestQueue.add(submitRequest);

        // Finally, start new activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}