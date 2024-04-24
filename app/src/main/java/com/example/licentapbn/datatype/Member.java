package com.example.licentapbn.datatype;

import java.io.Serializable;
import java.util.List;

public class Member implements Serializable {
    private String name;
    private String prename;
    private String email;
    private String phoneNumber;
    private List<String> itemsId;
    private String id;

    public Member() {
    }

    public Member(String name, String prename, String email, String phoneNumber, List<String> itemsId, String id) {
        this.name = name;
        this.prename = prename;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.itemsId = itemsId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getItemsId() {
        return itemsId;
    }

    public void setItemsId(List<String> itemsId) {
        this.itemsId = itemsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", prename='" + prename + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", itemsId=" + itemsId +
                ", id='" + id + '\'' +
                '}';
    }

}


