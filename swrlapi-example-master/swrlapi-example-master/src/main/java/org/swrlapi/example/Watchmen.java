/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.UserInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.swrlapi.example.SWRLAPIExample;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import org.swrlapi.example.USerInfo;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.swrlapi.example.API;
import org.swrlapi.example.myMATH;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

/**
 *
 * @author hp
 */
public class Watchmen {

    private Clock clock;  // dependency inject
    static String distaccount[];
    static String startTime[];
    static String endTime[];
    static String Account = "";
    static int state[] = {0, 0, 0};
    static String date;
    static ArrayList<USerInfo> userData = new ArrayList();

    public static void main(String[] args) throws SQLException, InterruptedException, OWLOntologyCreationException, IOException, ParseException, SQWRLException, SWRLParseException {
        int count = 0;
        GetDataInfo userinfo = new GetDataInfo();
        SWRLAPIExample rules = new SWRLAPIExample();
        ZoneId TPE = ZoneId.of("Asia/Taipei"); 
        ZonedDateTime startInstant = Instant.now().atZone(TPE);
       
 //=============================================================================================================================================================================================================================================//

        //TimeUnit.SECONDS.sleep(250);
/*        Instant stopInstant = Instant.now();
        Duration duration = Duration.between(startInstant, stopInstant);
         System.out.print("\n=====================================大於5=======================================\n" + duration.getSeconds() + "\n");
        if(duration.getSeconds() >= 5)
            System.out.print("\n=====================================大於5=======================================\n" + duration + "\n");
        if(duration.getSeconds() < 5)
            System.out.print("\n=====================================小於5=======================================\n" + duration + "\n");
         */

//=============================================================================================================================================================================================================================================//

        FileInputStream serviceAccount = new FileInputStream("watchmen-app-firebase-adminsdk-c3b9t-0ca56b9510.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl("https://watchmen-app.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
        /*
        GetDataInfo userinfo = new GetDataInfo();
        GetDriver();
        userinfo.GetData(userData);
        //rules.OntologyOpen(args, userData.get(0), 3);
        SWRLAPIExample rules = new SWRLAPIExample();
         rules.OntologyOpen(args, userData.get(0), 3);
//        for(int i = 0; i < userData.size() ; i++){
        //rules.OntologyOpen(args, userData.get(i), 3);
//        };
         */
        //persistCurrentStatus();
        //GetDriver();
        FirebaseListener listener = new FirebaseListener();
        API api = new API();
        api.GetClockTime("YZUACCHC17@gmail.com","2017-08-21","18:22:32");
        System.out.print("\n====================================="+api.GetClockTime("YZUACCHC17@gmail.com","2017-08-21","18:22:32")+"=======================================\n");
//         System.out.print("\n====================================="+userData.get(0).getEndWork()+"=======================================\n");
            String rs = listener.GetIsActiving();
            System.out.print("\n=====================================sdsds"+ rs +"=====================================================");

//GetDataInfo userinfo = new GetDataInfo();
GetDriver();
//userinfo.GetSleep(userData.get(0));
//GetDriver();

        while (true) {
            
            //GetDriver();
            userinfo.GetData(userData);
            System.out.println("\n -------------------------------------Game Start-------------------------------------");
            //for (int i = 0; i < userData.size(); i++) {
            //GetDriver();
            //userinfo.GetData(userData);
            if(userData.get(0).getStatus() == 0 || userData.get(0).getStatus() > 5){
                System.out.println("\n ------------------------------------- " +userData.get(0).getStatus()+"  燈號已被強制關閉-------------------------------------");
                return;
            }
            //有沒有觸發 alert
            if (FirebaseListener.GetMsg().equals("true") ||FirebaseListener.GetMsg().equals("null")) {
                System.out.println("\n -------------------------------------走PVT判斷的-------------------------------------");
                //GetDriver();
                //userinfo.GetData(userData);
                boolean isTest = mainCounter();
                if (isTest) {
                    System.out.println("\n Yes it did:");
                    rules.OntologyOpen(args, userData.get(0), 2);
                } else {
                    Info info = new Info();
                    int status = 0;
                    String reason = info.PVTBan;
                    System.out.println("\n main Driver Situation:" + " " + reason + " , " + status);
                    userData.get(0).setStatus(status);
                    userData.get(0).setReasons(reason);
                }
                FirebaseListener.SendMsg(userData.get(0).getAccount());
            } else if (count < 1) {
                System.out.println("\n -------------------------------------走綜合判斷的-------------------------------------");
                rules.OntologyOpen(args, userData.get(0), 3);
                count += 1;
          } 
//            else {
//                System.out.println("\n -------------------------------------走行車時間判斷的-------------------------------------");
//                rules.OntologyOpen(args, userData.get(0), 1);
//                //rules.OntologyOpen(args, userData.get(0), 2);
//            }
            date = today();
            persistCurrentStatus();
            System.out.print("\n=====================================做完一輪=======================================\n");
            TimeUnit.SECONDS.sleep(30);
            System.out.print("\n=====================================5秒結束準備下一輪=======================================\n");

        }

        //}
        //TimeUnit.SECONDS.sleep(60);

    }

    static public String today() {
        GetTime getdatetime = new GetTime();
        date = getdatetime.getDateTime();
        System.out.println("date: " + date);
        System.out.print(System.currentTimeMillis());
        return date;
    }

    static public void GetDriver() throws ParseException, IOException, FileNotFoundException, InterruptedException {
         FirebaseListener listener = new FirebaseListener();
         String isAct = listener.GetIsActiving();
         String getTime = listener.GetDrivingHour();
        USerInfo userinfo1 = new USerInfo();
        //YZUACCHC17@gmail.com O
        //yzufitbittest12@outlook.com O
        //yzufitbittest18@outlook.com O
        //yzufitbittest16@gmail.com X
        userinfo1.setAccount("YZUACCHC17@gmail.com");
        userinfo1.setDrivingStart(getTime);
        userinfo1.setIsDriving(isAct);
        userinfo1.setStarWork("2017-08-21 08:30:32");
        userinfo1.setStatus(-1);
        userData.add(userinfo1);
//        USerInfo userinfo = new USerInfo();
//        userinfo.setAccount("yzufitbittest12@outlook.com");
//        userinfo.setDrivingStart(getTime);
//        userinfo.setIsDriving(isAct);
//        userinfo.setStarWork("2017-08-15 08:02:02");
//        userData.add(userinfo);
//        USerInfo userinfo2 = new USerInfo();
//        userinfo2.setAccount("yzufitbittest10@gmail.com");
//        userinfo2.setDrivingStart(getTime);
//        userinfo2.setIsDriving(isAct);
//        userinfo2.setStarWork("2017-08-15 08:02:02");
//        userData.add(userinfo2);
//        USerInfo userinfo3 = new USerInfo();
//        userinfo3.setAccount("yzufitbittest18@outlook.com");
//        userinfo3.setDrivingStart(getTime);
//        userinfo3.setIsDriving(isAct);
//        userinfo3.setStarWork("2017-08-15 08:02:02");
//        userData.add(userinfo3);

//         DatabaseConnector SQL = new DatabaseConnector("173.194.252.196", "alznn", "zxcv041099", "Wathmen_APP");
//        SQL.connectDB();
//        SQL.doQuery("SELECT DISTINCT(User_ID) AS User_ID FROM Summary;");
//        StringBuffer rs = SQL.getResultString();
//        String str;
//        str = rs.toString();
//        distaccount = str.split("\n");
//        for (int i = 1; i < distaccount.length; i++) {
//            USerInfo userinfo = new USerInfo();
//            userinfo.setAccount(distaccount[i].substring(1));
//            userData.add(userinfo);
//            System.out.print("\nACount:" + userData.get(i-1).getAccount());
//        }

////        //這裡是紀錄上班時間
//
//         DatabaseConnector SQL = new DatabaseConnector("173.194.252.196", "alznn", "zxcv041099", "Wathmen_APP");
//        //SQL.connectDB();
//        for (int i = 0; i < userData.size(); i++) {
//            USerInfo userinfo5 = new USerInfo();
//            SQL.doQuery("SELECT StartTime FROM Watchmen_APP.PunchRecord where USer_ID = '" + userData.get(i).getAccount() + "' ORDER BY StartTime DESC;");
//            //StringBuffer 
//            StringBuffer rs = SQL.getResultString();
//            String str;
//            rs = SQL.getResultString();
//            //String str;
//            str = rs.toString();
//            startTime = str.split("\n");
//            System.out.print("\n=====================================" + startTime[1] + "=======================================\n");
//            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
//            java.util.Calendar cal = java.util.Calendar.getInstance();
//            cal.setTime(sdf.parse(startTime[1]));
//            userinfo5.setStarWork(cal.getTime());
//            System.out.print("\n=====================================" + "Time: " + userinfo5.getStarWork() + "=======================================\n");
//        }
        /*
        SQL.doQuery("SELECT EndTime FROM Watchmen_APP.PunchRecord where USer_ID = ' " + userData.get(0).getAccount() + "' ORDER BY EndTime DESC;");
        rs = SQL.getResultString();
        str = rs.toString();
        endTime = str.split("\n");
        System.out.print("\n====================================="+str+"=======================================\n");
//        userinfo.setAccount(dateTime[1]);
        userData.add(userinfo);
         */
    }

    static public void persistCurrentStatus() throws SQLException {
        date = today();
        String Light[] = {"close","Red","blue","Green"};
//        System.out.print(userData.get(0).getStatus() + "\ndate: " + date);
        String time = "";
        time = date.substring(11);
        System.out.print("date: " + date + "time\n" + time);
        DatabaseConnector IoT = new DatabaseConnector("173.194.252.196", "alznn", "zxcv041099", "IoT");
        IoT.connectDB();
//        for (int i = 0; i < userData.size(); i++) {
//            System.out.print(userData.get(i).getStatus() + "\ndate: " + date);
        IoT.updateQuery("insert into driver_status(driver_id,status,plate_number,update_time) values('" + userData.get(0).getAccount() + "','" + userData.get(0).getStatus() + "','" + "yzu888" + "', '" + date + "');");
        //System
        IoT.updateQuery("insert into StatusTransform(User_ID,Reason,Status,UpdateTime) values ('" + userData.get(0).getAccount() + "','" + userData.get(0).getReasons() + "','" + Light[userData.get(0).getStatus()] + "', '" + date + "');");
//        System.out.println("insert into Alert(Account,Alert,Message,Date) values('" + Account+ "','" + 1 +"', '" + msg + "','" + date + "');");

//        }
        IoT.close();
        return;
    }

    public static boolean mainCounter() throws IOException, FileNotFoundException, InterruptedException {
        for (int i = 0; i < 4; i++) {
            //FireBaseGetMsg();
            System.out.println( i +"\nHello isTest?");
            TimeUnit.SECONDS.sleep(5);
            if (FirebaseListener.GetMsgFromWeb().equals("true")) {
                return true;
            }
        }
        return false;
    }
}
