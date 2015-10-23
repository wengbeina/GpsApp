package cn.edu.zucc.TPF.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.AlarmDealBean;

public class ObjectChange {

	public ObjectChange() {
		// TODO Auto-generated constructor stub
	}
	
	public static Map<String, Object> AlarmDealBeanToMap(AlarmDealBean alarm){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", Integer.valueOf(alarm.getId()));
		map.put("liftid", alarm.getLiftid());
		map.put("senderid", alarm.getSenderid());
		map.put("warninglevel", Integer.valueOf(alarm.getWarninglevel()));
		map.put("accx", Float.valueOf(alarm.getAccx()));
		map.put("accy", Float.valueOf(alarm.getAccy()));
		map.put("accz", Float.valueOf(alarm.getAccz()));
		map.put("address", alarm.getAddress());
		map.put("liftlatitude", Float.valueOf(alarm.getLiftlatitude()));
		map.put("liftlongitude", Float.valueOf(alarm.getLiftlongitude()));
		map.put("distence", Float.valueOf(alarm.getDistence()));
		map.put("unusualtime", alarm.getUnusualtime());
		map.put("recordtime", alarm.getRecordtime());
		map.put("dealtag", alarm.getDealtag());
		
		return map;
	}
	
	public static AlarmDealBean MapToAlarmDealBean(Map<String, Object> map){
		AlarmDealBean alarm = new AlarmDealBean();
		alarm.setId(Integer.parseInt(map.get("id").toString()));
		alarm.setLiftid(map.get("liftid").toString());
		alarm.setSenderid(map.get("senderid").toString());
		alarm.setWarninglevel(Integer.parseInt(map.get("warninglevel").toString()));
		alarm.setAccx(Float.parseFloat(map.get("accx").toString()));
		alarm.setAccy(Float.parseFloat(map.get("accy").toString()));
		alarm.setAccz(Float.parseFloat(map.get("accz").toString()));
		alarm.setAddress(map.get("address").toString());
		alarm.setLiftlatitude(Float.parseFloat(map.get("liftlatitude").toString()));
		alarm.setLiftlongitude(Float.parseFloat(map.get("liftlongitude").toString()));
		alarm.setDistence(Float.parseFloat(map.get("distence").toString()));
		alarm.setUnusualtime(new Timestamp((Long)map.get("unusualtime")));
		alarm.setRecordtime(new Timestamp((Long)map.get("recordtime")));
		alarm.setDealtag(Integer.parseInt(map.get("dealtag").toString()));
		
		return alarm;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
