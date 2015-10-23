package cn.edu.zucc.TPF.GpsApp;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import cn.edu.zucc.TPF.Bean.GpsDataBean;

public class DataChangeHandle implements Runnable{
    private GpsDataBean gpsData;
    private int count;
    private TextView textLatitude;
    private TextView textLongitude;
	public DataChangeHandle(GpsDataBean gpsData, Activity act) {
		// TODO Auto-generated constructor stub
		this.gpsData = gpsData;
		/*this.textLatitude = (TextView)act.findViewById(R.id.latitudeText);
		this.textLongitude = (TextView)act.findViewById(R.id.longitudeText);*/
	}
	
	public void run(){
		while(true){
			gpsDataDeal();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    private void gpsDataDeal(){
		float[] a = {30.331164f, 30.329f, 30.330596f, 30.330313f, 30.330302f};
		float[] b = {120.149351f, 120.149351f, 120.149431f, 120.149627f, 120.149644f};		
		if(count == 5) count =0;
		gpsData.setLatitude(a[count]);
		gpsData.setLongitude(b[count]);
		count++;
		textLatitude.setText(String.valueOf(gpsData.getLatitude()));
		textLongitude.setText(String.valueOf(gpsData.getLongitude()));
	}

}
