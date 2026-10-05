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
@Table(name = "Favorites")
public class Favorite_24133016 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FavoriteId")
    private int favoriteId;

    @Column(name = "LikedDate")
    @Temporal(TemporalType.DATE)
    private Date likedDate;

    @ManyToOne
    @JoinColumn(name = "VideoId")
    private Video_24133016 video;

    @ManyToOne
    @JoinColumn(name = "Username")
    private User_24133016 user;

    public Favorite_24133016() {
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Date getLikedDate() {
        return likedDate;
    }

    public void setLikedDate(Date likedDate) {
        this.likedDate = likedDate;
    }

    public Video_24133016 getVideo() {
        return video;
    }

    public void setVideo(Video_24133016 video) {
        this.video = video;
    }

    public User_24133016 getUser() {
        return user;
    }

    public void setUser(User_24133016 user) {
        this.user = user;
    }
}
