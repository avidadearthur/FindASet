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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.findaset.R;

public class RegisterLoginActivity extends AppCompatActivity {
    private TextView usernameRegister;
    private RequestQueue requestQueue;
    private String baseURL = "https://studev.groept.be/api/a21pt113/";

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

        requestQueue = Volley.newRequestQueue( this );
        String requestURL = baseURL + "login";

        Intent intent = new Intent(this, MainActivity.class);

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONArray responseArray = new JSONArray(response);
                            String responseString = "";
                            for( int i = 0; i < responseArray.length(); i++ )
                            {
                                JSONObject curObject = responseArray.getJSONObject( i );
                                responseString += curObject.getString( "username" );
                            }

                            intent.putExtra("LoginInfo",responseString);
                            startActivity(intent);
                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        intent.putExtra("LoginInfo",error.getLocalizedMessage());
                        startActivity(intent);
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void onBtnRegister_Clicked(View caller) {
        EditText registeredUsername = (EditText) findViewById(R.id.editTextTextPersonNameRegister);
        EditText registeredPassword = (EditText) findViewById(R.id.editTextTextPasswordRegister);
        EditText registeredConfirmPassword = (EditText) findViewById(R.id.editTextTextPasswordConfirm);


        // Finally, start new activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}