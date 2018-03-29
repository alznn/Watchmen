/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;

/**
 *
 * @author hp
 */
public class DatabaseConfig {
    
    private String  urlstr = "173.194.252.196";
    private String userStr= "alznn";
    private String pwStr= "zxcv041099";
    private String DBname ="uniplat";

   public void setDBname(String DBname) {
        this.DBname = DBname;
    }
   
   public String getDBname() {
        return DBname;
    }

    public String getUrlstr() {
        return urlstr;
    }

    public void setUrlstr(String urlstr) {
        this.urlstr = urlstr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }

    public void setPwStr(String pwStr) {
        this.pwStr = pwStr;
    }

    public String getUserStr() {
        return userStr;
    }

    public String getPwStr() {
        return pwStr;
    }
}