package com.doordashlite.oye.doordashlite.Data;


public class InfoItem {

    private static final String TAG = InfoItem.class.getSimpleName();

    private String mID;
    private String mName;
    private String mDescription;
    private String mStatus;
    private String mDeliveryFee;
    private String mCoverImgUrl;

    public InfoItem(String vID, String vName, String vDescription,
                    String vStatus, String vDeliveryFee, String vCoverImageUrl ){
        this.mID = vID;
        this.mName = vName;
        this.mDescription = vDescription;
        this.mStatus = vStatus;
        this.mDeliveryFee = vDeliveryFee;
        this.mCoverImgUrl = vCoverImageUrl;
        final String coverURL = vCoverImageUrl;
    }

    public String getmID() {
        return mID;
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getmDeliveryFee() {
        return mDeliveryFee;
    }

    public String getmCoverImgUrl() {
        return mCoverImgUrl;
    }

}
