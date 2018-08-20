package ca.cliky.cliky.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import ca.cliky.cliky.ChangepasswordActivity;
import ca.cliky.cliky.R;
import ca.cliky.cliky.database.Users;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView name, email, phoneNo, subscriptionType, subscriptionDate;
    Button changePassword;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_account, container, false);
        name = (TextView)rootview.findViewById(R.id.username);
        email = (TextView) rootview.findViewById(R.id.email);
        phoneNo = (TextView) rootview.findViewById(R.id.phoneNo);
        subscriptionDate = (TextView) rootview.findViewById(R.id.subscriptionDate);
        subscriptionType = (TextView) rootview.findViewById(R.id.subscriptionType);
        changePassword = (Button) rootview.findViewById(R.id.change_password);

        Users users = new Users(getActivity().getApplicationContext());
        HashMap<String, String> user = users.getUserDetails();

        name.setText(user.get("firstName")+" "+user.get("lastName"));
        email.setText(user.get("email"));
        phoneNo.setText(user.get("phone"));

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ChangepasswordActivity.class);
                startActivity(i);
            }
        });

        return rootview;
    }

}
