/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.wifipass;

/**
 *
 * @author AZIZUL
 */

public class Student {
    public String name;
    public String id;
    private String signedBy;
    private String wifipassword;
    long startTime;
    long endTime;
   
    Student(String name, int num){
        this.name = name;
        this.id = num + "";
        signedBy = "-1";
        wifipassword = "-1";
    }    

    void signRequest(String name) {
        signedBy = name;
    }
    void setPassword(String pass) {
        wifipassword = pass;    
    } 
    void setStartTime(long tm){
        startTime = tm;
    }
    void setEndTime(long tm){
        endTime = tm;
    }
    double totalTime(){
        return ((double)endTime-startTime)/1000000.0;
    }

    String getPassword() {
        return wifipassword;
    }
}
