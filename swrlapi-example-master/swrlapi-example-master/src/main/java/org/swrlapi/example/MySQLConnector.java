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
import java.util.Vector;


/**
 *
 * @author LU SIN EN
 */
public class MySQLConnector {
    //login

    private config con = new config(); //configulation object

    String connStr = null;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    ResultSetMetaData metaData = null;
    StringBuffer ResultSB = new StringBuffer();

    public MySQLConnector(String inHost, String inAcc, String inPW, String inDB) {

        con.setDBname(inDB);
        con.setPwStr(inPW);
        con.setUrlstr(inHost);
        con.setUserStr(inAcc);
    }

    public static void insertdata(StringBuffer sb, config c) throws SQLException {
        sb.append(";");
        System.out.println(sb);
        MySQLConnector sleep_sql = new MySQLConnector(c.getUrlstr(), c.getUserStr(), c.getPwStr(), c.getDBname());
        sleep_sql.connectDB();
        sleep_sql.updateQuery(sb.toString());
        sleep_sql.close();

    }
public static void InsertManySQL(Vector<String> sb, config c) throws SQLException {//insert data to db
        
        MySQLConnector sleep_sql = new MySQLConnector(c.getUrlstr(), c.getUserStr(), c.getPwStr(), c.getDBname());
        System.out.println(sb);
        sleep_sql.connectDB();
        sleep_sql.insert(sb, c);
        sleep_sql.close();

    }

public  void insert(Vector<String> sb, config c) throws SQLException {//insert data to db
        
try{

    Statement st = conn.createStatement();
    System.out.println(sb.size());
    for (int i = 0; i < sb.size(); i++) {
        st.addBatch(sb.get(i));
    }
    st.executeBatch();
    st.close();
    }
catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    public String[] getdata(String query, String ans) throws SQLException {
        System.out.println(query);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        int size = 0;
        while (rs.next()) {
            size++;
        }
        rs.first();
        String[] data = new String[size];
        try {
            for (int i = 0; i < size; i++) {

                data[i] = rs.getString(ans);
                rs.next();

            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        st.close();
        return data;

    }

    public String[][] SelectAllData(String query) throws SQLException {
        System.out.println(query);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        metaData = rs.getMetaData(); //取出meta data
        int numCol = metaData.getColumnCount();
        int size = 0;
        while (rs.next()) {
            size++;
        }
        System.out.println("SIZE: " + size);
        System.out.println("numCol: " + numCol);
        rs.first();
        String[][] data = new String[size + 1][numCol];
        for (int i = 1; i <= numCol; i++) {
            data[0][i - 1] = metaData.getColumnName(i);
        }
        try {
            for (int i = 1; i < size + 1; i++) {
                for (int j = 0; j < numCol; j++) {
                    if (rs.getObject(j+1) == null) {
                        data[i][j] = "null";
                    } else {
                        data[i][j] = rs.getObject(j + 1).toString();
                    }
                }
                rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        st.close();
        return data;

    }

    public int connectDB() {
        try {
            //conn = (Connection) DriverManager.getConnection("jdbc:mysql://140.138.77.109/javaclass?user=root&password=tear040MARC668");
            connStr = "jdbc:mysql://" + con.getUrlstr() + "/" + con.getDBname() + "?user=" + con.getUserStr() + "&password=" + con.getPwStr();
            conn = (Connection) DriverManager.getConnection(connStr);
            //   conn.close();
            //System.out.println("Databases connected!");
            System.out.println("uniplat Databases connected!");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return -1;  //若錯誤回傳-1
        }

        return 1;

    }

    public void doQuery(String SQLstr) {
        try {

            String SQL = SQLstr;

            //清空result
            ResultSB.delete(0, ResultSB.length());
            System.out.println(SQL);
            stmt = (Statement) conn.createStatement();
            rs = stmt.executeQuery(SQL);

            metaData = rs.getMetaData(); //取出meta data
            int numCol = metaData.getColumnCount();

            for (int i = 1; i <= numCol; i++) {
                System.out.print("\t" + metaData.getColumnName(i));
                ResultSB.append("\t" + metaData.getColumnName(i));
            }
            System.out.println();
            ResultSB.append("\n");

            //print out detail
            while (rs.next()) {
                for (int i = 1; i <= numCol; i++) {
                    System.out.print("\t" + rs.getObject(i));
                    ResultSB.append("\t" + rs.getObject(i));
                }
                System.out.println();
                ResultSB.append("\n");
                //System.out.println(rs.getString(3) + ", ");
            }
            rs.close();
            stmt.close();
            conn.close();

            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {

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

    public void updateQuery(String SQLstr) {
        try {

            String SQL = SQLstr;
            stmt = (Statement) conn.createStatement();

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

    public void close() throws SQLException {
        conn.close();
    }
}
