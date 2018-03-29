package org.swrlapi.example;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;
//import org.swrlapi.builtins;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgument;
import org.swrlapi.builtins.AbstractSWRLBuiltInLibrary;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.sqwrl.exceptions.SQWRLException;
import org.swrlapi.example.API;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SWRLAPIExample {

    public static void OntologyOpen(String args[], USerInfo userData, int condition) throws OWLOntologyCreationException, InterruptedException, IOException, SQLException, ParseException, SQWRLException, SWRLParseException {
        FirebaseListener listener = new FirebaseListener();
        Optional<@NonNull String> owlFilename = args.length == 0 ? Optional.<@NonNull String>empty() : Optional.of(args[0]);
        Optional<@NonNull File> owlFile = (owlFilename != null && owlFilename.isPresent())
                ? Optional.of(new File(owlFilename.get()))
                : Optional.<@NonNull File>empty();
        // Create an OWL ontology using the OWLAPI
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = owlFile.isPresent()
                ? ontologyManager.loadOntologyFromOntologyDocument(owlFile.get())
                : ontologyManager.createOntology();

        // Create SQWRL query engine using the SWRLAPI
        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);
        System.out.println("/---------------------------------------------SWRL---------------------------------------------");
        //---------------------------------------------sleep condition---------------------------------------------

        switch (condition) {
            case 1://開車時間
                //OWLOntology ontology, double curentScore, double HighScore, double LowScore
                DrivingCondition(ontology, userData);
                break;
            case 2://有測驗時的成績
                //OWLOntology ontology, double curentScore, double HighScore, double LowScore
                PVTCondition(ontology, userData);
                break;
            case 3://綜合條件判斷
                //ontology , Account , double sleepHigh,sleepCurrent,sleepLow,
                //currentScore , LowScore , HighScore , Driving
                //notice PVT score smaller perfome better
                System.out.println("PVT Test: " + userData.getCurrentPVT() + "  " + userData.getLowPVT() + "  " + userData.getHighPVT());
                System.out.println("Sleep Test:\nCurrent:　" + userData.getCurrentSleep() + " \nLow: " + userData.getLowSleep() + " \nHigh: " + userData.getHighSleep());
                FatigueOntology(ontology, userData);
                break;
            case 4://專注力方程式
                //ontology , Account , double sleepHigh,sleepCurrent,sleepLow,
                //currentScore , LowScore , HighScore , Driving
                //notice PVT score smaller perfome better
                System.out.println("PVT Test: " + userData.getCurrentPVT() + "  " + userData.getLowPVT() + "  " + userData.getHighPVT());
                System.out.println("Sleep Test:\nCurrent:　" + userData.getCurrentSleep() + " \nLow: " + userData.getLowSleep() + " \nHigh: " + userData.getHighSleep());
                RealTimeFatigue(ontology, userData);
                break;
        }
        System.out.println("Finish");
    }

    private static void Usage() {
        System.err.println("Usage: " + SWRLAPIExample.class.getName() + " [ <owlFileName> ]");
        System.exit(1);
    }

    private static void FatigueOntology(OWLOntology ontology, USerInfo userData) throws InterruptedException, IOException, SQLException {
        FirebaseListener listener = new FirebaseListener();
        GetDataInfo pvtNewst = new GetDataInfo();
        int status = 5;
        String reason = "";
        String Account = userData.getAccount();
        int Driving = 20;
        //userData.getDrivingHour();
        Double currentScore = userData.getCurrentPVT(), LowScore = userData.getLowPVT(), HighScore = userData.getHighPVT();
        Double sleepCurrent = userData.getCurrentSleep(), sleepLow = userData.getLowSleep(), sleepHigh = userData.getHighSleep();
        Info info = new Info();

        try {
            // Create SQWRL query engine using the SWRLAPI
            SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);

            System.out.println("-------------------------------------------------------  綜合條件 -------------------------------------------------------");

            //---------------------------------------------sleep condition---------------------------------------------
            SQWRLResult SleepGreat = queryEngine.runSQWRLQuery("SleepGreat", "swrlb:greaterThanOrEqual(" + userData.getCurrentSleep() + "," + userData.getHighSleep() + ") -> sqwrl:select(" + userData.getHighSleep() + ")");
            SQWRLResult SleepBan = queryEngine.runSQWRLQuery("SleepBan", "swrlb:greaterThanOrEqual(" + userData.getLowSleep() + "," + userData.getCurrentSleep() + ") -> sqwrl:select(" + userData.getLowSleep() + ")");
            SQWRLResult SleepNormal = queryEngine.runSQWRLQuery("SleepNormal", "swrlb:greaterThan(" + userData.getCurrentSleep() + "," + userData.getLowSleep() + ")^swrlb:greaterThan(" + userData.getHighSleep() + "," + userData.getCurrentSleep() + ")  -> sqwrl:select(" + sleepCurrent + ")");

            SQWRLResult PVTGreat = queryEngine.runSQWRLQuery("PVTGreat", "swrlb:lessThanOrEqual(" + userData.getCurrentPVT() + "," + userData.getLowSleep() + ") -> sqwrl:select(" + userData.getLowSleep() + ")");
            SQWRLResult PVTBad = queryEngine.runSQWRLQuery("PVTBad", "swrlb:greaterThanOrEqual(" + userData.getCurrentPVT() + "," + userData.getHighSleep() + ") -> sqwrl:select(" + userData.getHighSleep() + ")");
            SQWRLResult PVTNormal = queryEngine.runSQWRLQuery("PVTNormal", "swrlb:greaterThan(" + userData.getCurrentPVT() + "," + userData.getLowSleep() + ")^swrlb:lessThan(" + userData.getCurrentPVT() + "," + userData.getHighSleep() + ")  -> sqwrl:select(" + userData.getCurrentPVT() + ")");

            SQWRLResult DrivingUp90 = queryEngine.runSQWRLQuery("Drivig90", "swrlb:greaterThanOrEqual(" + Driving + "," + 90 + ") -> sqwrl:select(" + Driving + ")");
            SQWRLResult DrivingUp50 = queryEngine.runSQWRLQuery("Drivig50", "swrlb:greaterThanOrEqual(" + Driving + "," + 50 + ")^swrlb:lessThan(" + Driving + "," + 90 + ") -> sqwrl:select(" + Driving + ")");
            SQWRLResult DrivingUp30 = queryEngine.runSQWRLQuery("Drivig30", "swrlb:greaterThanOrEqual(" + Driving + "," + 30 + ")^swrlb:lessThan(" + Driving + "," + 50 + ") -> sqwrl:select(" + Driving + ")");

            System.out.println("------------------Driving Time ------------------:" + Driving);

            // Process the SQWRL result
            //Driver can not drive
            if (SleepBan.next()) {
                reason = info.SleepBan;
                status = 0;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n1  Driver Situation:" + " " + reason + " , " + status);
            } //Driver sleep perform good
            else if (SleepGreat.next()) {
                status = 3;
                reason = info.SleepGreat;
                if (status < userData.getStatus()) {
                    userData.setStatus(status);
                    userData.setReasons(reason);
                }
                System.out.println("Great Set:" + SleepGreat);
//                連續駕駛超過30分鐘，狀態轉為須普通
                if (DrivingUp30.next()) {
                    status = 2;
                    reason = reason + info.DrivingOver30;
                    System.out.println("\n3  Driver Situation:" + " " + reason + " , " + status);
                    if (status < userData.getStatus()) {
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    }
                } //連續駕駛超過50分鐘，提醒進行專注力測驗，判斷前次專注力測驗成果
                else if (DrivingUp50.next()) {
                    status = 2;
                    reason = reason + info.DrivingOver50;
                    if (PVTGreat.next()) {
                        status = 3;
                        reason += info.PVTGreat;
                        System.out.println("\n4  Driver Situation:" + " " + reason + " , " + status);

                        userData.setStatus(status);
                        userData.setReasons(reason);
                    } else if (PVTNormal.next()) {
                        status = 2;
                        reason += info.PVTNormal;
                        System.out.println("\n5  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    } else if (PVTBad.next()) {
                        status = 1;
                        reason += info.PVTBad;
                        System.out.println("\n6  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    }
                    //連續駕駛超過90分鐘，強制進行專注力測試，若不過關強制休息
                } 
                  else if (DrivingUp90.next()) {
                    reason = reason + info.DrivingOver90;
                    status = 1;
                    userData.setStatus(status);
                    userData.setReasons(reason);
                    FirebaseListener.SendAlert(Account);
                    System.out.println("\n給Driver作測驗的時間");
                    if (Counter()) {
                        //給Driver作測驗的時間
                        System.out.println("\n再給Driver作測驗的時間");
                        pvtNewst.GetNewPVT(userData);
                        //TimeUnit.SECONDS.sleep(20);
                        System.out.println("\nDriver測驗有過");

                        if (PVTBad.next()) {
                            System.out.println("\nDriver測驗未過");
                            status = 0;
                            reason += info.DrivingBan;
                            System.out.println("\n7  Driver Situation:" + " " + reason + " , " + status);
                            userData.setStatus(status);
                            userData.setReasons(reason);
                        } else {
                            System.out.println("\nDriver測驗有過");
                            status = 1;
                            reason += info.PVTNormal;
                            System.out.println("\n8  Driver Situation:" + " " + reason + " , " + status);
                            userData.setStatus(status);
                            userData.setReasons(reason);
                        }
                        listener.SendMsg(userData.getAccount());
                    } else {
                        System.out.println("\nDriver未測驗");
                        status = 0;
                        reason += info.DrivingBan;
                        System.out.println("\n7-1  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);

                    }
                    System.out.println("\n8-1  Driver Situation:" + " " + reason + " , " + status);
                    listener.SendMsg(userData.getAccount());
                }
            } //Driver perform Normal
            else if (SleepNormal.next()) {
                status = 2;
                reason = info.SleepNormal;
                System.out.println("SleepNormal Set:" + SleepNormal);
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n9  Driver Situation:" + " " + reason + " , " + status);
                //連續駕駛超過90分鐘，狀態轉為須注意
                if (DrivingUp90.next()) {
                    reason = reason + info.DrivingOver90;
                    status = 1;
                    userData.setStatus(status);
                    userData.setReasons(reason);
                    listener.SendAlert(Account);
                    System.out.println("\n給Driver作測驗的時間");
                    if (Counter()) {
                        //給Driver作測驗的時間
                        System.out.println("\n再給Driver作測驗的時間");
                        pvtNewst.GetNewPVT(userData);
                        //TimeUnit.SECONDS.sleep(20);
                        System.out.println("\nDriver測驗有過");

                        if (PVTBad.next()) {
                            System.out.println("\nDriver測驗未過");
                            status = 0;
                            reason += info.DrivingBan;
                            System.out.println("\n7  Driver Situation:" + " " + reason + " , " + status);
                            userData.setStatus(status);
                            userData.setReasons(reason);
                        } else {
                            System.out.println("\nDriver測驗有過");
                            status = 1;
                            reason += info.PVTNormal;
                            System.out.println("\n8  Driver Situation:" + " " + reason + " , " + status);
                            userData.setStatus(status);
                            userData.setReasons(reason);
                        }
                    } else {
                        System.out.println("\nDriver未測驗");
                        status = 0;
                        reason += info.DrivingBan;
                        System.out.println("\n7-1  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    }
                    listener.SendMsg(userData.getAccount());
                    System.out.println("\n8-1  Driver Situation:" + " " + reason + " , " + status);
                } //連續駕駛超過50分鐘，狀態轉為須注意
                else if (DrivingUp50.next()) {
                    status = 1;
                    reason = reason + info.DrivingOver50;
                    System.out.println("\n10  Driver Situation:" + " " + reason + " , " + status);
                    userData.setStatus(status);
                    userData.setReasons(reason);
                    if (PVTBad.next()) {
                        status = 0;
                        reason += info.PVTBad;
                        System.out.println("\n11  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    } else if (PVTGreat.next()) {
                        status = 2;
                        reason += info.PVTGreat;
                        System.out.println("\n12  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    }
                } //連續駕駛超過30分鐘,狀態維持
                else if (DrivingUp30.next()) {
                    status = 2;
                    reason = reason + info.DrivingOver30;
                    System.out.println("\n13  Driver Situation:" + " " + reason + " , " + status);
                    userData.setStatus(status);
                    userData.setReasons(reason);
                    if (PVTGreat.next()) {
                        status = 3;
                        reason += info.PVTGreat;
                        System.out.println("\n14  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    } else if (PVTNormal.next()) {
                        status = 2;
                        reason += info.PVTNormal;
                        System.out.println("\nDriver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    } else if (PVTBad.next()) {
                        status = 1;
                        reason += info.PVTBad;
                        System.out.println("\n15  Driver Situation:" + " " + reason + " , " + status);
                        userData.setStatus(status);
                        userData.setReasons(reason);
                    }
                    reason = reason + info.DrivingUnder30;
                    System.out.println("\n16  Driver Situation:" + " " + reason + " , " + status);
                    userData.setStatus(status);
                    userData.setReasons(reason);
                }
            }

            System.out.println("\n\n------------------------------------------------------------------------------------AT LAST------------------------------------------------------------------------------------");
        } catch (SWRLParseException e) {
            System.err.println("Error parsing SWRL rule or SQWRL query: " + e.getMessage());
            System.exit(-1);
        } catch (SQWRLException e) {
            System.err.println("Error running SWRL rule or SQWRL query: " + e.getMessage());
            System.exit(-1);
        } catch (RuntimeException e) {
            System.err.println("Error starting application: " + e.getMessage());
            System.exit(-1);
        }
    }

//只有PVT因子時的判斷
    private static void PVTCondition(OWLOntology ontology, USerInfo userData) throws SQLException {
        int status = 0;
        String reason = "";
        GetDataInfo pvtNewst = new GetDataInfo();
        pvtNewst.GetNewPVT(userData);
        Info info = new Info();
        Double currentScore = userData.getCurrentPVT(), LowScore = userData.getLowPVT(), HighScore = userData.getHighPVT();
        try {
            // Create SQWRL query engine using the SWRLAPI
            SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);

            System.out.println("------------------------------------------------------- PVT condition -------------------------------------------------------");

            //---------------------------------------------PVT condition---------------------------------------------
            SQWRLResult PVTGreat = queryEngine.runSQWRLQuery("PVTGreat", "swrlb:lessThanOrEqual(" + currentScore + "," + LowScore + ") -> sqwrl:select(" + LowScore + ")");
            SQWRLResult PVTBad = queryEngine.runSQWRLQuery("PVTBad", "swrlb:greaterThanOrEqual(" + currentScore + "," + HighScore + ") -> sqwrl:select(" + HighScore + ")");
            SQWRLResult PVTNormal = queryEngine.runSQWRLQuery("PVTNormal", "swrlb:greaterThan(" + currentScore + "," + LowScore + ")^swrlb:lessThan(" + currentScore + "," + HighScore + ")  -> sqwrl:select(" + currentScore + ")");

            //Driver PVT perform good
            if (PVTGreat.next()) {
                status = 3;
                reason = info.PVTGreat;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n28  Driver Situation:" + " " + reason + " , " + status);
            }

            if (PVTBad.next()) {
                status = 1;
                reason = info.PVTBad;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("PVTBad Set:" + PVTBad);
                System.out.println("\n29  Driver Situation:" + " " + reason + " , " + status);
            }
            if (PVTNormal.next()) {
                status = 2;
                reason = reason + info.PVTNormal;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("PVTNormal Set:" + PVTNormal);
                System.out.println("\n30  Driver Situation:" + " " + reason + " , " + status);
            }

        } catch (SWRLParseException e) {
            System.err.println("Error parsing SWRL rule or SQWRL query: " + e.getMessage());
            System.exit(-1);
        } catch (SQWRLException e) {
            System.err.println("Error running SWRL rule or SQWRL query: " + e.getMessage());
            System.exit(-1);
        } catch (RuntimeException e) {
            System.err.println("Error starting application: " + e.getMessage());
            System.exit(-1);
        }

    }
//只有行車因子時的判斷

    private static void DrivingCondition(OWLOntology ontology, USerInfo userData) throws IOException, FileNotFoundException, InterruptedException, SQLException, ParseException {
        FirebaseListener listener = new FirebaseListener();
        GetDataInfo pvtNewst = new GetDataInfo();
        //DrivingTime(userData);
        int status = 0;
        String reason = "";
        Info info = new Info();
        int Driving = 20;
        userData.getDrivingHour();
        System.out.println("Driving90 Set:" + Driving);
//userData.getDrivingHour();
//
//        try {
//            // Create SQWRL query engine using the SWRLAPI
//            SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);
//            //---------------------------------------------sleep condition---------------------------------------------
//            SQWRLResult DrivingUp90 = queryEngine.runSQWRLQuery("Drivig90", "swrlb:greaterThanOrEqual(" + Driving + "," + 90 + ") -> sqwrl:select(" + Driving + ")");
//            SQWRLResult DrivingUp50 = queryEngine.runSQWRLQuery("Drivig50", "swrlb:greaterThanOrEqual(" + Driving + "," + 50 + ")^swrlb:lessThan(" + Driving + "," + 90 + ") -> sqwrl:select(" + Driving + ")");
//            SQWRLResult DrivingUp30 = queryEngine.runSQWRLQuery("Drivig30", "swrlb:greaterThanOrEqual(" + Driving + "," + 30 + ")^swrlb:lessThan(" + Driving + "," + 50 + ") -> sqwrl:select(" + Driving + ")");
//
//            SQWRLResult PVTGreat = queryEngine.runSQWRLQuery("PVTGreat", "swrlb:lessThanOrEqual(" + userData.getCurrentPVT() + "," + userData.getLowSleep() + ") -> sqwrl:select(" + userData.getLowSleep() + ")");
//            SQWRLResult PVTBad = queryEngine.runSQWRLQuery("PVTBad", "swrlb:greaterThanOrEqual(" + userData.getCurrentPVT() + "," + userData.getHighSleep() + ") -> sqwrl:select(" + userData.getHighSleep() + ")");
//            SQWRLResult PVTNormal = queryEngine.runSQWRLQuery("PVTNormal", "swrlb:greaterThan(" + userData.getCurrentPVT() + "," + userData.getLowSleep() + ")^swrlb:lessThan(" + userData.getCurrentPVT() + "," + userData.getHighSleep() + ")  -> sqwrl:select(" + userData.getCurrentPVT() + ")");
//
//            System.out.println("------------------------------------------------------- Driving -------------------------------------------------------");
//
//            // Process the SQWRL result
//            if (DrivingUp90.next()) {
//                //sent alert message
//
//                status = 1;
//                reason = info.DrivingOver90;
//                //TimeUnit.SECONDS.sleep(9000);
//                System.out.println("Driving90 Set:" + DrivingUp90);
//                System.out.println("\n31  Driver Situation:" + " " + reason + " , " + status);
//                userData.setStatus(status);
//                userData.setReasons(reason);
//
//                status = 0;
//                System.out.println("\n32  Driver Situation:" + " " + reason + " , " + status);
//
//                reason = reason + info.DrivingOver90;
//                status = 1;
//                userData.setStatus(status);
//                userData.setReasons(reason);
//                listener.SendAlert(userData.getAccount());
//                System.out.println("\n給Driver作測驗的時間");
//                if (Counter()) {
//                    //給Driver作測驗的時間
//                    System.out.println("\n再給Driver作測驗的時間");
//                    pvtNewst.GetNewPVT(userData);
//                    //TimeUnit.SECONDS.sleep(20);
//                    System.out.println("\nDriver測驗有過");
//
//                    if (PVTBad.next()) {
//                        System.out.println("\nDriver測驗未過");
//                        status = 0;
//                        reason += info.DrivingBan;
//                        System.out.println("\n7  Driver Situation:" + " " + reason + " , " + status);
//                        userData.setStatus(status);
//                        userData.setReasons(reason);
//                    } else {
//                        System.out.println("\nDriver測驗有過");
//                        status = 1;
//                        reason += info.PVTNormal;
//                        System.out.println("\n8  Driver Situation:" + " " + reason + " , " + status);
//                        userData.setStatus(status);
//                        userData.setReasons(reason);
//                    }
//                    //listener.SendMsg(userData.getAccount());
//                } else {
//                    System.out.println("\nDriver未測驗");
//                    status = 0;
//                    reason += info.DrivingBan;
//                    System.out.println("\n7-1  Driver Situation:" + " " + reason + " , " + status);
//                    userData.setStatus(status);
//                    userData.setReasons(reason);
//                }
//                System.out.println("\n8-1  Driver Situation:" + " " + reason + " , " + status);
//                listener.SendMsg(userData.getAccount());
//            } else if (DrivingUp50.next()) {
//                status = 1;
//                reason = info.DrivingOver30;
//                userData.setStatus(status);
//                userData.setReasons(reason);
//                System.out.println("Driving50 Set:" + DrivingUp50);
//                System.out.println("\n33  Driver Situation:" + " " + reason + " , " + status);
//
//            } else if (DrivingUp30.next()) {
//                System.out.println("Driving30 Set:" + DrivingUp30);
//                status = 2;
//                reason = info.DrivingOver30;
//                userData.setStatus(status);
//                userData.setReasons(reason);
//                System.out.println("\n34  Driver Situation:" + " " + reason + " , " + status);
//            } else {
//                status = 3;
//                reason = info.DrivingUnder30;
//                userData.setStatus(status);
//                userData.setReasons(reason);
//                System.out.println("\n35  Driver Situation:" + " " + reason + " , " + status);
//            }
//            System.out.println("\n\nAT LAST \n\nDriver Situation:" + " " + reason + " , " + status);
//        } catch (SWRLParseException e) {
//            System.err.println("Error parsing SWRL rule or SQWRL query: " + e.getMessage());
//            System.exit(-1);
//        } catch (SQWRLException e) {
//            System.err.println("Error running SWRL rule or SQWRL query: " + e.getMessage());
//            System.exit(-1);
//        } catch (RuntimeException e) {
//            System.err.println("Error starting application: " + e.getMessage());
//            System.exit(-1);
//        }
    }

    public static void RealTimeFatigue(OWLOntology ontology,USerInfo userData) throws ParseException, SQLException, SWRLParseException, SWRLParseException, SQWRLException {
        double score = 0.0;
        score = RealTime(userData);
        int status = 0;
        String reason = "";
        Info info = new Info();
        System.out.println("\n\n-----------------------------------------------------------------------------------走專注力回歸方程式-----------------------------------------------------------------------------------");

        try {
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++SWRL++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);
            //---------------------------------------------sleep condition---------------------------------------------
            SQWRLResult Great = queryEngine.runSQWRLQuery("RealTimeFatigueGreat", "swrlb:greaterThanOrEqual(" + score + "," + 80 + ") -> sqwrl:select(" + score + ")");
            SQWRLResult Normal = queryEngine.runSQWRLQuery("RealTimeFatigueNormal", "swrlb:greaterThanOrEqual(" + score + "," + 40 + ")^swrlb:lessThan(" + score + "," + 80 + ") -> sqwrl:select(" + score + ")");
            SQWRLResult Bad = queryEngine.runSQWRLQuery("RealTimeFatigueBad", "swrlb:greaterThanOrEqual(" + score + "," + 20 + ")^swrlb:lessThan(" + score + "," + 40 + ") -> sqwrl:select(" + score + ")");
            SQWRLResult Ban = queryEngine.runSQWRLQuery("RealTimeFatigueBan", "swrlb:greaterThanOrEqual(" + score + "," + 0 + ")^swrlb:lessThan(" + score + "," + 20 + ") -> sqwrl:select(" + score + ")");
            if (Great.next()) {
                reason = info.SleepBan;
                status = 3;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n1  Driver Situation:" + " " + reason + " , " + status);
            } else if (Normal.next()) {
                reason = info.SleepBan;
                status = 2;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n1  Driver Situation:" + " " + reason + " , " + status);
            } else if (Bad.next()) {
                reason = info.SleepBan;
                status = 1;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n1  Driver Situation:" + " " + reason + " , " + status);
            } else if (Ban.next()) {
                reason = info.SleepBan;
                status = 3;
                userData.setStatus(status);
                userData.setReasons(reason);
                System.out.println("\n1  Driver Situation:" + " " + reason + " , " + status);
            }

        } catch (SWRLParseException e) {
            System.err.println("Error parsing SWRL rule or SQWRL query: " + e.getMessage());
            System.exit(-1);
        } catch (SQWRLException e) {
            System.err.println("Error running SWRL rule or SQWRL query: " + e.getMessage());
            System.exit(-1);
        } catch (RuntimeException e) {
            System.err.println("Error starting application: " + e.getMessage());
            System.exit(-1);
        }
        System.out.println("\n\n-----------------------------------------------------------------------------------專注力回歸方程式結束-----------------------------------------------------------------------------------");

    }

    public static double RealTime(USerInfo userData) throws ParseException, SQLException {
        ZoneId TPE = ZoneId.of("Asia/Taipei");
        ZonedDateTime startInstant = Instant.now().atZone(TPE);
        String Account = userData.getAccount();
        double sleep3 = 100.0;
        double sleep5 = 100.0;
        double minFatigue = 100.0;
        double awake = 0.0;
        API api = new API();
        //GetDriver();
        System.out.print("\n=====================================ASDFERGFDFJHHRRSHEDFSXGGFSREESFDHFFFG=======================================\n" + startInstant + "\n");
        sleep3 = api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 5);
        System.out.print("\n=====================================TIME=======================================\n" + sleep3 + "\n");
        if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 2) == 0) {
            minFatigue = 0;
        }
        if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 1) == 0) {
            minFatigue = 20;
        }
        if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 5) >= 8) {
            sleep5 = 100 - 0.3057 * 5 - 0.7667;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 5) >= 6) {
            sleep5 = 100 - 0.7102 * 5 + 0.3054;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 5) >= 4) {
            sleep5 = 100 - 1.1479 * 5 - 0.0117;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 5) < 4) {
            sleep5 = 100 - 5.02 * 5 + 1.57;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 5) < 0) {
            sleep5 = 0;
        }

        if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 3) >= 8) {
            sleep3 = 100 - 0.3057 * 5 - 0.7667;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 3) >= 6) {
            sleep3 = 100 - 0.7102 * 5 + 0.3054;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 3) >= 4) {
            sleep3 = 100 - 1.1479 * 5 - 0.0117;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 3) < 4) {
            sleep3 = 100 - 5.02 * 5 + 1.57;
        } else if (api.GetAverageAsleepTime(Account, "2017-04-08T13:59:12.757Z", 3) < 0) {
            sleep3 = 0;
        }

        if (minFatigue != 0.0) {
            System.out.print("\n=====================================3333333333333333333333333======================================\n" + sleep3 + "\n");
            System.out.print("\n====================================555555555555555555555555555555====================================\n" + sleep5 + "\n");
            double temp = 100.0;
            if (sleep3 <= sleep5) {
                temp = sleep3;
            } else {
                temp = sleep5;
            }

            if (temp < minFatigue) {
                minFatigue = temp;
            }
        }
        System.out.print("\n=====================================ASDFERGFDFJHHRRSHEDFSXGGFSREESFDHFFFG=======================================\n" + minFatigue + "\n");

        String Date = startInstant.toString().substring(0, 10);
        String Time = startInstant.toString().substring(11, 19);
        awake = api.GetAwakeTime(Account, Date, "15:59:12");
