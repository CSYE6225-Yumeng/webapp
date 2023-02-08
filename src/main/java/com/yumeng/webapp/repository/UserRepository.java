package com.yumeng.webapp.repository;

import com.yumeng.webapp.data.Authority;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.util.HashUtil;
import com.yumeng.webapp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component
@Repository
public class UserRepository { // interface UserRepository extends JpaRepository<User, Long>
    public User createUser(User user) throws HibernateException {
//        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        user.setPassword(HashUtil.getHash(user.getPassword()));
        session.save(user);
        Authority authority = new Authority(user);
        session.save(authority);
        transaction.commit();
        return user;
    }


    public User getUsers(Long id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, id);
        transaction.commit();
        return user;
    }

    public User updateUsers(Long id, User userNew) throws PSQLException{
        User user = getUsers(id);
        if(userNew.getFirstName() != null){
            user.setFirstName(userNew.getFirstName());
        }
        if(userNew.getLastName() != null){
            user.setLastName(userNew.getLastName());
        }
        if(userNew.getPassword() != null){
            user.setPassword(HashUtil.getHash(userNew.getPassword()));
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        return user;
    }


}
