package ca.cliky.cliky;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ca.cliky.cliky.database.Users;

public class ChangepasswordActivity extends AppCompatActivity {

    EditText user_name, input_password, input_confirmpassword;
    Button passwordChange, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        user_name = (EditText) findViewById(R.id.user);
        input_password = (EditText) findViewById(R.id.password);
        input_confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        passwordChange = (Button) findViewById(R.id.password_change);
        cancel = (Button) findViewById(R.id.cancel);

        Users users = new Users(getApplicationContext());
        HashMap<String, String> user = users.getUserDetails();

        user_name.setText(user.get("firstName")+" "+user.get("lastName"));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChangepasswordActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String password, confirmPassword;
                password = input_password.getText().toString();
                confirmPassword = input_confirmpassword.getText().toString();
                if(match_password(password, confirmPassword)){
                    String jsonString = "{\"password\":\""+password+"\"}";
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                    } catch (JSONException e) { e.printStackTrace(); }
                }
            }
        });
    }

    private boolean match_password(String password, String passwordconfirm) {
        if(!password.isEmpty() && !passwordconfirm.isEmpty() && password.equals(passwordconfirm))
            return true;
        else
            return false;
    }
}
