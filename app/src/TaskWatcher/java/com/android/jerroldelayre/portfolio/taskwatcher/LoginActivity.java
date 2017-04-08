package com.android.jerroldelayre.portfolio.taskwatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.jerroldelayre.portfolio.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edittext_username)
    EditText mEditTextUsername;
    @BindView(R.id.edittext_password)
    EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onLogin(View view) {
        if(mEditTextUsername.getText().toString().equals("admin") && mEditTextPassword.getText().toString().equals("admin")) {
            //TODO: Login
            startActivity(new Intent(this, ProjectActivity.class));
        }
    }
}
