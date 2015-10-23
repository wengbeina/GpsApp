package cn.edu.zucc.TPF.GpsApp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;

import cn.edu.zucc.TPF.Bean.AlarmDealBean;
import cn.edu.zucc.TPF.util.CrcCompute;
import cn.edu.zucc.TPF.util.RemoteServerReader;

/**
 * Created by aqi on 15/10/10.
 */
public class ItemActivity extends Activity {
    private TextView mLiftIdtv;
    private TextView mLiftAddresstv;
    private TextView mLatitudetv;
    private TextView mLongtitudetv;
    private TextView mTimetv;
    private TextView mDealbtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        init();
        getInfo();
        dealAlert();

    }

    private void dealAlert() {
        mDealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String liftid = mLiftIdtv.getText().toString();
                AlarmDealBean mAlarmDeal = new AlarmDealBean();
                mAlarmDeal.setLiftid(liftid);
                new AlarmDealConvey(mAlarmDeal).execute(null, null);
                Toast.makeText(ItemActivity.this, "处理完成！", Toast.LENGTH_SHORT).show();
                //等待两秒再关闭
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }
                }.start();

            }
        });


    }


    private void getInfo() {
        mLiftIdtv.setText(getIntent().getExtras().getCharSequence("liftid"));
        mLiftAddresstv.setText(getIntent().getExtras().getCharSequence("address"));
        mLatitudetv.setText(getIntent().getExtras().getCharSequence("latitude"));
        mLongtitudetv.setText(getIntent().getExtras().getCharSequence("longtitude"));
        mTimetv.setText(getIntent().getExtras().getCharSequence("time"));


    }

    private PrintWriter getWriter(Socket mSocket) throws IOException {
        OutputStream socketOut = mSocket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }

    private void init() {
        mLiftIdtv = (TextView) findViewById(R.id.liftid);
        mLiftAddresstv = (TextView) findViewById(R.id.liftaddress);
        mLatitudetv = (TextView) findViewById(R.id.latitude);
        mLongtitudetv = (TextView) findViewById(R.id.longtitude);
        mDealbtn = (Button) findViewById(R.id.dealbtn);
        mTimetv = (TextView) findViewById(R.id.time);
    }

    public class AlarmDealConvey extends AsyncTask<Object, Void, Object> {
        private RemoteServerReader serverReader;
        private Socket socket;
        private String ip;
        private int port;

        private ObjectMapper map = new ObjectMapper();
        private String message;
        private String conveyContent;

        private InetSocketAddress addr;
        private CrcCompute crcCompute;
        private Timestamp recordtime = new Timestamp(System.currentTimeMillis());
        private AlarmDealBean alarmDeal;

        public AlarmDealConvey(AlarmDealBean alarmDeal) {
            // TODO Auto-generated constructor stub
            this.alarmDeal = alarmDeal;
        }

        @Override
        protected Void doInBackground(Object... object) {
            serverReader = new RemoteServerReader(ItemActivity.this);
            ip = serverReader.get("remoteip");
            port = Integer.parseInt(serverReader.get("remoteport"));
            addr = new InetSocketAddress(ip, port);
            crcCompute = new CrcCompute(CrcCompute.CRC_16);
            recordtime.setTime(System.currentTimeMillis());
            alarmDeal.setRecordtime(recordtime);
            /**数据打包成文本字符串Gson格式进行传送*/
            conveyData();

            return null;
        }


        private PrintWriter getWriter(Socket socket) throws IOException {
            OutputStream socketOut = socket.getOutputStream();
            return new PrintWriter(socketOut, true);
        }

        private void conveyData() {
            socket = new Socket();
            try {
                message = map.writeValueAsString(alarmDeal);
                String type = "05";
                message = type + message;
                int crcResult = crcCompute.GetDataCrc(message.getBytes());
                String crcToHex = crcCompute.ChangeToHexCrc(crcResult);
                message = message + crcToHex;
                socket.connect(addr, 10000);
                PrintWriter pw = getWriter(socket);
                pw.println(message);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                /*Toast toast = Toast.makeText(context, "与服务器连接中断！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();*/
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


    }

}
