package com.jian86_android.hahaclass;

public class ItemInstructor {
    private String title;
    private String subTitle;
    private int imgPath;

    public ItemInstructor(String title, String subTitle, int imgPath) {
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

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }
}
