package cn.edu.zucc.TPF.Bean;

/**
 * Created by aqi on 15/10/10.
 */
public class LiftBean {
    private String liftid;
    private String liftname;
    private String pwd;
    private float accxLimit;
    private float accyLimit;
    private float acczLimit;
    private float latitude;
    private float longitude;
    private String address;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLiftid() {
        return liftid;
    }

    public void setLiftid(String liftid) {
        this.liftid = liftid;
    }

    public String getLiftname() {
        return liftname;
    }

    public void setLiftname(String liftname) {
        this.liftname = liftname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public float getAccxLimit() {
        return accxLimit;
    }

    public void setAccxLimit(float accxLimit) {
        this.accxLimit = accxLimit;
    }

    public float getAccyLimit() {
        return accyLimit;
    }

    public void setAccyLimit(float accyLimit) {
        this.accyLimit = accyLimit;
    }

    public float getAcczLimit() {
        return acczLimit;
    }

    public void setAcczLimit(float acczLimit) {
        this.acczLimit = acczLimit;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
