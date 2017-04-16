package com.paul.t29ideagarden2.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by paul on 4/11/17.
 */

public class Idea extends BmobObject {
/*以下从demo的bean/Post.java中copy*/

    private static final long serialVersionUID = 1L;
    private User author;
    private String title;
    private String content;
    private BmobFile image;
    private BmobRelation likes;//一对多关系：用于存储喜欢该帖子的所有用户

    public Idea() {super();}
    public Idea(String tableName) {
        super(tableName);
    }





    private Integer shared =0 ;
    private Integer hated;
    private BmobGeoPoint gps;







    public Integer getShared() {
        return shared;
    }

    public void setShared(Integer shared) {
        this.shared = shared;
    }

    public Integer getHated() {
        return hated;
    }

    public void setHated(Integer hated) {
        this.hated = hated;
    }

    public BmobGeoPoint getGps() {
        return gps;
    }

    public void setGps(BmobGeoPoint gps) {
        this.gps = gps;
    }



    /*以下从demo的bean/Post.java中copy*/
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public BmobRelation getLikes() {
        return likes;
    }
    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
    public BmobFile getImage() {
        return image;
    }
    public void setImage(BmobFile image) {
        this.image = image;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }



}
