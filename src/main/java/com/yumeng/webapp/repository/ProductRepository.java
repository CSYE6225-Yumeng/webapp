package com.yumeng.webapp.repository;

import com.yumeng.webapp.data.Authority;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.util.HashUtil;
import com.yumeng.webapp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Component
@Repository
public class ProductRepository {
    public static Map<String, Object> getProduct(long productId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Product product = session.get(Product.class, productId);
        transaction.commit();
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
}
