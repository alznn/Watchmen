/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;

import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author hp
 */
public class IsActivity { static Vector bad_status = new Vector();
    static int myeffi[]; //= {67,48,36,52,98,94,96,93,96,39,74,100,98,94,19,95,79,96,97,97,97,96,98,100};
    static String effi[];
    static int currentEffi = 0;

    static public int LowSleep(String Account) {
        DatabaseConnector sql = new DatabaseConnector("173.194.252.196", "alznn", "tear040MARC668", "uniplat");
        sql.connectDB();
        
        //last 10 %
        sql.doQuery(String.format("SELECT ID_Activity FROM Summary WHERE User_ID='%s' and ID_Activity = 'STEPS' and Value > '0' ORDER BY StartTime DESC;)", Account));
        StringBuffer rs = sql.getResultString();
        String str;
        str = rs.toString();
        effi = str.split("\n");
        for (int i = 0; i < effi.length*0.1; i++) {
            myeffi[i] = Integer.parseInt(effi[i]);
        }
        
        //newst
        sql.doQuery(String.format("SELECT ID_Activity FROM Summary WHERE User_ID='%s' and ID_Activity = 'efficiency' ORDER BY StartTime DESC;)", Account));
        rs = sql.getResultString();
        str = rs.toString();
        effi = str.split("\n");
        currentEffi = Integer.parseInt(effi[0]);
        return currentEffi;
    }

    public Boolean isMove(String Account) throws SQLException {  //compare bater or worst

        
        for (int i = 0; i < myeffi.length; i++) {
            if (currentEffi > myeffi[i]) {
                return true;
            }
        }
        return false;
    }
}