package com.jian86_android.hahaclass;

public class ItemInstructor {
    private String title;
    private String subTitle;
    private String imgPath; //메인 이미지
    private String  license,field,career;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {

        this.license = license;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public ItemInstructor(String title, String subTitle, String imgPath) {
        this.title = title;
        this.subTitle = subTitle;
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public ItemInstructor(String title, String subTitle, String imgPath, String license, String field, String career) {
        this.title = title;
        this.subTitle = subTitle;
        this.imgPath = imgPath;
        this.license = license;
        this.field = field;
        this.career = career;
    }
}
