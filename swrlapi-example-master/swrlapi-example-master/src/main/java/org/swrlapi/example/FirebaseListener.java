/*
 * Copyright (c) 2017, hp
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.swrlapi.example;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import static org.swrlapi.example.Watchmen.userData;

/**
 *
 * @author hp
 */
public class FirebaseListener {

    static DatabaseReference ref;
    static FirebaseDatabase database;

    public static void SendAlert(String Account) throws FileNotFoundException, IOException, InterruptedException {
        System.out.println("\n\n\nSendAlert" + Account + "\n\n\n\n");
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        System.out.println("\n11111111111111111111111111111111111111111111");
        ref = fdb.getReference("alert");
        System.out.println("\n222222222222222222222");
        ref.setValue(new Info(false, true, Account));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("\n3333333333333333333333333");
        System.out.println("\n\n\n-----------------------------END-------------------------------------\n\n\n\n");
    }

    public static void SendMsg(String Account) throws FileNotFoundException, IOException, InterruptedException {
        System.out.println("\n\n\n SendMsg" + Account + "\n\n\n\n");
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        System.out.println("\n4444444444444444444444444444444444444444");
        ref = fdb.getReference("alert");
        System.out.println("\n55555555555555555555555555555555555555555555");
        ref.setValue(new Info(false, false, Account));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("\n6666666666666666666666666666666666666666666");
        System.out.println("\n\n\n-----------------------------SEND MSG END-------------------------------------\n\n\n\n");
    }
    static String Result = "";

    //Trigger 有沒有觸發 alert
    public static String GetMsg() throws FileNotFoundException, IOException, InterruptedException {
        String args[] = null;
        database = FirebaseDatabase.getInstance();

        Semaphore sh = new Semaphore(0);

        // Get a reference to our posts
        System.out.println("\nFIREBASEGetMSG" + "\n");

        // Attach a listener to read the data at our posts reference
        database
                .getReference("alert")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                         Result = "false";
                        Map<String, Object> isTest = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println("Here is FirebaseListener , and trigger value is " + isTest.get("trigger"));
                        Result = isTest.get("trigger").toString();
                        sh.release();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
        sh.acquire();
        return Result;
    }
    //是否有完成測驗
     public static String GetMsgFromWeb() throws FileNotFoundException, IOException, InterruptedException {
        database = FirebaseDatabase.getInstance();
        //Result = "false";   
        Semaphore sh = new Semaphore(0);

        // Get a reference to our posts
        System.out.println("\n Is Test?" + "\n");

        // Attach a listener to read the data at our posts reference
        database
                .getReference("alert")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Result = "false";
                        Map<String, Object> isTest = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println("Here is FirebaseListene GetMsgFromWeb , and isTest value is " + isTest.get("isTest"));
                        Result = isTest.get("isTest").toString();
                        if(Result.equals("null"))
                            Result = "false";
                        sh.release();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
        sh.acquire();
        return Result;
    }
     
     public static String GetDrivingHour() throws FileNotFoundException, IOException, InterruptedException {
        database = FirebaseDatabase.getInstance();
        //Result = "false";   
        Semaphore sh = new Semaphore(0);

        // Get a reference to our posts
        System.out.println("\n GetDrivingHour" + "\n");

        // Attach a listener to read the data at our posts reference
        database
                .getReference("DrivingInfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Result = "false";
                        Map<String, Object> isTest = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println("Here is FirebaseListene GetDrivingHour , and trigger value is" + isTest.get("DeltTime"));
                        Result = isTest.get("DeltTime").toString();
                        sh.release();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
        sh.acquire();
        return Result;
    }
     
     public static String GetIsActiving() throws FileNotFoundException, IOException, InterruptedException {
        database = FirebaseDatabase.getInstance();
        //Result = "false";   
        Semaphore sh = new Semaphore(0);

        // Get a reference to our posts
        System.out.println("\n GetDrivingHour" + "\n");

        // Attach a listener to read the data at our posts reference
        database
                .getReference("DrivingInfo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Result = "false";
                        Map<String, Object> isTest = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println("Here is FirebaseListene GetDrivingHour , and trigger value is" + isTest.get("isActivity"));
                        Result = isTest.get("isActivity").toString();
                        System.out.println(Result);
                        sh.release();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
        sh.acquire();
        return Result;
    }
}
