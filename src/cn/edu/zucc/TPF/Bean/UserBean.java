package cn.edu.zucc.TPF.Bean;

import java.io.Serializable;

public class UserBean implements Serializable{
    private String userid;
    private String userpwd;
    private String username;
    private String gender;
    private String cellphone;
    private int usertype;//0表示维保人员，1表示系统管理员
    private String email;
    private int isregistered; //0表示正在审核中，1表示已经注册成功
    
	public UserBean() {
		// TODO Auto-generated constructor stub
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
	 * @return the userpwd
	 */
	public String getUserpwd() {
		return userpwd;
	}

	/**
	 * @param userpwd the userpwd to set
	 */
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the cellphone
	 */
	public String getCellphone() {
		return cellphone;
	}

	/**
	 * @param cellphone the cellphone to set
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
	 * @return the usertype
	 */
	public int getUsertype() {
		return usertype;
	}

	/**
	 * @param usertype the usertype to set
	 */
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the isregistered
	 */
	public int getIsregistered() {
		return isregistered;
	}

	/**
	 * @param isregistered the isregistered to set
	 */
	public void setIsregistered(int isregistered) {
		this.isregistered = isregistered;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

}
