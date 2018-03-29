/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author hp
 */
public class GetDataInfo {

    static Vector bad_status = new Vector();
    static Vector<Double> myScore = new Vector<Double>();  //= {67,48,36,52,98,94,96,93,96,39,74,100,98,94,19,95,79,96,97,97,97,96,98,100};
    static Vector<Double> mySleep = new Vector<Double>();
    static String Score[];
    static String Sleep[];
    static double currentScore = 0;
    static double currentSleep = 0;

    public void GetData(ArrayList<USerInfo> userData) throws SQLException {
        System.out.print("-------------------------------------GetData-------------------------------------------------------- \n");
        DatabaseConnector sc = new DatabaseConnector("173.194.252.196", "alznn", "zxcv041099", "Watchmen_APP");
        sc.connectDB();
        for (int i = 0; i < userData.size(); i++) {
            USerInfo currentUser = userData.get(i);
            String Account = currentUser.getAccount();
            System.out.print("----------------------------------" + Account + "----------------------------------- \n");
            StringBuffer rs;
            String PVTQuery = "SELECT MeanRT FROM Watchmen_APP.PVT_History where User_ID = '" + Account + "'" + "ORDER BY MeanRT DESC;";
            String PVTNewst = "SELECT MeanRT FROM Watchmen_APP.PVT_History where User_ID = '%s' ORDER BY TestDate DESC,TestTime DESC";
            String SleepQuery = "SELECT Value FROM uniplat.Summary WHERE User_ID=" + "'" + Account + "'" + "and ID_Activity = 'efficiency' ORDER BY Value DESC;";
            String SleepNewst = "SELECT Value FROM uniplat.Summary WHERE User_ID='%s' and ID_Activity = 'efficiency' ORDER BY StartTime DESC;";

            //get all pvt score
            sc.doQuery(PVTQuery);
            rs = sc.getResultString();
            String str;
            str = rs.toString();
//            System.out.print("currentScore: " + str + "\n");
            Score = str.split("\n");

            for (int j = 1; j < Score.length; j++) {//
                Score[i] = Score[i].trim();
//                System.out.println("PVT: " + Score[j]);
                myScore.add(Double.parseDouble(Score[j]));
            }
//            for (int j = 0; j < myScore.size(); j++) {//
//                System.out.println("---------------------------------------my scorePVT: " + myScore.get(j));
//            }
            myMATH counting = new myMATH();
            currentUser.setLowPVT(counting.LowData(myScore));
            currentUser.setHighPVT(counting.HighData(myScore));
            //System.out.print("888888888888888888888888888888888888 Mu: " + counting.mu + "\n");
            //System.out.print("000000000000000000000000000000000000 theta: " + counting.theta + "\n");
            //get newst pvt score
            double current = 0.0;
            sc.doQuery(String.format(PVTNewst, Account));
            rs = sc.getResultString();
            str = rs.toString();
            Score = str.split("\n");
            current = (Double.parseDouble(Score[1]) - counting.mu) / counting.theta;
            currentUser.setCurrentPVT(current);
            //System.out.print("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMcurrentScore: " + currentUser.getCurrentPVT() + "\n");
            
            //get all sleep score
            sc.doQuery(SleepQuery);
            rs = sc.getResultString();
            str = rs.toString();
            Sleep = str.split("\n");

            for (int k = 1; k < Sleep.length; k++) {//
                Sleep[i] = Sleep[k].trim();
//                 System.out.println("Efficienct: " + Sleep[k]);
                mySleep.add(Double.parseDouble(Sleep[k]));
            }
            currentUser.setLowSleep(counting.LowData(mySleep));
            currentUser.setHighSleep(counting.HighData(mySleep));
            System.out.print("sssssssssssssssssssssssssssssssssss Mu: " + counting.mu + "\n");
            System.out.print("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee theta: " + counting.theta + "\n");
            //get newst Sleep Effi
            sc.doQuery(String.format(SleepNewst, Account));
            rs = sc.getResultString();
            str = rs.toString();
            Sleep = str.split("\n");
            current = (Double.parseDouble(Sleep[1]) - counting.mu) / counting.theta;
            currentUser.setCurrentSleep(current);
            System.out.print("currentScore: " + current + "\n");
             
            //GetSleep(userData.get(0));
        }

        sc.close();
    }

