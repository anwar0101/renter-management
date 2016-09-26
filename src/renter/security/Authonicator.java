/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import renter.db.DBConn;
import renter.model.User;

/**
 *
 * @author anwar
 */
public class Authonicator {

    private static final Map<String, String> USERS = new HashMap<>();
    private final Sql2o sql2o;
    
    public Authonicator(String username, String password){
        sql2o = new Sql2o(DBConn.DBURL, DBConn.USERNAME, DBConn.PASSWORD);
        if(getPassword(username).equals(password)){
            USERS.put(username, password);
            System.out.println("password: " + getPassword(username));
        }
    }
    
    private List<User> getAll(String username){
        String sql = "SELECT password, image FROM app.admin where username=:username";
        User u = User.getInstance();
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("username", username)
                    .bind(u).executeAndFetch(User.class);
        }
    }
    
    private String getPassword(String username) {
        String result;
        List<User> user = getAll(username);
        User u = User.getInstance();
        u.setImage(user.get(0).getImage());
        System.out.println("bytes:" + u.getImage());
        return user.get(0).getPassword();
    }

    public static boolean validate(String user, String password) {
        String validUserPassword = USERS.get(user);
        return validUserPassword != null && validUserPassword.equals(password);
    }
}
