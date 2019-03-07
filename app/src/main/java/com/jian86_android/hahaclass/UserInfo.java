package com.jian86_android.hahaclass;

public class UserInfo {
    private  String name;
    private  String email;
    private  String phone;
    private  String password;
    private  String imagePath;
    private  int level;
    private String l_num;
    private ItemInstructor itemInstructor;
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UserInfo(String name, String email, String phone, String password, String imagePath, int level) {
        this.level = level;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imagePath = imagePath;
    }

    public UserInfo(String name, String email, int level) {
        this.name = name;
        this.email = email;
        this.level = level;
    }

    public String getL_num() {
        return l_num;
    }

    public void setL_num(String l_num) {
        this.l_num = l_num;
    }
}
