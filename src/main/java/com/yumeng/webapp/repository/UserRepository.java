package com.yumeng.webapp.repository;

import com.yumeng.webapp.dao.UserDao;
import com.yumeng.webapp.data.Authority;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.util.HashUtil;
import com.yumeng.webapp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.postgresql.util.PSQLException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


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
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//            return null;
//        }

//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            List < User > students = session.createQuery("from User", User.class).list();
//            students.forEach(s - > System.out.println(s.getFirstName()));
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
    }





//    private Jdbi jdbi;

//    public UserRepository(Jdbi jdbi) {
//        this.jdbi = jdbi;
//    }

//    public User createUser(User user) throws PSQLException {
//        user.setPassword(HashUtil.getHash(user.getPassword()));
//
//        User newUser = jdbi.withExtension(UserDao.class, dao -> dao.createUser(user));
//        String authId = jdbi.withExtension(UserDao.class, dao -> dao.createAuth(newUser));
//        return newUser;
//    }

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
