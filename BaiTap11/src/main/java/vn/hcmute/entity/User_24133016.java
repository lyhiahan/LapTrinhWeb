package vn.hcmute.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Users")
public class User_24133016 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Username", columnDefinition = "NVARCHAR(50)")
    private String username;

    @Column(name = "Password", columnDefinition = "NVARCHAR(50)")
    private String password;

    @Column(name = "Phone", columnDefinition = "NVARCHAR(15)")
    private String phone;

    @Column(name = "Fullname", columnDefinition = "NVARCHAR(50)")
    private String fullname;

    @Column(name = "Email", columnDefinition = "NVARCHAR(150)")
    private String email;

    @Column(name = "Admin")
    private Boolean admin;

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "Images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "otp", columnDefinition = "NVARCHAR(10)")
    private String otp;

    @Column(name = "otpExpiry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpExpiry;

    public User_24133016() {
    }

    public User_24133016(String username, String password, String phone, String fullname, String email, Boolean admin, Boolean active) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.fullname = fullname;
        this.email = email;
        this.admin = admin;
        this.active = active;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(Date otpExpiry) {
        this.otpExpiry = otpExpiry;
    }
}
