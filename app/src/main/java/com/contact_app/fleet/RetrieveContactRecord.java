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
}
