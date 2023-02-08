package com.yumeng.webapp.repository;

import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Component
@Repository
public class ProductRepository {
    public Map<String, Object> getProduct(long productId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Product product = session.get(Product.class, productId);
        transaction.commit();
        if (product == null){
            return null;
        }
        return product.getProductResponse();
    }

    public Map<String, Object> createProduct (Product product, String userid) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, userid);
        product.setUser(user);
        session.save(product);
        transaction.commit();
        return product.getProductResponse();
    }

    public Product hasPermission(String userId, long ProductId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Product product = session.get(Product.class, ProductId);
        transaction.commit();
        if(product != null){
            if(userId.equals(Long.toString(product.getUser().getId()))){
                return product;
            }
        }
        return null;
    }

    public Product hasProduct(long ProductId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Product product = session.get(Product.class, ProductId);
        transaction.commit();
        return product;
    }

    public Product updateProduct(Product product, Product getProduct) throws PSQLException {
        String name = product.getName();
        if(name != null){
            getProduct.setName(name);
        }
        String description = product.getDescription();
        if(description != null){
            getProduct.setDescription(description);
        }
        String sku = product.getSku();
        if(sku != null){
            getProduct.setSku(sku);
        }
        String manufacturer = product.getManufacturer();
        if(manufacturer != null){
            getProduct.setManufacturer(manufacturer);
        }
        Integer quantity = product.getQuantity();
        if(quantity != null){
            getProduct.setQuantity(quantity);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(getProduct);
        transaction.commit();
        return getProduct;
    }

    public void deleteProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(product);
        transaction.commit();
    }
}
