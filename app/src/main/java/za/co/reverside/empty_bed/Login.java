package za.co.reverside.empty_bed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import java.io.IOException;
import java.util.HashMap;
import za.co.reverside.empty_bed.config.IP_Address;
import za.co.reverside.empty_bed.domain.Account;

public class Login extends AppCompatActivity {

    public static final String PREFS = "prefFile";
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private final String loginURL =  IP_Address.getIp() + "api/login";
    private SharedPreferences preferences;

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

        customfonts.MyTextView loginButton =  findViewById(R.id.loginButton);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().length() == 0 || password.getText().length() == 0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    dialog.setMessage("Login Button Clicked")
                            .setCancelable(false)
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog box = dialog.create();
                    box.setTitle("Login Message");
                    box.show();
                }
                else{
                    System.out.println(username.getText().length()+"");
                    doLogin(username.getText().toString(), password.getText().toString());
                }

            }
        });

    }

    public void doLogin(String username, String password){

        final HashMap postData = new HashMap();
        postData.put("username", username);
        postData.put("password", password);

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(Login.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String result) {

                if(result.isEmpty())
                    Toast.makeText(Login.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                else{

                    Account account = new Account();
                    try {
                        account = new ObjectMapper().readValue(result, Account.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    preferences = getSharedPreferences(PREFS, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", account.getName());
                    editor.putString("token", account.getToken());
                    editor.commit();

                    //Open search page
                    Intent intent = new Intent(Login.this, SearchActivity.class);
                    startActivity(intent);
                }
            }
        });

        loginTask.execute(loginURL);

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

    }
}







