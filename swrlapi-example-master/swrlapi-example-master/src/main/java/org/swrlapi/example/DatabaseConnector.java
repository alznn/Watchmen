/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swrlapi.example;
import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author hp
 */
public class DatabaseConnector {
     private DatabaseConfig con = new DatabaseConfig(); //configulation object


    String connStr = null;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    ResultSetMetaData metaData = null;
    StringBuffer ResultSB = new StringBuffer();
    

  //  public static void main(String[] args) throws SQLException {
    //    mySQL_queryier queryier = new mySQL_queryier("173.194.252.196", "big", "bigteampw", "javaclass");
      //  queryier.connect();
     //   System.out.println(queryier.exec("SELECT * FROM pan_pet WHERE"));
       // queryier.disconnect();
    //}
    public DatabaseConnector(String inHost, String inAcc, String inPW , String inDB) {
        con.setDBname(inDB);
        con.setPwStr(inPW);
        con.setUrlstr(inHost);
        con.setUserStr(inAcc);
    }

    public int connectDB() {
        try {
            // conn = (Connection) DriverManager.getConnection("jdbc:mysql://140.138.77.109/javaclass?user=root&password=tear040MARC668");
            connStr = "jdbc:mysql://" + con.getUrlstr() + "/" + con.getDBname() + "?user=" + con.getUserStr() + "&password=" + con.getPwStr()+"";
            System.out.println("Result ~~~~ "+connStr);
            conn = (Connection) DriverManager.getConnection(connStr);
            // conn.close();
            System.out.println("Databases connected!");
        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return -1;  //若錯誤回傳-1
        }

        return 1;

    }
    //各種token
    public void doQuery_token(){}
    
    

    //資料存取
    public  void doQuery(String SQLstr) {
        try {

            String SQL = SQLstr;
           
            //清空result
            ResultSB.delete(0, ResultSB.length());
            
            stmt = (Statement) conn.createStatement();            
            
            rs = stmt.executeQuery(SQL);
                        
            metaData = rs.getMetaData(); //取出meta data
            int numCol = metaData.getColumnCount();

            for (int i = 1; i <= numCol; i++) {
        //       System.out.print("\t" + metaData.getColumnName(i));
                ResultSB.append("\t" + metaData.getColumnName(i));
            }
            System.out.println();
            ResultSB.append("\n");

            //print out detail
            while (rs.next()) {
                for (int i = 1; i <= numCol; i++) {
              //      System.out.print("\t" + rs.getObject(i));
                    ResultSB.append("\t" + rs.getObject(i));
                }
             //   System.out.println();
                ResultSB.append("\n");
                //System.out.println(rs.getString(3) + ", ");
            }
            rs.close();
            stmt.close();
            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }
        
   }
    
    public  void updateQuery(String SQLstr) {
        try {

           String SQL = SQLstr;
          stmt =(Statement) conn.createStatement();

            //清空result
            ResultSB.delete(0, ResultSB.length());
           // int resultset;
            stmt.executeUpdate(SQL);
            stmt.close();
            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed
            if (stmt != null) {
                try {
                    stmt.close();
              //      conn.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }
        
   }

    public StringBuffer getResultString() {
        return this.ResultSB;
    }
/*
    public ResultSetMetaData getMetaData() {
        return this.metaData;
    }

    public ResultSet getResultSet() {
        return this.rs;
    }
*/ 
    public void close() throws SQLException{
        conn.close();
    } 
}