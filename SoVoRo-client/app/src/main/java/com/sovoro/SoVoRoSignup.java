package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sovoro.utils.AppHelper;

import java.util.HashMap;
import java.util.Map;

// 회원가입 액티비티
public class SoVoRoSignup extends AppCompatActivity {

    enum SignupOption {
        NICKNAMEDUPLICATION,
        SMSSEND,
        SMSCHECK,
        SIGNUP
    }

    // 입력 레이아웃 내부
    private EditText sovoroSignUpId;
    private EditText sovoroSignUpPassword;
    private EditText sovoroSignUpNickName;
    private EditText sovoroSignUpSmsInput;
    private EditText sovoroSignUpSmsConfirm;

    // 비밀번호 불일치, 닉네임 중복, SMS값 불일치 에러 메세지 출력
    private TextView sovoroPasswordErrorMsg;

    // 닉네임 중복 확인
    private Button sovoroNicknameDuplicationCheck;
    // SMS발송 버튼
    private Button sovoroSignUpSmsSendButton;
    // SMS 인증번호 확인 버튼
    private Button sovoroSignUpSmsConfirmButton;
    // 회원가입 버튼
    private Button sovoroSignUp;

    // POST메소드 경로
    private final String SIGNUP_PATH="/signup";
    private final String NICKNAME_CHECK="/nickname-check";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_signup);

        // RequestQueue의 context를 signup activity에 맞춘다
        // 요청을 보낼 리퀘스트 큐가 어느 액티비티에서 요청을 받있는지 구분
        AppHelper.setRequestQueue(this);

        // AppHelper의 getURL메소드를 통해 post요청을 보낼 url 생성
        // PATH는 /signup
        // final String URL=AppHelper.getURL(SIGNUP_PATH);

        // xml레이아웃과 자바 코드의 연결

        // 입력 레이아웃 내부
        sovoroSignUpId=findViewById(R.id.sovoro_signup_id);
        sovoroSignUpPassword=findViewById(R.id.sovoro_signup_password);
        sovoroSignUpNickName=findViewById(R.id.sovoro_signup_nickname);
        sovoroSignUpSmsInput=findViewById(R.id.sovoro_signup_sms_send_input);
        sovoroSignUpSmsConfirm=findViewById(R.id.sovoro_sms_confirm_input);

        // 비밀번호 불일치, 닉네임 중복, SMS값 불일치 에러 메세지 출력
        sovoroPasswordErrorMsg=findViewById(R.id.sovoro_signup_err_text);

        // 닉네임 중복 확인 버튼
        sovoroNicknameDuplicationCheck=findViewById(R.id.sovoro_signup_nickname_duplication_check);
        // SMS발송 버튼
        sovoroSignUpSmsSendButton=findViewById(R.id.sovoro_signup_sms_send);
        // SMS 인증번호 확인 버튼
        sovoroSignUpSmsConfirmButton=findViewById(R.id.sovoro_signup_sms_confirm);
        // 회원가입 버튼
        sovoroSignUp=findViewById(R.id.sovoro_signup);

        /**클릭 이벤트 처리**/
        // 코드 간결화를 위해 메소드로 외부화
        // 닉네임 중복 확인
        sovoroNicknameDuplicationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.requestQueueAdd(getSigninRequest(SignupOption.SIGNUP,AppHelper.getURL(NICKNAME_CHECK)),"STRING");
            }
        });

        // SMS전송, SMS인증번호 확인은 소켓으로 수행
        // 굳이 소켓 아니어도 될지도
//        // sms인증
//        // 지금은 구현하지 않음
//        sovoroSignUpSmsSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 추후 네이버 api를 이용한 sms인증 서비스 구현
//                AppHelper.requestQueueAdd(getSigninRequest(SignupOption.SMSSEND),"STRING");
//            }
//        });
//        sovoroSignUpSmsConfirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 추후 네이버 api를 이용한 sms인증 서비스 구현
//                AppHelper.requestQueueAdd(getSigninRequest(SignupOption.SMSCHECK),"STRING");
//            }
//        });

        // 버튼 클릭 시 로그인 버튼 구현
        // 버튼 클릭 완료 시 로그인 완료 메세지와 함께 초기 signin 액티비티로 이동
        sovoroSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 구현해야 할 부분
                AppHelper.requestQueueAdd(getSigninRequest(SignupOption.SIGNUP,AppHelper.getURL(SIGNUP_PATH)),"STRING");
            }
        });
    }

    private StringRequest getSigninRequest(SignupOption signupOption,String URL) {
        // 별명 중복확인, SMS전송, SMS인증번호 확인은 소켓으로 수행
        switch (signupOption) {
            case NICKNAMEDUPLICATION:
                return null;
            case SMSSEND:
                return null;
            case SMSCHECK:
                return null;
            case SIGNUP:
                return new StringRequest(
                        // 리퀘스트 형태
                        // POST 리퀘스트임
                        // 만약 GET(단순 해당 URL의 정보만을 받아옴)의 경우 Request.Method.GET
                        Request.Method.POST,
                        // AppHelper를 통해 만든 request url
                        // http://13.58.48.132:3000/signup
                        URL,
                        // 리스폰스 리스너 콜백 함수
                        new Response.Listener<String>() {
                            // 리스폰스를 String형태로 받아온다
                            @Override
                            public void onResponse(String response) {
                                // switch를 통해 리스폰스 분별
                                // 0일 경우 로그인 계정 생성 후 signin 페이지로 넘어김
                                // -1일 경우 비밀번호 불일치 에러 메세지 송신
                                switch (response) {
                                    case "0": // 인증 성공
                                        // 서버에서 데이터베이스 서버에 정보 저장
                                        // signup페이지로 넘어가는 intent
                                        Intent intent = new Intent(getApplicationContext(), SoVoRoSignin.class);
                                        startActivity(intent);
                                        break;
                                    case "-1":
                                        // 비밀번호 불일치
                                        sovoroPasswordErrorMsg.setText("비밀번호가 일치하지 않아요");
                                        break;
                                }
                            }
                        },
                        // 에러 발생 콜백 함수
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // 에러 발생시 로그창에 에러를 띄운다
                                System.err.println(error.toString());
                            }
                        }) {
                    // POST 리퀘스트에서 override한 메세지 송신 객체 getParams
                    // Map 형태로 정보를 해당 url로 post
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        // 사용자 id와 password를 map에 저장하여 송신한다
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", sovoroSignUpId.getText().toString());
                        params.put("password", sovoroSignUpPassword.getText().toString());
                        params.put("username", sovoroSignUpNickName.getText().toString());
                        return params;
                    }
                };
            default:
                break;
        }
        return null;
    }
}