/**
* @Package com.manyouren.android.core.user    
* @Title: Album.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-13 下午3:59:50 
* @version V1.0   
*/
package com.manyouren.manyouren.core.user;

import android.os.Parcel;
import android.os.Parcelable;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table album.
 */
public class Album implements Parcelable {

    private Long id;
    private Long userId;
    private String username = "";
    private String smallAvatar = "";
    private String dateline = "2014-7-12";
    private Integer albumtype = 1;
    private String albumcity = "西安";
    private String location;
    private String content;
    private String pics = "[]";
    private Integer likenum = 0;
    private float rating = 3.0f;
    private String picResource = "url";
    private int gender=0;

    public Album() {
    }

    public Album(Long id) {
        this.id = id;
    }

    public Album(Long id, Long userId, String username, String smallAvatar, String dateline, Integer albumtype, String albumcity, String location, 
    		String content, String pics, Integer likenum,float rating,String picResource,int gender) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.smallAvatar = smallAvatar;
        this.dateline = dateline;
        this.albumtype = albumtype;
        this.albumcity = albumcity;
        this.location = location;
        this.content = content;
        this.pics = pics;
        this.likenum = likenum;
        this.rating = rating;
        this.picResource = picResource;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getSmallAvatar() {
        return smallAvatar;
    }

    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public Integer getAlbumtype() {
        return albumtype;
    }

    public void setAlbumtype(Integer albumtype) {
        this.albumtype = albumtype;
    }

    public String getAlbumcity() {
        return albumcity;
    }

    public void setAlbumcity(String albumcity) {
        this.albumcity = albumcity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public Integer getLikenum() {
        return likenum;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public float getRating() {
        return rating;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }
    
    public void setPicResource(String picResource) {
        this.picResource = picResource;
    }
    
    public String getPicResource() {
        return picResource;
    }
    
    public void setGender(int gender) {
        this.gender = gender;
    }
    
    public int getGender() {
        return gender;
    }


	/* 
	* <p>Title: describeContents</p> 
	* <p>Description: </p> 
	* @return 
	* @see android.os.Parcelable#describeContents() 
	*/
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* 
	* <p>Title: writeToParcel</p> 
	* <p>Description: </p> 
	* @param dest
	* @param flags 
	* @see android.os.Parcelable#writeToParcel(android.os.Parcel, int) 
	*/
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(id);
		dest.writeLong(userId);
		dest.writeString(username);
		dest.writeString(smallAvatar);
		dest.writeString(dateline);
	    dest.writeInt(albumtype);
	    dest.writeString(albumcity);
	    dest.writeString(location);
	    dest.writeString(content);
	    dest.writeString(pics);
	    dest.writeInt(likenum);
	    dest.writeFloat(rating);
	    dest.writeString(picResource);
	    dest.writeInt(gender);
	}
	
	public Album(Parcel source) {  
		this.id = source.readLong();
		this.userId = source.readLong();
		this.username = source.readString();
		this.smallAvatar = source.readString();
		this.dateline = source.readString();
		this.albumtype = source.readInt();
		this.albumcity = source.readString();
		this.location = source.readString();
		this.content = source.readString();
		this.pics = source.readString();
		this.likenum = source.readInt();
		this.rating = source.readFloat();
		this.picResource = source.readString();
		this.gender = source.readInt();
	 }  
	
	public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {  
		  
	  @Override  
	  public Album createFromParcel(Parcel source) {  
	  
	   return new Album(source);  
	  }  
	  
	  @Override  
	  public Album[] newArray(int size) {  
	  
	   return new Album[size];  
	   // throw new UnsupportedOperationException();  
	  }  
	  
	 };  

}