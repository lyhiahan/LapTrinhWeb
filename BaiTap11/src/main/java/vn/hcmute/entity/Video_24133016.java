package vn.hcmute.entity;

import java.io.Serializable;
import java.util.List;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Videos")
public class Video_24133016 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "VideoId", columnDefinition = "NVARCHAR(50)")
    private String videoId;

    @Column(name = "Title", columnDefinition = "NVARCHAR(200)")
    private String title;

    @Column(name = "Poster", columnDefinition = "NVARCHAR(500)")
    private String poster;

    @Column(name = "Views")
    private Integer views;

    @Column(name = "Description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "Price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "Stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category_24133016 category;

    @OneToMany(mappedBy = "video")
    private List<Share_24133016> shares;

    @OneToMany(mappedBy = "video")
    private List<Favorite_24133016> favorites;

    public Video_24133016() {
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Category_24133016 getCategory() {
        return category;
    }

    public void setCategory(Category_24133016 category) {
        this.category = category;
    }

    public List<Share_24133016> getShares() {
        return shares;
    }

    public void setShares(List<Share_24133016> shares) {
        this.shares = shares;
    }

    public List<Favorite_24133016> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite_24133016> favorites) {
        this.favorites = favorites;
    }
}
