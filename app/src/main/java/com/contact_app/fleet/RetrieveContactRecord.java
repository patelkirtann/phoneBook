package com.contact_app.fleet;

import android.graphics.Bitmap;

public class RetrieveContactRecord {

    private String mId;
    private String mName;
    private String mEmail;
    private String mPhone;
    private String mStreet;
    private String mCity;
    private String mIntro;
    private String mLogName;
    private String mLogNumber;
    private String mLogTime;
    private String mLogDuration;
    private String mLogDate;
    private byte[] mPicture;
    private int mLogCallType;
    private Bitmap mLogCallImage;

    public Bitmap getLogCallImage() {
        return mLogCallImage;
    }

    public void setLogCallImage(Bitmap mLogCallImage) {
        this.mLogCallImage = mLogCallImage;
    }

    public int getLogCallType() {
        return mLogCallType;
    }

    public void setLogCallType(int mLogCallType) {
        this.mLogCallType = mLogCallType;
    }

    public String getLogDate() {
        return mLogDate;
    }

    public void setLogDate(String mLogDate) {
        this.mLogDate = mLogDate;
    }

    public String getLogName() {
        return mLogName;
    }

    public void setLogName(String mLogName) {
        this.mLogName = mLogName;
    }

    public String getLogNumber() {
        return mLogNumber;
    }

    public void setLogNumber(String mLogNumber) {
        this.mLogNumber = mLogNumber;
    }

    public String getLogTime() {
        return mLogTime;
    }

    public void setLogTime(String mLogTime) {
        this.mLogTime = mLogTime;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;

    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getIntro() {
        return mIntro;
    }

    public void setIntro(String mIntro) {
        this.mIntro = mIntro;
    }

    public byte[] getPicture() {
        return mPicture;
    }

    public void setPicture(byte[] mPicture) {
        this.mPicture = mPicture;
    }

    public String getLogDuration() {
        return mLogDuration;
    }

    public void setLogDuration(String mLogDuration) {
        this.mLogDuration = mLogDuration;
    }
}
