package cn.edu.zucc.TPF.GpsApp;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.AlarmDealBean;
import cn.edu.zucc.TPF.Bean.GpsDataBean;
import cn.edu.zucc.TPF.Bean.TransportBean;
import cn.edu.zucc.TPF.Bean.UserBean;
import cn.edu.zucc.TPF.adapter.GpsAdapter;
import cn.edu.zucc.TPF.util.CrcCompute;
import cn.edu.zucc.TPF.util.ObjectChange;
import cn.edu.zucc.TPF.util.RemoteServerReader;
import cn.edu.zucc.TPF.util.SlipButton;

public class GpsActivity extends Activity {
    private LocationManager locationManager;
    private SlipButton slipTransport;
    private TextView textCurrent;
    private TextView textNotice;
    private GpsDataBean gpsData = new GpsDataBean();
    private TransportBean transportState = new TransportBean();
    private Thread gpsDataConvey;
    private Geocoder geoCoder;
    private List<Address> addressList;
    private String bestProvider;

    private ListView listView;
    private LayoutInflater inflater;
    private int pageSize = 6;
    private int pageNum = 1;
    private int totalCount = 0;
    private SimpleAdapter simpleAdapter = null;
    private TextView totalShow;
    private TextView currentShow;
    private ImageButton preBtn;
    private ImageButton nextBtn;
    private Button showBtn;
    private LinearLayout noticeLayout;

    private List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> childList;

    public LocationClient mLocationClient;
    private Map<String, Object> locationMap = new HashMap<String, Object>();
    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;
    private String locationString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mLocationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //是否打开GPS
        option.setCoorType("bd0911"); //返回值的坐标类型
        option.setPriority(LocationClientOption.NetWorkFirst); //设置定位优先级
        option.setProdName("GPS App"); //设置产品线名称
        option.setScanSpan(UPDATE_TIME); //设置更新时间
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("time:");
                sb.append(location.getTime());
                sb.append("\nlatitus:");
                sb.append(location.getLatitude());
                sb.append("\nlongtitude:");
                sb.append(location.getLongitude());

                locationString = sb.toString();


                gpsData.setLatitude(location.getLatitude());
                gpsData.setLongitude(location.getLongitude());
                Log.e("jingweidu", gpsData.getLatitude() + "," + gpsData.getLongitude());
            }

        });

//        gpsData.setLatitude(30.330596f);
//        gpsData.setLongitude(120.149431f);

        UserBean user = (UserBean) getIntent().getSerializableExtra("USER");
