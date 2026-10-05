package vn.hcmute.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Category")
public class Category_24133016 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    private int categoryId;

    @Column(name = "Categoryname", columnDefinition = "NVARCHAR(100)")
    private String categoryname;

    @Column(name = "Categorycode", columnDefinition = "NVARCHAR(100)")
    private String categorycode;

    @Column(name = "Images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "Status")
    private Boolean status;

    @OneToMany(mappedBy = "category")
    private List<Video_24133016> videos;

    public Category_24133016() {
    }

    public Category_24133016(int categoryId, String categoryname, String categorycode, String images, Boolean status) {
        this.categoryId = categoryId;
        this.categoryname = categoryname;
        this.categorycode = categorycode;
        this.images = images;
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategorycode() {
        return categorycode;
    }

    public void setCategorycode(String categorycode) {
        this.categorycode = categorycode;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Video_24133016> getVideos() {
        return videos;
    }

    public void setVideos(List<Video_24133016> videos) {
        this.videos = videos;
    }
}