    public void GetNewPVT(USerInfo userData) throws SQLException {
        System.out.print("----------------------------------------GetNewPVT----------------------------------------------------- \n");
        String Account = userData.getAccount();
        String PVTQuery = "SELECT MeanRT FROM Watchmen_APP.PVT_History where User_ID = '" + Account + "'" + "ORDER BY MeanRT DESC;";
        String PVTNewst = "SELECT MeanRT FROM Watchmen_APP.PVT_History where User_ID = '%s' ORDER BY TestDate DESC,TestTime DESC";
        StringBuffer rs;
        USerInfo currentUser = userData;
        DatabaseConnector sc = new DatabaseConnector("173.194.252.196", "alznn", "zxcv041099", "Watchmen_APP");
        sc.connectDB();
        //get all pvt score
        sc.doQuery(PVTQuery);
        rs = sc.getResultString();
        String str;
        str = rs.toString();
//            System.out.print("currentScore: " + str + "\n");
        Score = str.split("\n");

        for (int j = 1; j < Score.length; j++) {//
            Score[j] = Score[j].trim();
            //System.out.println("PVT: " + Score[j]);
            myScore.add(Double.parseDouble(Score[j]));
        }
        myMATH counting = new myMATH();
        currentUser.setLowPVT(counting.LowData(myScore));
        currentUser.setHighPVT(counting.HighData(myScore));
        //System.out.print("888888888888888888888888888888888888 Mu: " + counting.mu + "\n");
        //System.out.print("000000000000000000000000000000000000 theta: " + counting.theta + "\n");
        //get newst pvt score
        double current = 0.0;
        sc.doQuery(String.format(PVTNewst, Account));
        rs = sc.getResultString();
        str = rs.toString();
        Score = str.split("\n");
        current = (Double.parseDouble(Score[1]) - counting.mu) / counting.theta;
        currentUser.setCurrentPVT(current);
        //System.out.print("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMcurrentScore: " + currentUser.getCurrentPVT() + "\n");
        sc.close();
    }

    //for 異質流量監控管理平台之程式判斷
    public void GetSleep(USerInfo userData) throws SQLException {
        System.out.print("----------------------------------------GetSleep----------------------------------------------------- \n");
        String FSleep[];
        Vector<Double> FySleep = new Vector<Double>();
        String Account = userData.getAccount();
        String FreshCSleepQuery = "SELECT AfterStandard FROM Watchmen.AfterStandard WHERE User_ID= '" + Account + "'ORDER BY DateOfSleep DESC;";
        String FreshSleepQuery = "SELECT AfterStandard FROM Watchmen.AfterStandard WHERE User_ID= '" + Account + "'ORDER BY AfterStandard DESC;";
        StringBuffer rs;
        USerInfo currentUser = userData;
        DatabaseConnector sc = new DatabaseConnector("173.194.252.196", "alznn", "zxcv041099", "Watchmen");
        sc.connectDB();
        //get all pvt score
        sc.doQuery(FreshSleepQuery);
        rs = sc.getResultString();
        String str;
        str = rs.toString();
//            System.out.print("currentScore: " + str + "\n");
        FSleep = str.split("\n");
        for (int j = 1; j < FSleep.length; j++) {//
            FSleep[j] = FSleep[j].trim();
            //System.out.println("sleep: " + FSleep[j]);
            FySleep.add(Double.parseDouble(FSleep[j]));
        }
        myMATH counting = new myMATH();
//        currentUser.setLowPVT(counting.LowData(FySleep));
//        currentUser.setHighPVT(counting.HighData(FySleep));

        //get current
        sc.doQuery(FreshCSleepQuery);
        rs = sc.getResultString();

        str = rs.toString();
        System.out.print("currentScore: " + str + "\n");
        FSleep = str.split("\n");
        for (int j = 1; j < FSleep.length; j++) {//
            FSleep[j] = FSleep[j].trim();
            //System.out.println("sleep: " + FSleep[j]);
            FySleep.add(Double.parseDouble(FSleep[j]));
        }
        currentUser.setCurrentSleep(FySleep.get(1));
        System.out.println("\n\n\ncurrent sleep: " + FySleep.get(1));
        System.out.println("\n\n\nLow sleep: " + counting.LowData(FySleep));
        System.out.println("\n\n\nHigh sleep: " + counting.HighData(FySleep));
        sc.close();
    }
}
