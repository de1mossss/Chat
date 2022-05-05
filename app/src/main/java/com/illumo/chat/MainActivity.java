package com.illumo.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static User result;

    public static FrameLayout container;
    public static FragmentManager myFragmentManager;

    public static LoginFragment loginFragment;
    public static SignUpFragment signUpFragment;

    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";

    public static class LoginFragment extends Fragment{

        Button loginBtn;
        EditText nameEdit;
        EditText passwordEdit;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.login_dialog, null);

            loginBtn = view.findViewById(R.id.login);
            nameEdit = view.findViewById(R.id.emailEdit);
            passwordEdit = view.findViewById(R.id.passwordEdit);

            view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleLoginDialog(view);

                }
            });

            view.findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SwitchToSignUp();
                }
            });

            return view;
        }

        private void handleLoginDialog(View view) {

//            View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
//            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//
//            builder.setView(view).show();

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

                                GetAuthStatus(view, result.getToken());
                                GetCurrentUser(view);
                            }
                            else
                            {
                                Toast.makeText(view.getContext(), "Wrong password or username", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                            Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            });

        }


        public void GetAuthStatus(View view, String token)
        {
            Call<Void> call = RetrofitClient.getInstance().getMyApi().executeAuthCheck("token=" + token);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 200){
                        Toast.makeText(view.getContext(), "Ok", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(view.getContext(), ChatMain.class);
                        intent.putExtra("token", result.getToken());
                        startActivity(intent);
                    }else if(response.code() == 401)
                    {
                        Toast.makeText(view.getContext(), "Fail", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
        void GetCurrentUser(View view)
        {
            Call<User> call = RetrofitClient.getInstance().getMyApi().executeGetUserID("token=" + result.getToken());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200)
                    {
                        User user = response.body();
                        user.setToken(result.getToken());
                        result = user;

                    }
                    else if(response.code() == 401)
                    {
                        //Toast.makeText(this, "Failed to get userID", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    public static class SignUpFragment extends Fragment{

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.signup_dialog, null);

            view.findViewById(R.id.signup1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleSignupDialog();
                }
            });

            view.findViewById(R.id.login1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SwitchToLogin();
                }
            });

            return view;
        }
        private void handleSignupDialog() {

            View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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
                                Toast.makeText(view.getContext(), "Signed up successfully", Toast.LENGTH_LONG).show();
                                SwitchToLogin();
                            } else if (response.code() == 400) {
                                Toast.makeText(view.getContext(), "Already registered", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout) findViewById(R.id.container);

        myFragmentManager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        signUpFragment = new SignUpFragment();

        if (savedInstanceState == null) {
            // при первом запуске программы
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            // добавляем в контейнер при помощи метода add()
            fragmentTransaction.add(R.id.container, loginFragment, TAG_1);
            fragmentTransaction.commit();

        }
    }

    public static void SwitchToLogin()
    {
        LoginFragment fragment = (LoginFragment) myFragmentManager.findFragmentByTag(TAG_1);

        if(fragment == null)
        {
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, loginFragment, TAG_1);
            fragmentTransaction.commit();
        }
    }

    public static void SwitchToSignUp()
    {
        SignUpFragment fragment = (SignUpFragment) myFragmentManager.findFragmentByTag(TAG_2);

        if(fragment == null)
        {
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, signUpFragment, TAG_2);
            fragmentTransaction.commit();
        }
    }

}