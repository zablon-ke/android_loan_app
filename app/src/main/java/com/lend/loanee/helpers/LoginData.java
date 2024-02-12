package com.lend.loanee.helpers;

import org.json.JSONObject;

import java.io.Serializable;

public class LoginData  implements Serializable {

    String mobile;
    String password;
    String token="";
    String role="";
    JSONObject details;

    public LoginData(String mobile, String password, String token) {
        this.mobile = mobile;
        this.password = password;
        this.token = token;
    }

    public LoginData(String mobile, String password, String token, String role) {
        this.mobile = mobile;
        this.password = password;
        this.token = token;
        this.role = role;
    }

    public LoginData(String mobile, String password, String token, JSONObject details) {
        this.mobile = mobile;
        this.password = password;
        this.token = token;
        this.details = details;
    }
    public LoginData(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public JSONObject getDetails() {
        return details;
    }

    public void setDetails(JSONObject details) {
        this.details = details;
    }
}
