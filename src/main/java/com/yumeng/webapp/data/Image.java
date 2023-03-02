package com.yumeng.webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name = "image", uniqueConstraints = {@UniqueConstraint(columnNames = {"image_id"})})
@EntityListeners(value = AuditingEntityListener.class)
public class Image {
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @Column(name = "image_id")
//    @Id
//    @GeneratedValue
//    @UuidGenerator(style = UuidGenerator.Style.TIME)
//    private String imageId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "uuid2")
//    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "image_id")
    private String imageId;

    @Column(name = "product_id", nullable = false)
    @JsonProperty("product_id")
    private String productId;

    @Column(name = "file_name", nullable = false)
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty(value = "date_created", access = JsonProperty.Access.READ_ONLY)
    @CreatedDate
    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "s3_bucket_path", nullable = false)
    @JsonProperty("s3_bucket_path")
    private String s3BucketPath;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;


    public Image() {

    }

    public Map<String, Object> getImageResponse(){
        Map<String, Object> result = new HashMap<>();
        result.put("image_id", imageId);
        result.put("product_id",productId);
        result.put("file_name",fileName);
        result.put("date_created",dateCreated);
        result.put("s3_bucket_path",s3BucketPath);
        return result;
    }

//    public void setImageId() {
//        this.imageId = String.valueOf(UUID.randomUUID());
//    }


    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setS3BucketPath(String s3BucketPath) {
        this.s3BucketPath = s3BucketPath;
    }

//    public String getImageId() {
//        return imageId;
//    }

    public String getProductId() {
        return productId;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getS3BucketPath() {
        return s3BucketPath;
    }

    public User getUser() {
        return user;
    }

}
