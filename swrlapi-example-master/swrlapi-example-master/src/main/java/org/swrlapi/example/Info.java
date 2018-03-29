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

/**
 *
 * @author hp
 */
public class Info {

    boolean trigger;
    boolean isTest;
    String User;
    String Activity = "Last night activities were frequent,";
    String SleepGreat = "Last time sleep efficiency greate,";
    String SleepNormal = "Last time sleep efficiency normal,";
    String SleepBan = "Last time sleep efficiency terrible,driver can not drivers";
    String PVTGreat = "Last time PVT test performed great,";
    String PVTNormal = "Last time PVT test performed normal,";
    String PVTBad = "Last time PVT test performed bad , the driver is not suitable for driving,";
    String PVTBan = "Time's up , did not do PVT Test";
    
    String DrivingOver90 = "Driving time over 90 minutes , the driver is not suitable for driving";
    String DrivingOver50 = "Driving time over 50 minutes ";
    String DrivingOver30 = "Driving time over 30 minutes";
    String DrivingUnder30 = "Driving time under 30 minutes";
    String DrivingBan = "Driving over 90 min , and PVT didnot pass";
    String PVTAlert = "Please do PVT TEST in 15 minutes, or we'll forbid your driving force";

    public Info() {
    }

    public Info(boolean isTest ,boolean trigger, String User) {
        this.isTest = isTest;
        this.trigger = trigger;
        this.User = User;
    }

    public boolean isIsTest() {
        return isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }
    
    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }

}
