package com.contact_app.fleet;

class RetrieveContactRecord {

    private String mId, mName, mEmail, mPhone, mStreet, mCity, mIntro;
    private byte[] mPicture;

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
}
