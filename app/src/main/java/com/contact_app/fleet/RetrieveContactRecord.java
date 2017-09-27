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

    String getEmail() {
        return mEmail;
    }

    void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    String getPhone() {
        return mPhone;
    }

    void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    String getStreet() {
        return mStreet;
    }

    void setStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    String getCity() {
        return mCity;
    }

    void setCity(String mCity) {
        this.mCity = mCity;
    }

    String getIntro() {
        return mIntro;
    }

    void setIntro(String mIntro) {
        this.mIntro = mIntro;
    }

    byte[] getPicture() {
        return mPicture;
    }

    void setPicture(byte[] mPicture) {
        this.mPicture = mPicture;
    }

    public String getLogDuration() {
        return mLogDuration;
    }

    public void setLogDuration(String mLogDuration) {
        this.mLogDuration = mLogDuration;
    }
}
