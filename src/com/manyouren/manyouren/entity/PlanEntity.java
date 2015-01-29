/**   
 * @Title: PlanEntity.java 
 * @Package com.manyouren.android.entity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-7 下午12:22:03 
 * @version V1.0   
 */
package com.manyouren.manyouren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: PlanEntity
 * @author firefist_wei firefist.wei@gmail.com
 * @date 2014-6-7 下午12:22:03
 * 
 */
public class PlanEntity implements Serializable {

	private String planId;

	private String userId;

	private String createTime;

	private String scenicId;

	private String pName;

	private String myPName; // 如果scenicId ==0, 使用这个作为景点名

	private String startCity;

	private String startDate;

	private String endDate;

	private String vehicle;

	private String type;

	private String together;

	private String purpose;

	private String images;

	private String flight;

	private String postscript;

	private String like;

	private String live;

	private String lat;

	private String lon;

	private String adName;
	private String cName;

	// 下面是用户的数据
	private String username;
	private String gender;
	private String statusText;
	private String birthday;
	private String homeland;
	private String want2Go;
	private String school;
	private String beenThere;
	private String frequency;
	private String magazine;
	private String hobbyText;
	private String vehicleText;
	private String profession;
	private String company;
	private String residence;
	private String avatar0;
	private String objectId; // chat peerId

	public PlanEntity() {
	}

	public String getMyPName() {
		return myPName;
	}

	public void setMyPName(String myPName) {
		this.myPName = myPName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getScenicId() {
		return scenicId;
	}

	public void setScenicId(String scenicId) {
		this.scenicId = scenicId;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getStartCity() {
		return startCity;
	}

	public void setStartCity(String startCity) {
		this.startCity = startCity;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTogether() {
		return together;
	}

	public void setTogether(String together) {
		this.together = together;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getLive() {
		return live;
	}

	public void setLive(String live) {
		this.live = live;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHomeland() {
		return homeland;
	}

	public void setHomeland(String homeland) {
		this.homeland = homeland;
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
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

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getAvatar0() {
		return avatar0;
	}

	public void setAvatar0(String avatar0) {
		this.avatar0 = avatar0;
	}

}
