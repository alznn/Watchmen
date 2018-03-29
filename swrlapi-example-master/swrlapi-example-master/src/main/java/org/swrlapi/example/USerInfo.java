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

import java.time.Instant;
import java.util.Date;

/**
 *
 * @author hp
 */
public class USerInfo {

    private String Account;
    private double currentPVT;
    private double HighPVT;
    private double LowPVT;
    private double currentSleep;
    private double HighSleep;
    private double LowSleep;
    private int DrivingHour;
    private int status;
    private String reasons;
    private String starWork;
    private String endWork;
    private String isDriving;//A or V
    private String DrivingStart;//start driving time
    private String DrivingEnd;

    public String getIsDriving() {
        return isDriving;
    }

    public void setIsDriving(String isDriving) {
        this.isDriving = isDriving;
    }

    public String getDrivingStart() {
        return DrivingStart;
    }

    public void setDrivingStart(String DrivingStart) {
        this.DrivingStart = DrivingStart;
    }

    public String getDrivingEnd() {
        return DrivingEnd;
    }

    public void setDrivingEnd(String DrivingEnd) {
        this.DrivingEnd = DrivingEnd;
    }

    public String getStarWork() {
        return starWork;
    }

    public void setStarWork(String starWork) {
        this.starWork = starWork;
    }

    public String getEndWork() {
        return endWork;
    }

    public void setEndWork(String endWork) {
        this.endWork = endWork;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public double getCurrentPVT() {
        return currentPVT;
    }

    public void setCurrentPVT(double currentPVT) {
        this.currentPVT = currentPVT;
    }

    public double getHighPVT() {
        return HighPVT;
    }

    public void setHighPVT(double HighPVT) {
        this.HighPVT = HighPVT;
    }

    public double getLowPVT() {
        return LowPVT;
    }

    public void setLowPVT(double LowPVT) {
        this.LowPVT = LowPVT;
    }

    public double getCurrentSleep() {
        return currentSleep;
    }

    public void setCurrentSleep(double currentSleep) {
        this.currentSleep = currentSleep;
    }

    public double getHighSleep() {
        return HighSleep;
    }

    public void setHighSleep(double HighSleep) {
        this.HighSleep = HighSleep;
    }

    public double getLowSleep() {
        return LowSleep;
    }

    public void setLowSleep(double LowSleep) {
        this.LowSleep = LowSleep;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDrivingHour() {
        return DrivingHour;
    }

    public void setDrivingHour(int DrivingHour) {
        this.DrivingHour = DrivingHour;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

}
