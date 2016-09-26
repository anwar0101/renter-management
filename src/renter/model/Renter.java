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
public class Renter {

    private String createTable = 
            "create table app.renter(r_id int NOT NULL, r_name varchar(20) NOT NULL, r_father varchar(20), r_address varchar(100), r_phoneno varchar(20),r_email varchar(20), r_houseno varchar(20), r_houserent int not null, r_nid varchar(20), r_passport varchar(20), r_ref varchar(20), r_photo blob, is_active boolean)";
  
            // r_id,r_name,r_father,r_address,r_phoneno,r_email,r_houseno,r_houserent,r_nid,r_passport,r_ref,r_photo,is_active
    /**
     *
     */

    private int r_id;
    private String r_name;
    private String r_father;
    private String r_address;
    private String r_phoneno;
    private String r_email;
    private String r_nid;
    private String r_passport;
    private String r_ref;
    private String r_houseno;
    private int r_houserent;
    
    private byte[] r_photo;
    private Boolean is_active;
    
    
//    public void insertImage() throws SQLException, IOException{
//        Blob blob = connection.conn.createBlob();
//        ObjectOutputStream oos;
//        oos = new ObjectOutputStream(blob.setBinaryStream(1));
//        
//        oos.writeObject(new Image("usericon.png"));
//        oos.close();
//        
//        blob.free();
//    }
//    public void getImage() throws SQLException, IOException{
//        Blob photo = rs.getBlob(1);
//      ObjectInputStream ois = null;
//      ois = new ObjectInputStream(photo.getBinaryStream());
//      image = (ImageIcon) ois.readObject();
//    }

    public Renter(int r_id, String r_name, String r_father, String r_address, String r_phoneno, String r_email, String r_nid, String r_passport, String r_ref, String r_houseno, int r_houserent, byte[] r_photo, Boolean is_active) {
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_father = r_father;
        this.r_address = r_address;
        this.r_phoneno = r_phoneno;
        this.r_email = r_email;
        this.r_nid = r_nid;
        this.r_passport = r_passport;
        this.r_ref = r_ref;
        this.r_houseno = r_houseno;
        this.r_houserent = r_houserent;
        
        this.r_photo = r_photo;
        this.is_active = is_active;
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

    public String getR_father() {
        return r_father;
    }

    public void setR_father(String r_father) {
        this.r_father = r_father;
    }

    public String getR_address() {
        return r_address;
    }

    public void setR_address(String r_address) {
        this.r_address = r_address;
    }

    public String getR_phoneno() {
        return r_phoneno;
    }

    public void setR_phoneno(String r_phoneno) {
        this.r_phoneno = r_phoneno;
    }

    public String getR_email() {
        return r_email;
    }

    public void setR_email(String r_email) {
        this.r_email = r_email;
    }

    public String getR_nid() {
        return r_nid;
    }

    public void setR_nid(String r_nid) {
        this.r_nid = r_nid;
    }

    public String getR_passport() {
        return r_passport;
    }

    public void setR_passport(String r_passport) {
        this.r_passport = r_passport;
    }

    public String getR_ref() {
        return r_ref;
    }

    public void setR_ref(String r_ref) {
        this.r_ref = r_ref;
    }

    public String getR_houseno() {
        return r_houseno;
    }

    public void setR_houseno(String r_houseno) {
        this.r_houseno = r_houseno;
    }

    public int getR_houserent() {
        return r_houserent;
    }

    public void setR_houserent(int r_houserent) {
        this.r_houserent = r_houserent;
    }

    public byte[] getR_photo() {
        return r_photo;
    }

    public void setR_photo(byte[] r_photo) {
        this.r_photo = r_photo;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }
    
}
