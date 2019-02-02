package za.co.reverside.empty_bed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

import za.co.reverside.empty_bed.config.IP_Address;

public class Register extends AppCompatActivity
{
    public static final String PREFS = "prefFile";
    private Button registerButton;
    private SharedPreferences sharedPreferences;
    private final String registerURL =  IP_Address.getIp() + "api/members";
    
    ImageView sback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sback = (ImageView)findViewById(R.id.sback);
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Register.this, Main.class);
                startActivity(it);

            }
        });

        customfonts.MyTextView registerButton = findViewById(R.id.registerButton);
        final EditText firstName = findViewById(R.id.firstName);
        final EditText lastName = findViewById(R.id.lastName);
        final EditText mobile = findViewById(R.id.mobile);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegistration(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        mobile.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                );
            }
        });

    }

    public void doRegistration(String firstName, String lastname, String mobile,
                               String email, String password) {

        final HashMap postData = new HashMap();
        postData.put("firstname", firstName);
        postData.put("lastname", lastname);
        postData.put("mobile", mobile);
        postData.put("email", email);
        postData.put("password", password);

        PostResponseAsyncTask registerTask = new PostResponseAsyncTask(Register.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                System.out.println(result);

                if(!result.isEmpty()){
                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }

                //Open OTP page
                Intent intent = new Intent(Register.this, OTPActivity.class);
                startActivity(intent);
            }



        });

        registerTask.execute(registerURL);
    }
}
