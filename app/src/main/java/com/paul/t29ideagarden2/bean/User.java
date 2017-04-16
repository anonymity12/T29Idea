package com.paul.t29ideagarden2.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by paul on 4/11/17.
 */

public class User extends BmobUser {
    private static final long serialVersionUID = 1L;
    private String intro;
    private Integer reputation;
    private Integer ideaCount;


    //    private BmobFile pic;
    private BmobGeoPoint gpsAdd;
    private BmobDate uploadTime;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
    @Override
    public String toString() {
        return getUsername()+"\n"+getObjectId()+"\n"+getSessionToken()+"\n"+getEmailVerified();
    }



/*我觉得，用户地点不用记录

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public BmobDate getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(BmobDate uploadTime) {
        this.uploadTime = uploadTime;
    }
*/

}
