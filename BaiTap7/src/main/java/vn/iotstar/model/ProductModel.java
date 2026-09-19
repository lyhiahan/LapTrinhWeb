package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private String images;
    private MultipartFile imageFile;
    private String description;
    private double discount;
    private Date createDate;
    private short status = 1;
    private Long categoryId;
    private Boolean isEdit = false;
}