//        awake = api.GetAwakeTime("yzufitbittest12@outlook.com","2017-04-08","15:59:12");
        System.out.print("\n=====================================ASDFERGFDFJHHRRSHEDFSXGGFSREESFDHFFFG=======================================\n" + "\n\n\n" + awake + "\n");
        double score = 0.0;
        score = myMATH.FocusMethod(minFatigue, awake);
        return score;
    }
    
     public static void DrivingTime(USerInfo userData) throws ParseException, SQLException, IOException, InterruptedException {
        FirebaseListener listener = new FirebaseListener();
        String CurrentTime = listener.GetDrivingHour();
        String CurrentAvt = listener.GetIsActiving();
        
        if(userData.getIsDriving().equals("A")&&CurrentAvt.equals("V")){
            userData.setDrivingHour(Integer.parseInt(CurrentTime)-Integer.parseInt(userData.getDrivingStart()));
            CurrentAvt = listener.GetIsActiving();
            while(CurrentAvt.equals("V")){
                userData.setIsDriving("V");
                CurrentAvt = listener.GetIsActiving();
            }
            userData.setIsDriving("A");
            userData.setDrivingStart(listener.GetDrivingHour());
        }
        else if(userData.getIsDriving().equals("A")&&CurrentAvt.equals("A")){
            userData.setDrivingHour(Integer.parseInt(CurrentTime)-Integer.parseInt(userData.getDrivingStart()));
             userData.setIsDriving("A");
            userData.setDrivingStart(listener.GetDrivingHour());
        }
        else if(userData.getIsDriving().equals("V")&&CurrentAvt.equals("A")){
            userData.setDrivingHour(Integer.parseInt(CurrentTime)-Integer.parseInt(userData.getDrivingStart()));
             userData.setIsDriving("A");
            userData.setDrivingStart(listener.GetDrivingHour());
        }
     }

    public static boolean Counter() throws IOException, FileNotFoundException, InterruptedException {
        String rs = FirebaseListener.GetMsg();
        for (int i = 12; i > 0; i--) {
            //FireBaseGetMsg();
            System.out.println("Hello world");
            TimeUnit.SECONDS.sleep(5);
            if (FirebaseListener.GetMsg().equals("true")) {
                return true;
            }
        }
        return false;
    }
}
