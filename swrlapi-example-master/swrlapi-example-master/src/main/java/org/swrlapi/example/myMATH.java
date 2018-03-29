/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Vector;
/**
 *
 * @author hp
 */
public class myMATH {
        static double mu = 0.0;
        static double theta = 0.0;
        static double LowStand = 0.0; 
        static double HighStand = 0.0;
        
    public static double Mu(Vector<Double> originArray){
        double mu = 0.0;
        double sum = 0.0;
        Vector<Double> muArray = new Vector<Double>();
        muArray = originArray;
        for(Double i :muArray)
            sum+=i;
        System.out.print(sum);
        return sum/muArray.size();
    } 
    public static double Theta(double mu ,Vector<Double> originArray){

//        mu =  Mu(originArray);
        double sum = 0;
//        System.out.print("____________________________________________________________");
        Vector<Double> thetaArray = new Vector<Double>();
        thetaArray = originArray;
        for(Double i :thetaArray)
        {
            sum+=Math.pow((i-mu),2);
        }
        //theta = sum / countArray.length;
        return Math.sqrt(sum/thetaArray.size());
    } 
    
    public static Vector<Double> standardization(Vector<Double> originArray){

        mu =  Mu(originArray);
        double sum = 0;
        Vector<Double> standArray = new Vector<Double>();
        theta = Theta(mu ,originArray);
        for(Double i :originArray){
            standArray.add(((i-mu)/theta));
        }
//         for(int i=0 ; i <standArray.size();i++){
//            System.out.print("\nstandardization : "+standArray.get(i));
//        }
        //theta = sum / countArray.length;
        return standArray;
    }
    
    public static double LowData(Vector<Double> originArray){
        
        originArray = standardization(originArray);
        LowStand = originArray.get(originArray.size()-1);
        return LowStand;
    }
     public static double HighData(Vector<Double> originArray){
         originArray = standardization(originArray);
         HighStand = originArray.get(0);
        return HighStand;
    }
     
     public static double FocusMethod(double BaseFatigue,double awakeDuration){
         double AVR,AVR24;
         double score = 0.0; 
         AVR = 0.0072*Math.pow(awakeDuration, 3) - 0.2171*Math.pow(awakeDuration, 2) + 2.3509*awakeDuration - 1.1484;
         AVR24 = 0.0072*Math.pow(24, 3) - 0.2171*Math.pow(24, 2) + 2.3509*24 - 1.1484;
         System.out.print("\n=====================================AAAAAAAAAAAAAVVVVVVVVVVVVVVVVVVVVVRRRRRRRRRRRRRRRR=======================================\n" +"\n\n\n"+AVR+ "\n");
System.out.print("\n=====================================22222222222222222222222222222224444444444444444444444444444444=======================================\n"+AVR24+ "\n");
         return (AVR/AVR24)*100;
     }
}