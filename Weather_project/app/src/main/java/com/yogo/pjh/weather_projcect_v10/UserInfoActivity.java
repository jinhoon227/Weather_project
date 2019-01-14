package com.yogo.pjh.weather_projcect_v10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;


public class UserInfoActivity extends AppCompatActivity {

    private String userID;
    private String userPassword;
    private String userName;
    private int userAge;
    private String userGender;
    private String userEmail;
    private  Boolean loginChecked;
    private Boolean userShowCloset; //add7.22


    @Override
    protected void onPause() {
        super.onPause();

        Log.d("아니왜3",userShowCloset.toString());
        SharedPreferences settings=getSharedPreferences("settings",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();

        String uSC = "false";
        if (userShowCloset) {
            uSC = "true";
        } else {
            uSC = "false";
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ShowUserClosetRequest showUserClosetRequest = new ShowUserClosetRequest(userID, uSC, responseListener); //LoginRequest에 객체를 만들고
        RequestQueue queue = Volley.newRequestQueue(UserInfoActivity.this);
        queue.add(showUserClosetRequest);


        editor.putBoolean("userShowCloset", userShowCloset);
        editor.commit();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        final EditText showUserName = (EditText) findViewById(R.id.showUserName);
        final EditText showUserAge = (EditText) findViewById(R.id.showUserAge);
        final EditText showUserGender = (EditText) findViewById(R.id.showUserGender);
        final EditText showUserEmail = (EditText) findViewById(R.id.showUserEmail);
        final Button logoutButton=(Button)findViewById(R.id.logoutButton);
        final ToggleButton showUserClosetToggle=(ToggleButton)findViewById(R.id.showUserClosetToggle); //add7.22

        Intent intent = getIntent();
        userID=intent.getStringExtra("userID");
        userPassword=intent.getStringExtra("userPassword");
        userName=intent.getStringExtra("userName");
        userAge=intent.getIntExtra("userAge",-1);
        userGender=intent.getStringExtra("userGender");

        //데이터 가져오기
        SharedPreferences settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        loginChecked = settings.getBoolean("loginChecked", false);
        if (loginChecked) {
            userID = settings.getString("userID", "");
            userPassword = settings.getString("userPassword", "");
            userName = settings.getString("userName", "");
            userAge = settings.getInt("userAge", -1);
            userGender = settings.getString("userGender", "");
            userEmail = settings.getString("userEmail", "");
            userShowCloset=settings.getBoolean("userShowCloset",false); //add7.22
            showUserClosetToggle.setChecked(userShowCloset);

            if(userShowCloset)
                showUserClosetToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_on1));
        }


        showUserClosetToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showUserClosetToggle.isChecked()) {
                    showUserClosetToggle.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.toggle_on1)
                    );
                    userShowCloset=true;
                } else {
                    showUserClosetToggle.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.toggle_off1)
                    );
                    userShowCloset=false;
                }

            }
        });


        showUserName.setText(userName);
        showUserName.setEnabled(false);
        showUserName.setBackgroundColor(getResources().getColor(R.color.colorGrayBright));

        showUserAge.setText(userAge+"");
        showUserAge.setEnabled(false);
        showUserAge.setBackgroundColor(getResources().getColor(R.color.colorGrayBright));

        showUserGender.setText(userGender);
        showUserGender.setEnabled(false);
        showUserGender.setBackgroundColor(getResources().getColor(R.color.colorGrayBright));

        showUserEmail.setText(userEmail);
        showUserEmail.setEnabled(false);
        showUserEmail.setBackgroundColor(getResources().getColor(R.color.colorGrayBright));





        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(UserInfoActivity.this, MainActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(loginIntent);

                //스태틱 리스트초기화
                TodayRecommendCodi.recommand_clothes.clear();

                //자동로그인체크 해제시 폰에 저장된 정보 모두 삭제
                SharedPreferences settings=getSharedPreferences("settings",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=settings.edit();
                editor.clear();
                editor.commit();

                //카카오톡 로그아웃
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                    }
                });

            }
        });
    }
}
