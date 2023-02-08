package com.yumeng.webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = {"sku"})})
@EntityListeners(value = AuditingEntityListener.class)
public class Product {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("description")
    @Column(name = "description", nullable = false)
    private String description;
    @JsonProperty("sku")
    @Column(name = "sku", nullable = false)
    private String sku;

    @JsonProperty("manufacturer")
    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;
    @JsonProperty("quantity")
    @Column(name = "quantity", nullable = false)
//    @Max(value=100, message = "Quantity must be a positive Integer less than or equal to 100!")
//    @Min(value=0, message = "Quantity must be a positive Integer less than or equal to 100!")
    @Range(max=100, min=0, message = "Quantity must be a positive Integer less than or equal to 100!")
    private Integer quantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreatedDate
    @Column(name = "date_added")
    private Date dateAdded;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "date_last_updated")
    @LastModifiedDate
    private Date dateLastUpdated;
    @ManyToOne
    @JoinColumn(name = "owner_user_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    public Product() {

    }

    public Map<String, Object> getProductResponse(){
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name",name);
        result.put("description",description);
        result.put("sku",sku);
        result.put("manufacturer",manufacturer);
        result.put("quantity",quantity);
        result.put("date_added",dateAdded);
        result.put("date_last_updated",dateLastUpdated);
        result.put("owner_user_id", user.getId());
        return result;
    }



    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSku() {
        return sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public User getUser() {
        return user;
    }
}
