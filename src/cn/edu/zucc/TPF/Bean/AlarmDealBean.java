package cn.edu.zucc.TPF.Bean;

import java.sql.Timestamp;

public class AlarmDealBean {
    private int id;
    private String liftid;
    private String senderid;
    private int warninglevel;
    private float accx;
    private float accy;
    private float accz;
    private String address;
    private float liftlatitude;
    private float liftlongitude;
    private float distence;
    private Timestamp unusualtime;
    private Timestamp recordtime;
    private int dealtag;
    
	public AlarmDealBean() {
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
	 * @return the liftid
	 */
	public String getLiftid() {
		return liftid;
	}

	/**
	 * @param liftid the liftid to set
	 */
	public void setLiftid(String liftid) {
		this.liftid = liftid;
	}

	/**
	 * @return the warninglevel
	 */
	public int getWarninglevel() {
		return warninglevel;
	}

	/**
	 * @param warninglevel the warninglevel to set
	 */
	public void setWarninglevel(int warninglevel) {
		this.warninglevel = warninglevel;
	}

	/**
	 * @return the accx
	 */
	public float getAccx() {
		return accx;
	}

	/**
	 * @param accx the accx to set
	 */
	public void setAccx(float accx) {
		this.accx = accx;
	}

	/**
	 * @return the accy
	 */
	public float getAccy() {
		return accy;
	}

	/**
	 * @param accy the accy to set
	 */
	public void setAccy(float accy) {
		this.accy = accy;
	}

	/**
	 * @return the accz
	 */
	public float getAccz() {
		return accz;
	}

	/**
	 * @param accz the accz to set
	 */
	public void setAccz(float accz) {
		this.accz = accz;
	}

	/**
	 * @return the unusualtime
	 */
	public Timestamp getUnusualtime() {
		return unusualtime;
	}

	/**
	 * @param unusualtime the unusualtime to set
	 */
	public void setUnusualtime(Timestamp unusualtime) {
		this.unusualtime = unusualtime;
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

	/**
	 * @return the dealtag
	 */
	public int getDealtag() {
		return dealtag;
	}

	/**
	 * @param dealtag the dealtag to set
	 */
	public void setDealtag(int dealtag) {
		this.dealtag = dealtag;
	}

	/**
	 * @return the senderid
	 */
	public String getSenderid() {
		return senderid;
	}

	/**
	 * @param senderid the senderid to set
	 */
	public void setSenderid(String senderid) {
		this.senderid = senderid;
	}

	/**
	 * @return the distence
	 */
	public float getDistence() {
		return distence;
	}

	/**
	 * @param distence the distence to set
	 */
	public void setDistence(float distence) {
		this.distence = distence;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the liftlatitude
	 */
	public float getLiftlatitude() {
		return liftlatitude;
	}

	/**
	 * @param liftlatitude the liftlatitude to set
	 */
	public void setLiftlatitude(float liftlatitude) {
		this.liftlatitude = liftlatitude;
	}

	/**
	 * @return the liftlongitude
	 */
	public float getLiftlongitude() {
		return liftlongitude;
	}

	/**
	 * @param liftlongitude the liftlongitude to set
	 */
	public void setLiftlongitude(float liftlongitude) {
		this.liftlongitude = liftlongitude;
	}
}
