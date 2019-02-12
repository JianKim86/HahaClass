package com.jian86_android.hahaclass;

public class UserInfo {
    private static String name;
    private static String email;
    private static String phone;
    private static String password;
    private static String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        imagePath = imagePath;
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

    public UserInfo(String name, String email, String phone, String password) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public UserInfo(String name, String email, String phone, String password, String imagePath) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imagePath = imagePath;
    }
}
