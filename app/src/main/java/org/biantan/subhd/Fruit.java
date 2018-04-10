package org.biantan.subhd;

public class Fruit {

    private String title;
    private String name;
    private String info;
    private String success;
    private String primary;
    private String imgurl;
    private String targetUrl;

    public Fruit(String title, String name, String info, String success, String primary, String imgurl, String targetUrl) {
        this.title = title;
        this.name = name;
        this.info = info;
        this.success = success;
        this.primary = primary;
        this.imgurl = imgurl;
        this.targetUrl = targetUrl;
    }

    public String gettitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getinfo() {
        return info;
    }

    public String getprimary() {
        return primary;
    }

    public String getsuccess() {
        return success;
    }

    public String getimgurl() {
        return imgurl;
    }

    public String gettargetUrl() {
        return targetUrl;
    }

}