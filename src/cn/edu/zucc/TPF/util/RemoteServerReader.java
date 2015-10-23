package cn.edu.zucc.TPF.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import android.content.Context;


public class RemoteServerReader {
	private Context context;
	static private Properties ps;
	
	public RemoteServerReader(Context context){
		this.context = context;
		init();
	}
	
	private void init(){
		ps =  new Properties();
		try {
			InputStream input = context.getAssets().open("serverip/server.properties");
			ps.load(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String get(String key){
		//return (String) config.getProperty(key);
		return (String) ps.get(key);
	}
	
	public void put(String key, String value){
		try {
			FileOutputStream output = new FileOutputStream("serverip/server.properties");
			ps.setProperty(key, value);
			ps.store(output, "Save properties!");
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
