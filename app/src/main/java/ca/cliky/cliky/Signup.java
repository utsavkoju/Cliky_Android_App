package ca.cliky.cliky;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import ca.cliky.cliky.essentials.Constant;
import ca.cliky.cliky.services.HTTPClient;
import ca.cliky.cliky.services.Response;

public class Signup extends Activity {

    EditText input_username, input_firstName, input_lastName, input_email, input_password, input_confirmpassword, input_phoneNo, input_street, input_city, input_zipcode;
    Spinner spin_country;
    ScrollView scrollView;
    ProgressBar progressBar;
    Button email_login, email_sign_up;
    private static final String TAG="Signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        input_username = (EditText) findViewById(R.id.username);
        input_firstName = (EditText) findViewById(R.id.firstName);
        input_lastName = (EditText) findViewById(R.id.lastName);
        input_email = (EditText) findViewById(R.id.email);
        input_password = (EditText) findViewById(R.id.password);
        input_confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        input_phoneNo = (EditText) findViewById(R.id.phoneNo);
        input_street = (EditText) findViewById(R.id.street);
        input_city = (EditText) findViewById(R.id.city);
        input_zipcode = (EditText) findViewById(R.id.zipcode);
        spin_country = (Spinner) findViewById(R.id.country);

        scrollView = (ScrollView) findViewById(R.id.sign_up_form);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);

        email_login = (Button) findViewById(R.id.email_login);
        email_sign_up = (Button) findViewById(R.id.email_sign_up);

        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signup.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        email_sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username, firstname, lastname, email, password, confirmpassword, phoneno, street, city, zipcode, country;
                username = input_username.getText().toString();
                firstname = input_firstName.getText().toString();
                lastname = input_lastName.getText().toString();
                email = input_email.getText().toString();
                password = input_password.getText().toString();
                confirmpassword = input_confirmpassword.getText().toString();
                phoneno = input_phoneNo.getText().toString();
                street = input_street.getText().toString();
                city = input_city.getText().toString();
                zipcode = input_zipcode.getText().toString();
                country = spin_country.getSelectedItem().toString();
                if(match_password(password, confirmpassword)){
                    if(check_required_fields(username, firstname, lastname, email, zipcode, country)){
                        String jsonString = "{\"name\":\""+username+"\",\"first_name\":\""+firstname+"\",\"last_name\":\""+lastname+"\",\"email\":\""+email+"\",\"password\":\""+password+"\",\"phone\":\""+phoneno+"\",\"street_address\":\""+street+"\",\"city\":\""+city+"\",\"zip_code\":\""+zipcode+"\",\"country\":\""+country+"\"}";
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            HTTPClient httpClient = new HTTPClient();
                            String server = Constant.API_REGISTER;
                            Response status = httpClient.basic_post(server, jsonObject.toString());
                            if(status.getBody().equals("success")){
                                Intent i = new Intent(Signup.this, Activation.class);
                                startActivity(i);
                                finish();
                            } else {
                                Snackbar.make(scrollView, R.string.error_creating_account, Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        input_username.setError("Username is required");
                        input_firstName.setError("First name is required");
                        input_lastName.setError("Last Name is required");
                        input_email.setError("Email is required");
                        input_zipcode.setError("ZIP Code is required");
                        Snackbar.make(scrollView, R.string.validation_error,Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    input_password.setError("Password doesn't match");
                    Snackbar.make(scrollView, R.string.validation_error,Snackbar.LENGTH_LONG).show();
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

    private boolean check_required_fields(String username, String firstName, String lastName, String email, String zipCode, String country) {
        if(!username.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !zipCode.isEmpty() && !country.isEmpty())
            return true;
        else
            return false;
    }
}
