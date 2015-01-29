/**
 * @Package com.manyouren.android.entity    
 * @Title: UserEntity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-10 下午4:21:44 
 * @version V1.0   
 */
package com.manyouren.manyouren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-10 下午4:21:44
 * 
 */
public class UserEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1211802439119529774L;

	private long userId;

	private String username;

	private String birthday;

	private int gender;

	private String homeland;

	private String residence;

	private String want2Go;

	private String school;

	private String beenThere;

	private int frequency;

	private String magazine;

	private String hobbyText;

	private String vehicleText;

	private String profession;

	private String company;

	private String avatar0; // josn String

	private String avatar1;

	private String avatar2;

	private String avatar3;

	private String email;

	private String objectId;

	public UserEntity() {

	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar0() {
		return avatar0;
	}

	public void setAvatar0(String avatar0) {
		this.avatar0 = avatar0;
	}

	public String getAvatar1() {
		return avatar1;
	}

	public void setAvatar1(String avatar1) {
		this.avatar1 = avatar1;
	}

	public String getAvatar2() {
		return avatar2;
	}

	public void setAvatar2(String avatar2) {
		this.avatar2 = avatar2;
	}

	public String getAvatar3() {
		return avatar3;
	}

	public void setAvatar3(String avatar3) {
		this.avatar3 = avatar3;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return username;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.username = userName;
	}

	/**
	 * @return the homeland
	 */
	public String getHomeland() {
		return homeland;
	}

	/**
	 * @param homeland
	 *            the homeland to set
	 */
	public void setHomeland(String homeland) {
		this.homeland = homeland;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getWant2Go() {
		return want2Go;
	}

	public void setWant2Go(String want2Go) {
		this.want2Go = want2Go;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getBeenThere() {
		return beenThere;
	}

	public void setBeenThere(String beenThere) {
		this.beenThere = beenThere;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getMagazine() {
		return magazine;
	}

	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}

	public String getHobbyText() {
		return hobbyText;
	}

	public void setHobbyText(String hobbyText) {
		this.hobbyText = hobbyText;
	}

	public String getVehicleText() {
		return vehicleText;
	}

	public void setVehicleText(String vehicleText) {
		this.vehicleText = vehicleText;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
