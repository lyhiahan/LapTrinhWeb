package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int id;

    @Column(name = "cate_name", columnDefinition = "NVARCHAR(255) NOT NULL")
    private String name;

    @Column(name = "icons", columnDefinition = "NVARCHAR(255) NULL")
    private String icon;

    @Column(name = "price")
    private double price;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(int id, String name, String icon, double price) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getAutoIcon() {
        if (name == null) {
            return "fas fa-layer-group";
        }
        String lower = name.toLowerCase();
        if (lower.contains("trang sức") || lower.contains("nhẫn") || lower.contains("vòng")
                || lower.contains("dây chuyền") || lower.contains("kim cương")) {
            return "fas fa-gem text-warning";
        }
        if (lower.contains("giày") || lower.contains("dép") || lower.contains("sandal") || lower.contains("sneaker")) {
            return "fas fa-shoe-prints text-danger";
        }
        if (lower.contains("điện thoại") || lower.contains("phone") || lower.contains("smartphone")
                || lower.contains("mobile")) {
            return "fas fa-mobile-screen-button text-primary";
        }
        if (lower.contains("laptop") || lower.contains("máy tính") || lower.contains("pc")
                || lower.contains("macbook")) {
            return "fas fa-laptop text-info";
        }
        if (lower.contains("quần") || lower.contains("jean") || lower.contains("kaki") || lower.contains("trousers")
                || lower.contains("pants") || lower.contains("short")) {
            return "fa-pants text-primary";
        }
        if (lower.contains("áo") || lower.contains("ao ") || lower.startsWith("ao") || lower.contains("thời trang")
                || lower.contains("váy") || lower.contains("đầm")) {
            return "fas fa-shirt text-success";
        }
        if (lower.contains("đồng hồ") || lower.contains("watch")) {
            return "fas fa-clock text-primary";
        }
        if (lower.contains("âm thanh") || lower.contains("tai nghe") || lower.contains("loa")) {
            return "fas fa-headphones text-dark";
        }
        if (lower.contains("máy ảnh") || lower.contains("camera")) {
            return "fas fa-camera text-info";
        }
        if (lower.contains("mỹ phẩm") || lower.contains("làm đẹp") || lower.contains("son") || lower.contains("phấn")) {
            return "fas fa-wand-magic-sparkles text-danger";
        }
        if (lower.contains("gia dụng") || lower.contains("nhà cửa") || lower.contains("nội thất")) {
            return "fas fa-couch text-secondary";
        }
        if (lower.contains("sách") || lower.contains("truyện") || lower.contains("văn phòng")) {
            return "fas fa-book-open text-primary";
        }
        if (lower.contains("game") || lower.contains("bàn phím") || lower.contains("chuột")) {
            return "fas fa-gamepad text-danger";
        }
        return "fas fa-box-open text-primary";
    }

    public String getAutoBgColor() {
        if (name == null) {
            return "bg-light";
        }
        String lower = name.toLowerCase();
        if (lower.contains("trang sức") || lower.contains("nhẫn")) {
            return "bg-warning-subtle";
        }
        if (lower.contains("giày") || lower.contains("dép")) {
            return "bg-danger-subtle";
        }
        if (lower.contains("điện thoại") || lower.contains("phone")) {
            return "bg-primary-subtle";
        }
        if (lower.contains("laptop") || lower.contains("máy tính")) {
            return "bg-info-subtle";
        }
        if (lower.contains("quần") || lower.contains("jean") || lower.contains("kaki") || lower.contains("trousers")
                || lower.contains("pants") || lower.contains("short")) {
            return "bg-primary-subtle";
        }
        if (lower.contains("áo") || lower.contains("thời trang") || lower.contains("váy") || lower.contains("đầm")) {
            return "bg-success-subtle";
        }
        if (lower.contains("đồng hồ")) {
            return "bg-primary-subtle";
        }
        if (lower.contains("mỹ phẩm")) {
            return "bg-danger-subtle";
        }
        return "bg-light";
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", price=" + price +
                '}';
    }
}
