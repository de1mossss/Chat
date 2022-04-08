package com.illumo.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView errorLog;
    private static User result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorLog = findViewById(R.id.errorlog);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginDialog();
            }
        });
        
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupDialog();
            }
        });
    }

    private void handleLoginDialog() {

        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).show();

        Button loginBtn = view.findViewById(R.id.login);
        EditText nameEdit = view.findViewById(R.id.emailEdit);
        EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("nickname", nameEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                //

                Call<User> call = RetrofitClient.getInstance().getMyApi().executeLogin(map);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if(response.isSuccessful())
                        {
                            result = response.body();

                            GetAuthStatus(result.getToken());
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Wrong password or username", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

    }

    private void handleSignupDialog() {

        View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button signupBtn = view.findViewById(R.id.signup);
        EditText nameEdit = view.findViewById(R.id.nameEdit);
        EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("nickname", nameEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                //
                Call<Void> call = RetrofitClient.getInstance().getMyApi().executeSignUp(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Toast.makeText(MainActivity.this, "Signed up successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(MainActivity.this, "Already registered", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }

    public void GetUserById()
    {
        Call<User> call = RetrofitClient.getInstance().getMyApi().executeGetUserID("token=" + result.getToken(), "NnXYMMk4VFbbFXEEebWSt");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200)
                {
                    User user = response.body();

                }
                else if(response.code() == 401)
                {
                    Toast.makeText(MainActivity.this, "Failed to get userID", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void GetAuthStatus(String token)
    {
        Call<Void> call = RetrofitClient.getInstance().getMyApi().executeAuthCheck("token=" + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, ChatMain.class);
                    intent.putExtra("token", result.getToken());
                    startActivity(intent);
                }else if(response.code() == 401)
                {
                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}