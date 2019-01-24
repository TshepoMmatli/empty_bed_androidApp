package za.co.reverside.empty_bed;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import za.co.reverside.empty_bed.models.LoginResponse;

public class Login extends AppCompatActivity {

    public static final String PREFS = "prefFile";
    private RequestQueue requestQueue;
    private View view;
    private Button loginButton;
    private SharedPreferences sharedPreferences;

    ImageView sback;
    EditText usernameImgView, passwordImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sback = (ImageView)findViewById(R.id.signingin);
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Login.this, Main.class);
                startActivity(it);
            }
        });


        this.initializeGadgets();
        this.loginButton.setOnClickListener(doLogin());
        this.requestQueue = Volley.newRequestQueue(view.getContext());
    }

    private void initializeGadgets(){

        this.usernameImgView = view.findViewById(R.id.username);
        this.passwordImgView = view.findViewById(R.id.password);
        this.loginButton = view.findViewById(R.id.loginButton);
    }

    private View.OnClickListener doLogin(){
        View.OnClickListener accessApp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginLogic();


            }
        };
        return accessApp;
    }

    private void loginLogic(){

        String username = usernameImgView.getText().toString();
        String password = passwordImgView.getText().toString();
        String url = "http://45.79.13.122:8080/swagger-ui.html#!/account45service/loginUsingPOST";
        final Gson gson = new Gson();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        JSONObject loginObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                Request.Method.POST,
                url,
                loginObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        LoginResponse loginResponse = gson.fromJson(String.valueOf(response), LoginResponse.class);
                        Log.e("Login response :", loginResponse.getUser().getUserName() + " has " + loginResponse.getMessageResponse());

                        if(loginResponse.getUser().getUserName() != null){

                            sharedPreferences = getActivity().getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user-logged-in", gson.toJson(loginResponse.getUser()));
                            editor.apply();

                            customerID = loginResponse.getUser().getUserID();
                            driverID = loginResponse.getUser().getUserID();
                            supplierID = loginResponse.getUser().getUserID();
                            String userRole = loginResponse.getUser().getRoles().get(0).getName();
                            redirectBasedOnRole(userRole);



                        }
                        else if(loginResponse.getUser().getUserName() == null){

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                    .setTitle("Login response")
                                    .setMessage(loginResponse.getMessageResponse() + "\n" + "Forgotten password?")
                                    .setPositiveButton("Recover password", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            startActivity(new Intent(getContext(), RecoverPasswordActivity.class));
                                        }
                                    })
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put(ApplicationHeaders.HEADERS, ApplicationHeaders.HEADERSVALUE);

                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}







