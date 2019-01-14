package com.yogo.pjh.weather_projcect_v10;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterSocialLoginActivity extends AppCompatActivity {

    private String userID;
    private String userPassword;
    private String userCheckPassword;
    private String userName;
    private String userEmail;
    private String userGender;
    private int userAge;
    private AlertDialog dialog;
    private boolean validate = false;
    private String usertpid;
    private String userSAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_social_login);

        Intent intent=getIntent();
        userID = intent.getStringExtra("userID");

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText ageText = (EditText) findViewById(R.id.ageText);
        final EditText emailText=(EditText) findViewById(R.id.emailText);
        final CheckBox userPolicyCheckBox=(CheckBox)findViewById(R.id.usePolicyCheckBox);
        final CheckBox userPolicyCheckBox2=(CheckBox)findViewById(R.id.usePolicyCheckBox2);

        RadioGroup genderGroup =(RadioGroup) findViewById(R.id.genderGroup);
        int genderGroupID =genderGroup.getCheckedRadioButtonId();
        userGender = "";


        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });


        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName=nameText.getText().toString().trim();
                userEmail= emailText.getText().toString().trim();
                userSAge = ageText.getText().toString();

                if (userID.equals("")  ||userName.equals("") || userSAge.equals("") || userGender.equals("") || userEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                    dialog = builder.setMessage("빈칸을 채워주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }


                if(!checkEmail(userEmail)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                    dialog = builder.setMessage("이메일 양식에 맞춰주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(!userPolicyCheckBox.isChecked()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                    dialog = builder.setMessage("약관 동의를 하지 않으셨습니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(!userPolicyCheckBox2.isChecked()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                    dialog = builder.setMessage("약관 동의를 하지 않으셨습니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }



                Response.Listener<String> responseEmailListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                //their no ID in DB


                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                                                dialog = builder.setMessage("회원 가입 성공")
                                                        .setPositiveButton("확인", null)
                                                        .create();
                                                dialog.show();
                                                finish();
                                            }
                                            else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                                                dialog = builder.setMessage("회원 가입 실패")
                                                        .setNegativeButton("확인", null)
                                                        .create();
                                                dialog.show();
                                            }
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                int userAge = Integer.parseInt(userSAge);
                                RegisterRequest registerRequest = new RegisterRequest(userID, "0000", userName, userAge, userGender, userEmail, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(RegisterSocialLoginActivity.this);
                                queue.add(registerRequest);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSocialLoginActivity.this);
                                dialog = builder.setMessage("존재하는 이메일 입니다")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest(userEmail, responseEmailListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterSocialLoginActivity.this);
                queue.add(validateEmailRequest);

            }
        });
    }

    /**
     * 이메일 포맷 체크
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }

    protected void onStop(){
        super.onStop();
        if(dialog!=null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }

}




