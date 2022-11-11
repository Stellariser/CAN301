package com.example.can301;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
// Key package for connection！！！
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

import androidx.appcompat.app.AppCompatActivity;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OKUT;
import com.example.can301.net.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    // Computational Intelligence Vision and Security Lab
    private Button lBtn;
    private EditText inputEmail;
    private EditText inputPassword;
    private String status = String.valueOf('a');
    // notifications
    private String success = "Login success.";
    private String fail = "Login failed";
    private String l2durl = "file:///android_asset/www/l2d.html";
    private TextView rBtn,title;
    private WebView mWebview;
    private boolean loginFlag;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find element
        lBtn = findViewById(R.id.btn_login);
        rBtn = findViewById(R.id.regLink);
        mWebview = findViewById(R.id.l2d);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        title = findViewById(R.id.caption1);
        loginFlag = false;

        //登录flag，需要后端控制user登陆状态，存一个token
        if(loginFlag){
            // 直接跳转
           jumpToMain();
        }
        this.loadL2d(l2durl);

        lBtn.setOnClickListener(this::onClick);
        rBtn.setOnClickListener(this::onClickRegisterLink);
    }


    private void loadL2d(String url){
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        //mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.loadUrl(url);
    }


    private void onClick(View view){
        String username = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
//        new Thread(){
//            @Override
//            public void run(){
//                try {
//                    String a = OKUT.getInstance().doGet("https://mock.apifox.cn/m1/1900048-0-default/user/login?username=admin&password=123");
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println(a);
//                            status = a;
//                            jumpToMain();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }.start();





//        OkHttpUtils.getSoleInstance().doGet("https://mock.apifox.cn/m1/1900048-0-default/user/login", new NetAgent() {
//            @Override
//            public void onSuccess(String result) {
//                System.out.println(result);
//                status = result;
//                title.setText(result);
////                jumpToMain();
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                e.printStackTrace();
//            }
//        }, this);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("username", "admin");
        hashMap.put("password","123");
        OkHttpUtils.getSoleInstance().doPostForm("http://10.0.2.2:4523/m1/1900048-0-default/user/login", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG);

                title.setText(result);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        },hashMap,this);

    }

    private void onClickRegisterLink(View view){
        jumpToRegister();
    }

    //跳转方法
    private void jumpToMain(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(MainActivity.this, FunctionalActivity.class);
        startActivity(intent);
        // finish login activity, 这样回退就不会再返回到login
        MainActivity.this.finish();
    }

    private void jumpToRegister(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(MainActivity.this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // finish login activity, 这样回退就不会再返回到login
    }



    private static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][w.-]*[a-zA-Z0-9]@[a-zA-Z0-9][w.-]*[a-zA-Z0-9].[a-zA-Z][a-zA-Z.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}