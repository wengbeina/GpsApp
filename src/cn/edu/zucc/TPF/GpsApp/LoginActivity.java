package cn.edu.zucc.TPF.GpsApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.edu.zucc.TPF.Bean.UserBean;

public class LoginActivity extends Activity {

    private EditText idEdit;
    private EditText pwdEdit;
    private Button loginBtn;
    private Button registBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idEdit = (EditText) findViewById(R.id.userid);
        pwdEdit = (EditText) findViewById(R.id.userpwd);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registBtn = (Button) findViewById(R.id.registBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                UserBean user = new UserBean();
                user.setUserid(idEdit.getText().toString());
                user.setUserpwd(pwdEdit.getText().toString());
                LoginDeal loginDeal = new LoginDeal(LoginActivity.this, user);
                loginDeal.login();

                /*Intent intent = new Intent(LoginActivity.this, GpsActivity.class);;
				
				Bundle mBundle =  new Bundle();
				mBundle.putSerializable("USER", user);
				intent.putExtras(mBundle);
				startActivity(intent);*/
            }
        });

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private void reset() {
        idEdit.setText("");
        pwdEdit.setText("");
    }

}
