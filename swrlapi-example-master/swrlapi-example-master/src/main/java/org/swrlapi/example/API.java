/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;

import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author Chili
 */
public class API {

    /**
     * @param args the command line arguments
     */

    public static double GetAverageAsleepTime(String User, String Date, int num) throws ParseException, SQLException {
        double ans = 0;
        config c = new config();
        MySQLConnector sql = new MySQLConnector(c.getUrlstr(), c.getUserStr(), c.getPwStr(), c.getDBname());
        sql.connectDB();
        String query;
        String[] res = null;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(sdf.parse(Date));
        cal.add(java.util.Calendar.DATE, (num - 1) * -1);
        System.out.println(sdf.format(cal.getTime()));
        String end = sdf.format(cal.getTime());
        query = "select AVG(Value) as avg FROM uniplat.Summary Where DateTime<='" + Date + "'and DateTime>='" + end + "'and User_ID='" + User + "' and ID_Activity='minutesAsleep' ";
        res = sql.getdata(query, "avg");
        try {
            ans = Double.parseDouble(res[0]);
            //System.out.println(ans);
        } catch (NumberFormatException e) {
            System.out.println("浮點數轉換錯誤:"+e.getMessage());
        }
        sql.close();
        return ans/60;
    }

    public static double GetAwakeTime(String User, String Date, String Time) throws ParseException, SQLException {
        
        config c = new config();
        MySQLConnector sql = new MySQLConnector(c.getUrlstr(), c.getUserStr(), c.getPwStr(), c.getDBname());
        sql.connectDB();
        String query;
        String[] time = null;
        String[] enddate = null;
        /* java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(sdf.parse(Date));
        cal.add(java.util.Calendar.DATE, (num - 1) * -1);
        // System.out.println(sdf.format(cal.getTime()));
        String end = sdf.format(cal.getTime());*/

        query = "SELECT * FROM uniplat.Sleep_Detail where User_ID='" + User + "' and DateTime='" + Date + "' and EndTime<='" + Time + "' order by StartTime desc limit 1";
        time = sql.getdata(query, "EndTime");
        enddate = sql.getdata(query, "DateTime");
        String EndTime = time[0];
        String EndDate = enddate[0];
         java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date beginDate = sdf.parse(EndDate+" "+EndTime);
        java.util.Date endDate = sdf.parse(Date+" "+Time);
         //System.out.println(beginDate);
         //System.out.println(endDate);
        double difference=endDate.getTime()-beginDate.getTime();
        double answer=difference/(1000*60);
        //System.out.println(answer);
        sql.close();
        return answer/60;
    }
  public static double GetClockTime(String User, String Date, String Time) throws ParseException, SQLException {
        
        config c = new config();
        MySQLConnector sql = new MySQLConnector(c.getUrlstr(), c.getUserStr(), c.getPwStr(), c.getDBname());
        sql.connectDB();
        String query;
        String[] time = null;
        String[] enddate = null;
         java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(format.parse(Date));
        cal.add(java.util.Calendar.DATE,  1);
        // System.out.println(sdf.format(cal.getTime()));
        String end = format.format(cal.getTime());

        query = "SELECT * FROM Watchmen_APP.PunchRecord where User_ID='" + User + "' and StartTime>='" + Date + "' and StartTime<'" + end + "' order by StartTime desc limit 1";
        time = sql.getdata(query, "StartTime");
        String EndTime = time[0];
         java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date beginDate = sdf.parse(EndTime);
        java.util.Date endDate = sdf.parse(Date+" "+Time);
         //System.out.println(beginDate);
         //System.out.println(endDate);
        double difference=endDate.getTime()-beginDate.getTime();
        double answer=difference/(1000*60);
        //System.out.println(answer);
        sql.close();
        return answer;
    }

}
