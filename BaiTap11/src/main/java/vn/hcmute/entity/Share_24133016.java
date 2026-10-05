package vn.hcmute.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Shares")
public class Share_24133016 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShareId")
    private int shareId;

    @Column(name = "Emails", columnDefinition = "NVARCHAR(50)")
    private String emails;

    @Column(name = "SharedDate")
    @Temporal(TemporalType.DATE)
    private Date sharedDate;

    @ManyToOne
    @JoinColumn(name = "Username")
    private User_24133016 user;

    @ManyToOne
    @JoinColumn(name = "VideoId")
    private Video_24133016 video;

    public Share_24133016() {
    }

    public int getShareId() {
        return shareId;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public Date getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(Date sharedDate) {
        this.sharedDate = sharedDate;
    }

    public User_24133016 getUser() {
        return user;
    }

    public void setUser(User_24133016 user) {
        this.user = user;
    }

    public Video_24133016 getVideo() {
        return video;
    }

    public void setVideo(Video_24133016 video) {
        this.video = video;
    }
}
