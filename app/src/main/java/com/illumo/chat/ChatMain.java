package com.illumo.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.illumo.chat.adapter.ChatAdapter;
import com.illumo.chat.adapter.CustomRecyclerViewAdapter;
import com.illumo.chat.adapter.MessageAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMain extends AppCompatActivity {

    private String token;
    private static ViewPager viewPager;
    public static Chat selectedChat;
    public static ChatAdapter chatAdapter;

    public static FrameLayout container;
    public static FragmentManager fragmentManager;

    public static Fragment_1 fragment_1;
    public static Fragment_3 fragment_3;

    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_3";

    public static class Fragment_1 extends Fragment {

        private RecyclerView rw;
        private CustomRecyclerViewAdapter adapter;

        private FloatingActionButton newChatButton;

        public Fragment_1()
        {

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.chat_menu_fragment, container, false);

            initRecyclerView(view);
            String test = MainActivity.result.getId();
            String test2 = MainActivity.result.getToken();
            Call<List<Chat>> call = RetrofitClient.getInstance().getMyApi().executeGetChatByParticipantId("token=" + MainActivity.result.getToken(), MainActivity.result.getId());
            call.enqueue(new Callback<List<Chat>>() {
                @Override
                public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                    if(response.isSuccessful())
                    {

                        List<Chat> chats = new ArrayList<>();

                        chats = response.body();

                        chatAdapter.setItems(chats);
                    }
                }

                @Override
                public void onFailure(Call<List<Chat>> call, Throwable t) {

                }
            });

            view.findViewById(R.id.newChatBut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view_1 = getLayoutInflater().inflate(R.layout.newchat_layout, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(view_1).show();

                    EditText title = view_1.findViewById(R.id.chatTitle);
                    EditText usernames = view_1.findViewById(R.id.participans);
                    Button createChat = view_1.findViewById(R.id.createChatBut);
                    RecyclerView recycler = view_1.findViewById(R.id.recycler_users);

                    recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

                    adapter = new CustomRecyclerViewAdapter();
                    recycler.setAdapter(adapter);

                    usernames.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            //
                            if(usernames.getText().toString() == "") return;
                            Call<List<User>> call = RetrofitClient.getInstance().getMyApi().executeGetUserByNickname("token=" + MainActivity.result.getToken(), usernames.getText().toString());
                            call.enqueue(new Callback<List<User>>() {
                                @Override
                                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                    if(response.isSuccessful())
                                    {
                                        List<User> users = new ArrayList<>();

                                        users = response.body();

                                        adapter.clearItems();
                                        adapter.setItems(users);

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<User>> call, Throwable t) {

                                }
                            });

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    createChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            HashMap<String, Object> map = new HashMap<>();
                            if(adapter.getSelected().size() < 2)
                            {
                                map.put("isDialogue", "true");
                            }
                            ArrayList<String> selectedUserIds = new ArrayList<>();
                            for(User user : adapter.getSelected())
                            {
                                selectedUserIds.add(user.getId());
                            }
                            map.put("title", title.getText().toString());
//                        for(int i = 0; i < selectedUserIds.size(); i++)
//                        {
//                            map.put("participants", selectedUserIds.get(i));
//                        }
                            map.put("participants", selectedUserIds);


                            Call<Chat> call = RetrofitClient.getInstance().getMyApi().executeNewChat("token=" + MainActivity.result.getToken(), map);
                            call.enqueue(new Callback<Chat>() {
                                @Override
                                public void onResponse(Call<Chat> call, Response<Chat> response) {
                                    if(response.isSuccessful())
                                    {
                                        RequestBody reqChat = call.request().body();
                                        Chat chat = response.body();

                                        Toast.makeText(view_1.getContext(), "Chat created", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(view_1.getContext(), "Error creating chat {}", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Chat> call, Throwable t) {

                                    Toast.makeText(view_1.getContext(), "Fail to create chat", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    });


                }
            });

            return view;
        }

        void GetChatCurrentUser()
        {

        }

        private void initRecyclerView(View view)
        {
            rw = view.findViewById(R.id.recyclerview);

            rw.setLayoutManager(new LinearLayoutManager(view.getContext()));

            chatAdapter = new ChatAdapter();
            rw.setAdapter(chatAdapter);
        }

    }

    public static class Fragment_2 extends Fragment {

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

            TextView idText = (TextView)view.findViewById(R.id.id);
            TextView usernameText = (TextView)view.findViewById(R.id.username);
            TextView createAtText = (TextView)view.findViewById(R.id.createAt);

            idText.setText(MainActivity.result.getId());
            usernameText.setText(MainActivity.result.getNickname());

            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            String newformat = "d MMMM yyyy HH:mm";
            SimpleDateFormat dataFormat = new SimpleDateFormat(format);

            String dataString = MainActivity.result.getCreatedAt();

            try {
                Date date = dataFormat.parse(dataString);
                dataFormat.applyPattern(newformat);
                String newdataString = dataFormat.format(date);
                createAtText.setText(newdataString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Fragment_3 extends Fragment{

        private TextView chatTitle;
        private EditText messageField;
        private ImageButton sendMessageButton;
        private RecyclerView recyclerView;
        private MessageAdapter adapter;

        public Fragment_3()
        {

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.chat_message_fragment, container, false);

            chatTitle = view.findViewById(R.id.chatTitle);
            messageField = view.findViewById(R.id.messageField);
            sendMessageButton = view.findViewById(R.id.sendMessageBtn);

            adapter = new MessageAdapter();
            recyclerView = view.findViewById(R.id.recycler_message);

            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(adapter);

            GetAllMessage();

            if(selectedChat != null)
            {
                chatTitle.setText(selectedChat.getTitle());
                Toast.makeText(view.getContext(), selectedChat.getTitle(), Toast.LENGTH_LONG).show();
            }

            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("content", messageField.getText().toString());
                    map.put("chatId", selectedChat.getId());
                    map.put("type", "text");

                    Call<Message> call = RetrofitClient.getInstance().getMyApi().executeSendMessage("token=" + MainActivity.result.getToken(), map);
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful())
                            {
                                Toast.makeText(view.getContext(), "Message sended", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(view.getContext(), "Fail to send message", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {

                        }
                    });

                }
            });


            return view;
        }

        void GetAllMessage()
        {
            Call<List<Message>> call = RetrofitClient.getInstance().getMyApi().executeGetAllMessage("token=" + MainActivity.result.getToken(), ChatMain.selectedChat.getId());
            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if(response.isSuccessful())
                    {
                        List<Message> messages = response.body();

                        adapter.setItems(messages);
                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });
        }

    }

    public static class Fragment_4 extends Fragment{


        public Fragment_4()
        {

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.main_chat_fragment, container, false);

            fragmentManager = getParentFragmentManager();
            fragment_1 = new Fragment_1();
            fragment_3 = new Fragment_3();

            if (savedInstanceState == null) {
                // при первом запуске программы
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // добавляем в контейнер при помощи метода add()
                fragmentTransaction.add(R.id.container, fragment_1, TAG_1);
                fragmentTransaction.commit();

            }

            return view;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        token = getIntent().getStringExtra("token");

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter); // устанавливаем адаптер
        viewPager.setCurrentItem(1); // выводим второй экран


    }

    public static void SetFragment(int i)
    {
        viewPager.setCurrentItem(i);
    }

    public class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
                return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment_2();
                case 1:
                    return new Fragment_4();

                default:
                    return new Fragment_4();
            }
        }

    }

    public static void SwitchToChats()
    {
        MainActivity.LoginFragment fragment = (MainActivity.LoginFragment) fragmentManager.findFragmentByTag(TAG_1);

        if(fragment == null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment_1, TAG_1);
            fragmentTransaction.commit();
        }
    }

    public static void SwitchToMessage()
    {
        MainActivity.SignUpFragment fragment = (MainActivity.SignUpFragment) fragmentManager.findFragmentByTag(TAG_2);

        if(fragment == null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment_3, TAG_2);
            fragmentTransaction.commit();
        }
    }
}