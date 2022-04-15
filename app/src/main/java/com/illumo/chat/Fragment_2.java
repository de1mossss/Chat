package com.illumo.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_2 extends Fragment {

    private String token;

    public Fragment_2()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);

        token = MainActivity.result.getToken();


        GetCurrentUser(view);

        return view;
    }

    void GetCurrentUser(View view)
    {
        Call<User> call = RetrofitClient.getInstance().getMyApi().executeGetUserID("token=" + token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200)
                {
                    User user = response.body();

                    TextView idText = (TextView)view.findViewById(R.id.id);
                    TextView usernameText = (TextView)view.findViewById(R.id.username);
                    TextView createAtText = (TextView)view.findViewById(R.id.createAt);

                    idText.setText(user.getId());
                    usernameText.setText(user.getNickname());

                    String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                    String newformat = "d MMMM yyyy HH:mm";
                    SimpleDateFormat dataFormat = new SimpleDateFormat(format);

                    String dataString = user.getCreatedAt();

                    try {
                        Date date = dataFormat.parse(dataString);
                        dataFormat.applyPattern(newformat);
                        String newdataString = dataFormat.format(date);
                        createAtText.setText(newdataString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



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
