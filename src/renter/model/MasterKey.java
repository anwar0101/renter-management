/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.model;

import java.sql.Date;

/**
 *
 * @author AnWaR0x07
 */
public class MasterKey {
    //create table app.masterkey (masterkey varchar(20) not null, masterto date, masterfrom date, islock boolean)
    private String masterkey;
    private Date masterto;
    private Date masterfrom;
    private Boolean islock;

    public MasterKey(String masterkey, Date masterto, Date masterfrom, Boolean islock) {
        this.masterkey = masterkey;
        this.masterto = masterto;
        this.masterfrom = masterfrom;
        this.islock = islock;
    }

    public String getMasterkey() {
        return masterkey;
    }

    public void setMasterkey(String masterkey) {
        this.masterkey = masterkey;
    }

    public Date getMasterto() {
        return masterto;
    }

    public void setMasterto(Date masterto) {
        this.masterto = masterto;
    }

    public Date getMasterfrom() {
        return masterfrom;
    }

    public void setMasterfrom(Date masterfrom) {
        this.masterfrom = masterfrom;
    }

    public Boolean getIslock() {
        return islock;
    }

    public void setIslock(Boolean islock) {
        this.islock = islock;
    }
    
}
