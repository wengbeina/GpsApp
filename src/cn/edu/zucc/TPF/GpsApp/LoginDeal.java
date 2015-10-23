package cn.edu.zucc.TPF.GpsApp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.edu.zucc.TPF.Bean.UserBean;
import cn.edu.zucc.TPF.util.CrcCompute;
import cn.edu.zucc.TPF.util.RemoteServerReader;

public class LoginDeal {
	private RemoteServerReader serverReader;
    private Context context;
    private Socket socket;
    private String ip;
    private int port;
    private ObjectMapper map = new ObjectMapper();
    private String message;
    private InetSocketAddress addr;
    private CrcCompute crcCompute;
    private UserBean user;
    private String result;

    
	public LoginDeal(Context context, UserBean user) {
		this.context = context;
		this.user = user;
		// TODO Auto-generated constructor stub
	}
	
	public void login(){
		serverReader = new RemoteServerReader(context);
		ip = serverReader.get("remoteip");
		port = Integer.parseInt(serverReader.get("remoteport"));
		crcCompute =  new CrcCompute(CrcCompute.CRC_16);
		addr = new InetSocketAddress(ip, port);
		socket = new Socket();
		
		try {
			message = map.writeValueAsString(user);
			String type = "03";
			message = type + message;
			int crcResult = crcCompute.GetDataCrc(message.getBytes());
			String crcToHex = crcCompute.ChangeToHexCrc(crcResult);
			message = message + crcToHex;
			socket.connect(addr, 10000);
			
			PrintWriter pw = getWriter(socket);
			pw.println(message);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			result = br.readLine();
			if(result.equals("2")){
				Toast toast = Toast.makeText(context, "用户名不存在！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			else if(result.equals("1")){
				Toast toast = Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Intent intent = new Intent(context, GpsActivity.class);;
				
				Bundle mBundle =  new Bundle();
				mBundle.putSerializable("USER", user);
				intent.putExtras(mBundle);
				context.startActivity(intent);
			}
			else{
				Toast toast = Toast.makeText(context, "登录密码错误！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast toast = Toast.makeText(context, "连接服务器超时！", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
}
