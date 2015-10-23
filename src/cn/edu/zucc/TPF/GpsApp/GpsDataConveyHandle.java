package cn.edu.zucc.TPF.GpsApp;

import android.content.Context;

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
import java.util.List;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.GpsDataBean;
import cn.edu.zucc.TPF.Bean.TransportBean;
import cn.edu.zucc.TPF.util.CrcCompute;
import cn.edu.zucc.TPF.util.RemoteServerReader;

public class GpsDataConveyHandle implements Runnable{
    private GpsDataBean gpsData ;
    private RemoteServerReader serverReader;
    private Context context;
    private Socket socket;
    private String ip;
    private int port;
    private ObjectMapper map = new ObjectMapper();
    private String message;
    private String conveyContent;
    private InetSocketAddress addr;
    private CrcCompute crcCompute;
    private long period = 5*1000;
    private Timestamp recordtime = new Timestamp(System.currentTimeMillis());
    private TransportBean transportState;
    private List<Map<String, Object>> noticeList;
    
	public GpsDataConveyHandle(GpsDataBean gpsData, Context context , TransportBean transportState
			,List<Map<String, Object>> noticeList) {
		this.gpsData = gpsData;
		this.context = context;
		this.transportState = transportState;
		this.noticeList = noticeList;
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		serverReader = new RemoteServerReader(context);
		ip = serverReader.get("remoteip");
		port = Integer.parseInt(serverReader.get("remoteport"));
		addr = new InetSocketAddress(ip, port);
		crcCompute =  new CrcCompute(CrcCompute.CRC_16);
		while(true){
			try {				
			while(!(transportState.isTransportOn())){
				Thread.sleep(3*1000);
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
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	
	private void conveyData(){	
		socket = new Socket();		 
		try {
			message = map.writeValueAsString(gpsData);
			String type = "04";
			message = type + message;
			int crcResult = crcCompute.GetDataCrc(message.getBytes());
			String crcToHex = crcCompute.ChangeToHexCrc(crcResult);
			message = message + crcToHex;
			socket.connect(addr, 10000);
			PrintWriter pw = getWriter(socket);
			pw.println(message);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String alarms = br.readLine();
			if(alarms.length() > 6){
				   char[] alarmsToArray = alarms.toCharArray();
				   String conveyCrc = new String(alarmsToArray, alarmsToArray.length -4, 4);
				   conveyContent = new String(alarmsToArray, 0, alarmsToArray.length-4);			   
				   int computeCrc = crcCompute.GetDataCrc(conveyContent.getBytes());				   
				   if(crcCompute.ChangeToHexCrc(computeCrc).equals(conveyCrc)){
					   dealAlarmData();
				   }
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*Toast toast = Toast.makeText(context, "与服务器连接中断！", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();*/
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void dealAlarmData(){
		try {
			List <Map<String, Object>> alarmSource= map.readValue(conveyContent, List.class);
		    noticeList.clear();
		    noticeList.addAll(alarmSource);
		    
			/*List <AlarmDealBean> alarmList = new ArrayList<AlarmDealBean>();
			for(int i=0;i<alarmSource.size();i++){
				AlarmDealBean temp = ObjectChange.MapToAlarmDealBean(alarmSource.get(i));
				alarmList.add(temp);
			}*/
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