//        UserBean user = new UserBean();
//        user.setUserid("");
//        user.setUsertype(0);
        gpsData.setUserid(user.getUserid());
        this.setTitle("GpsApp");
        geoCoder = new Geocoder(this);

        noticeLayout = (LinearLayout) this.findViewById(R.id.resultlayout);
        inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //test();
        ListViewInit();
        noticeLayout.addView(listView);
        slipTransport = (SlipButton) this.findViewById(R.id.transportSwitch);
        textCurrent = (TextView) this.findViewById(R.id.currentLocation);
        textNotice = (TextView) this.findViewById(R.id.noticeText);
        showBtn = (Button) this.findViewById(R.id.show);
        showBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                show();
            }
        });

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        bestProvider = locationManager.getBestProvider(getCriteria(), true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        updateView(location);
        locationManager.requestLocationUpdates(bestProvider, 500, 0.2f, locationListener);
        slipTransport.setOnChangedListener(new SlipButton.OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                // TODO Auto-generated method stub
                transportChange(CheckState);
            }
        });

        new GpsDataConveyTask(gpsData, transportState, noticeList).execute(null, null);

    }

    //位置监听器
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateView(location);
        }

        public void onProviderDisabled(String provider) {
            //updateView(null);
        }

        public void onProviderEnabled(String provider) {
            Location location = locationManager.getLastKnownLocation(provider);
            updateView(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            /*Location location = locationManager.getLastKnownLocation(provider);
            updateView(location);*/
        }
    };

    private void ListViewInit() {
        LinearLayout listTemp = (LinearLayout) inflater.inflate(R.layout.list_layout, null);
        View titleView = inflater.inflate(R.layout.list_title, null);
        View forpageView = inflater.inflate(R.layout.forpage, null);
        listView = (ListView) listTemp.findViewById(R.id.mListView);

        listTemp.removeView(listView);
        listView.addHeaderView(titleView);
        listView.addFooterView(forpageView);
        childList = getCurrentPageList(noticeList);
        simpleAdapter = new GpsAdapter(this, childList, R.layout.list_item, new String[]{"liftid",
                "unusualtime"}, new int[]{R.id.liftid, R.id.unusualtime}, noticeList);
        listView.setAdapter(simpleAdapter);

        totalShow = (TextView) forpageView.findViewById(R.id.total);
        currentShow = (TextView) forpageView.findViewById(R.id.current);
        totalCount = noticeList.size();
        totalShow.setText("总条数：" + totalCount);
        currentShow.setText("当前页：" + pageNum);

        preBtn = (ImageButton) forpageView.findViewById(R.id.preView);
        nextBtn = (ImageButton) forpageView.findViewById(R.id.nextView);

        preBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                nextBtn.setEnabled(true);
                if (pageNum > 1) {
                    pageNum--;
                    //childList = liftDao.getLiftdataList(user.getId(), from, to, pageNum, pageSize);
                    childList = getCurrentPageList(noticeList);
                    simpleAdapter = new GpsAdapter(GpsActivity.this, childList, R.layout.list_item, new String[]{"liftid",
                            "unusualtime"}, new int[]{R.id.liftid, R.id.unusualtime}, noticeList);
                    listView.setAdapter(simpleAdapter);
                    currentShow.setText("当前页：" + pageNum);
                }
                if (pageNum == 1) {
                    preBtn.setEnabled(false);
                }
                show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                preBtn.setEnabled(true);
                if (pageNum < getDealer(totalCount, pageSize)) {
                    pageNum++;
                    //childList = liftDao.getLiftdataList(user.getId(), from, to, pageNum, pageSize);
                    childList = getCurrentPageList(noticeList);
                    simpleAdapter = new GpsAdapter(GpsActivity.this, childList, R.layout.list_item, new String[]{"liftid",
                            "unusualtime"}, new int[]{R.id.liftid, R.id.unusualtime}, noticeList);
                    listView.setAdapter(simpleAdapter);
                    currentShow.setText("当前页：" + pageNum);
                          /*totalCount = liftDao.getLiftdataCount(user.getId(), from, to);
                       totalShow.setText("总条数："+totalCount);*/
                }
                if (pageNum == getDealer(totalCount, pageSize)) {
                    nextBtn.setEnabled(false);
                }
                show();
            }
        });
    }

    //得到总页数，即总记录数和页面记录数的商
    private int getDealer(int host, int client) {
        if (host % client == 0)
            return host / client;
        else
            return host / client + 1;
    }

    private void notice(int count) {
        //totalCount = noticeList.size();
        textNotice.setText("" + count);
    }

    private void show() {
        notice(noticeList.size());
        childList = getCurrentPageList(noticeList);
        simpleAdapter = new GpsAdapter(this, childList, R.layout.list_item, new String[]{"liftid",
                "unusualtime"}, new int[]{R.id.liftid, R.id.unusualtime}, noticeList);
        listView.setAdapter(simpleAdapter);
        noticeLayout.removeAllViews();
        noticeLayout.addView(listView);
        totalCount = noticeList.size();
        totalShow.setText("总条数：" + totalCount);
        currentShow.setText("当前页：" + pageNum);
    }


    private List<Map<String, Object>> getCurrentPageList(List<Map<String, Object>> allList) {
        List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
        if (pageSize * pageNum <= allList.size()) {
            for (int i = 0; i < pageSize; i++) {
                childList.add(allList.get((pageNum - 1) * pageSize + i));
            }
        } else {
            for (int i = 0; i < allList.size() - (pageNum - 1) * pageSize; i++) {
                childList.add(allList.get((pageNum - 1) * pageSize + i));
            }
        }

        return childList;
    }

    /**
     * 向服务器传送数据的滑动开关监听事件代码
     */
    private void transportChange(boolean checkState) {
        if (checkState == true) {

            mLocationClient.start();

            transportState.setTransportOn(true);
            Toast toast = Toast.makeText(this, "传输功能启动！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {

            mLocationClient.stop();
            mLocationClient.requestLocation();

            transportState.setTransportOn(false);
            Toast toast = Toast.makeText(this, "传输功能关闭", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    public void updateView(Location newLocation) {
        Location location = newLocation;

        if (location != null) {
            gpsData.setLatitude((float) location.getLatitude());
            gpsData.setLongitude((float) location.getLongitude());
        }
        try {
            addressList = geoCoder.getFromLocation(gpsData.getLatitude(), gpsData.getLongitude(), 5);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (addressList != null && addressList.size() > 0) {
            String result = "";
            Address i = addressList.get(0);
            if (i.getCountryName() == null) {
                i.setCountryName("");
            }
            if (i.getAdminArea() == null) {
                i.setAdminArea("");
            }
            if (i.getLocality() == null) {
                i.setLocality("");
            }
            if (i.getSubLocality() == null) {
                i.setSubLocality("");
            }
            if (i.getFeatureName() == null) {
                i.setFeatureName("");
            }

            result += i.getCountryName() + i.getAdminArea() + i.getLocality() +
                    i.getSubLocality() + i.getFeatureName();
            //Address addr = addressList.get(0);
            textCurrent.setText(result);
            //textLongitude.setText(String.valueOf(gpsData.getLongitude()));

        } else
            textCurrent.setText("连接不到谷歌服务器！请等待重试！");
        textNotice.setText("0");

    }

    public Criteria getCriteria() {
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_MEDIUM);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setBearingRequired(false);
        c.setAltitudeRequired(false);
        c.setPowerRequirement(Criteria.POWER_LOW);

        return c;
    }


    /**
     * 内部类
     */
    public class GpsDataConveyTask extends AsyncTask<Object, Void, Object> {
        private RemoteServerReader serverReader;
        private Socket socket;
        private String ip;
        private int port;
        private ObjectMapper map = new ObjectMapper();
        private String message;
        private String conveyContent;
        private InetSocketAddress addr;
        private CrcCompute crcCompute;
        private long period = 5 * 1000;
        private Timestamp recordtime = new Timestamp(System.currentTimeMillis());
        private GpsDataBean gpsData;
        private TransportBean transportState;
        private List<Map<String, Object>> noticeList;

        public GpsDataConveyTask(GpsDataBean gpsData, TransportBean transportState,
                                 List<Map<String, Object>> noticeList) {
            // TODO Auto-generated constructor stub
            this.gpsData = gpsData;
            this.transportState = transportState;
            this.noticeList = noticeList;
        }


        @Override
        protected Void doInBackground(Object... object) {
            serverReader = new RemoteServerReader(GpsActivity.this);
            ip = serverReader.get("remoteip");
            port = Integer.parseInt(serverReader.get("remoteport"));
            addr = new InetSocketAddress(ip, port);
            crcCompute = new CrcCompute(CrcCompute.CRC_16);
            while (true) {
                try {
                    while (!(transportState.isTransportOn())) {
                        Thread.sleep(3 * 1000);
                    }
                    recordtime.setTime(System.currentTimeMillis());
                    gpsData.setRecordtime(recordtime);
                    /**数据打包成文本字符串Gson格式进行传送*/
                    conveyData();
                    Thread.sleep(period);

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private PrintWriter getWriter(Socket socket) throws IOException {
            OutputStream socketOut = socket.getOutputStream();
            return new PrintWriter(socketOut, true);
        }

        private void conveyData() {
            socket = new Socket();
            try {
                message = map.writeValueAsString(gpsData);
                String type = "04";
                message = type + message;
                int crcResult = crcCompute.GetDataCrc(message.getBytes());
                String crcToHex = crcCompute.ChangeToHexCrc(crcResult);
                message = message + crcToHex;
                socket.connect(addr, 1000);
                PrintWriter pw = getWriter(socket);
                pw.println(message);

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                String alarms = br.readLine();
                Log.e("gpsreadline", alarms);
                if (("NOITEMe346").equals(alarms)) {
                    noticeList.clear();

                } else if (alarms.length() > 6) {
                    char[] alarmsToArray = alarms.toCharArray();
                    conveyContent = new String(alarmsToArray, 0, alarmsToArray.length - 4);
                    dealAlarmData();
                }


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

        private void dealAlarmData() {
            try {
                List<Map<String, Object>> alarmSource = map.readValue(conveyContent, List.class);
                List<AlarmDealBean> alarmList = new ArrayList<AlarmDealBean>();
                for (Map<String, Object> i : alarmSource) {
                    alarmList.add(ObjectChange.MapToAlarmDealBean(i));
                }

                noticeList.clear();
                for (AlarmDealBean i : alarmList) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", Integer.valueOf(i.getId()));
                    map.put("liftid", i.getLiftid());
                    map.put("warninglevel", Integer.valueOf(i.getWarninglevel()));
                    map.put("distence", Float.valueOf(i.getDistence()));
                    map.put("unusualtime", i.getUnusualtime().toString());
                    map.put("address", i.getAddress().toString());
                    map.put("latitude", Float.valueOf(i.getLiftlatitude()));
                    map.put("longtitude", Float.valueOf(i.getLiftlongitude()));
                    noticeList.add(map);
                }

            } catch (JsonParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
