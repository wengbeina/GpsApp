package cn.edu.zucc.TPF.Bean;

import java.sql.Timestamp;

public class GpsDataBean {
    private int id;
    private String userid;
    private double latitude;//纬度
    private double longitude;//经度
    private String clientip;
    private Timestamp recordtime;//记录时间
    
	public GpsDataBean() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the recordtime
	 */
	public Timestamp getRecordtime() {
		return recordtime;
	}
	/**
	 * @param recordtime the recordtime to set
	 */
	public void setRecordtime(Timestamp recordtime) {
		this.recordtime = recordtime;
	}
	public String getClientip() {
		return clientip;
	}
	public void setClientip(String clientip) {
		this.clientip = clientip;
	}
}
