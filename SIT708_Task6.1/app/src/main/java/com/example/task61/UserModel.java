package com.example.task61;

import java.util.ArrayList;

public class UserModel {

    String imageUrl, fullName, phoneNumber;
    ArrayList<OrderModel> orderList;

    public UserModel(String imageUrl, String fullName, String phoneNumber) {
        this(imageUrl, fullName, phoneNumber, new ArrayList<OrderModel>());
    }

    public UserModel(String imageUrl, String fullName, String phoneNumber, ArrayList<OrderModel> orderList) {
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.orderList = orderList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<OrderModel> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderModel> orderList) {
        this.orderList = orderList;
    }


}
