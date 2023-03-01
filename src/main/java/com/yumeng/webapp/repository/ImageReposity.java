package com.yumeng.webapp.repository;

import com.yumeng.webapp.data.Image;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Component
@Repository
public class ImageReposity {
    public Map<String, Object> createImage (Image image, String userid) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, userid);
        image.setUser(user);
        session.save(image);
        transaction.commit();
        return image.getImageResponse();
    }
}
