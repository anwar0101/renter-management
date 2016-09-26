/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.model;

import java.sql.Date;

/**
 *
 * @author anwar
 */
public class Bill {
    
    //String accounts = "create table bill (b_month date not null, r_id int not null, r_name varchar(20),r_houserent int, b_paid int, b_due int, b_update date)";
    
    private Date b_month;
    private int r_id;
    private String r_name;
    private int r_houserent;
    private int b_paid;
    private int b_due;
    private Date b_update;

    public Bill(Date b_month, int r_id, String r_name, int r_houserent, int b_paid, int b_due, Date b_update) {
        this.b_month = b_month;
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_houserent = r_houserent;
        this.b_paid = b_paid;
        this.b_due = b_due;
        this.b_update = b_update;
    }

    public Date getB_month() {
        return b_month;
    }

    public void setB_month(Date b_month) {
        this.b_month = b_month;
    }

    public int getR_id() {
        return r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public int getR_houserent() {
        return r_houserent;
    }

    public void setR_houserent(int r_houserent) {
        this.r_houserent = r_houserent;
    }

    public int getB_paid() {
        return b_paid;
    }

    public void setB_paid(int b_paid) {
        this.b_paid = b_paid;
    }

    public int getB_due() {
        return b_due;
    }

    public void setB_due(int b_due) {
        this.b_due = b_due;
    }

    public Date getB_update() {
        return b_update;
    }

    public void setB_update(Date b_update) {
        this.b_update = b_update;
    }
}
