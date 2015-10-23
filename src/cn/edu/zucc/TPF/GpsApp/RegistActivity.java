package cn.edu.zucc.TPF.GpsApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.zucc.TPF.Bean.UserBean;

/**
 * Created by aqi on 15/10/19.
 */
public class RegistActivity extends Activity {
    private EditText mId;
    private EditText mName;
    private EditText mPwd;
    private EditText mGender;
    private EditText mCellphone;
    private EditText mEmail;
    private EditText mCode;
    private Button mOkbtn;
    private Button mResetbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_regist);
        init();
        okbtnListener();
        resetbtnListener();


    }

    private void okbtnListener() {
        mOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("4423".equals(mCode.getText().toString())) {
                    UserBean user = new UserBean();
                    user.setUserid(mId.getText().toString());
                    user.setUsername(mName.getText().toString());
                    user.setUserpwd(mPwd.getText().toString());
                    user.setIsregistered(1);
                    user.setUsertype(0);
                    user.setCellphone(mCellphone.getText().toString());
                    user.setEmail(mEmail.getText().toString());
                    user.setGender(mGender.getText().toString());

                    RegistDeal deal = new RegistDeal(RegistActivity.this, user);
                    deal.register();
                }else {
                    Toast.makeText(RegistActivity.this,"授权码不正确，请核对！",Toast.LENGTH_SHORT).show();
                    mCode.setText("");
                }
            }
        });
    }

    private void resetbtnListener(){
        mResetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    private void init() {
        mId = (EditText) findViewById(R.id.lift_id);
        mName = (EditText) findViewById(R.id.litf_name);
        mPwd = (EditText) findViewById(R.id.lift_pwd);
        mGender = (EditText) findViewById(R.id.gender);
        mCellphone = (EditText) findViewById(R.id.cellphone);
        mEmail = (EditText) findViewById(R.id.email);
        mCode = (EditText) findViewById(R.id.code);
        mOkbtn = (Button) findViewById(R.id.okBtn);
        mResetbtn = (Button) findViewById(R.id.resetBtn);
    }

    private void reset(){
        mId.setText("");
        mName.setText("");
        mPwd.setText("");
        mGender.setText("");
        mCellphone.setText("");
        mEmail.setText("");
        mCode.setText("");
    }
}
