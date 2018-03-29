/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;

import java.util.Calendar;

/**
 *
 * @author hp
 */
public class GetTime {
  
    public static String getDateTime() {  //無參數=傳回現在時間
        Calendar c=Calendar.getInstance();
        int[] a={c.get(Calendar.YEAR),
             c.get(Calendar.MONTH),
             c.get(Calendar.DAY_OF_MONTH),
             c.get(Calendar.HOUR_OF_DAY),
             c.get(Calendar.MINUTE),
             c.get(Calendar.SECOND)
             };
    
        StringBuffer sb=new StringBuffer();
        sb.append(a[0]);
        if (a[1]<9) {sb.append("-0" + (a[1] + 1));}   //加 1 才會得到實際月份
        else {sb.append("-" + (a[1] + 1));}
        if (a[2]<10) {sb.append("-0" + (a[2]));}
        else {sb.append("-" + (a[2]));}
        if (a[3]<10) {sb.append(" 0" + (a[3]));}
        else {sb.append(" " + (a[3]));}
        if (a[4]<10) {sb.append(":0" + a[4]);}
        else {sb.append(":" + a[4]);}
        if (a[5]<10) {sb.append(":0" + a[5]);}
        else {sb.append(":" + a[5]);}
    
         return sb.toString();
    }

    String getDateTime(int i, int i0, int i1, int i2, int i3, int i4) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
