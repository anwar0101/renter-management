/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.model;

/**
 *
 * @author anwar
 */
public class User {
    
    //create table: create table app.admin (id int not null,username varchar(20) not null, password varchar(20), fullname varchar(20), image blob)
    
    private String username;
    private String password;
    private String fullname;
    private int id;
    private byte[] image;
    
    private User() {
    }
    
    public static User getInstance() {
        return UserHolder.INSTANCE;
    }
    
    private static class UserHolder {
        private static final User INSTANCE = new User();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    
    
}
