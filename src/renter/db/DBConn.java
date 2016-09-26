/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.db;

/**
 *
 * @author anwar
 */
public class DBConn {
    public static String DBURL = "jdbc:derby:database";
    public static String DRIVERNAME = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String USERNAME = "anwar";
    public static String PASSWORD="anwar";
    
    public static void setDBUrl(String dburl){
        DBConn.DBURL = dburl;
    }
}
