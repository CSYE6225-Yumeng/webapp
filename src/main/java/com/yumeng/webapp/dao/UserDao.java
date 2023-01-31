package com.yumeng.webapp.dao;

import com.yumeng.webapp.data.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.transaction.Transaction;


public interface UserDao {  // data access object
    @Transaction
    @SqlQuery("INSERT INTO users(first_name, last_name, password, username) " +
            "VALUES(:firstName,:lastName,:password,:username) " +
            "returning id, first_name, last_name, username, account_created, account_updated ")
    @RegisterBeanMapper(User.class)
    User createUser(@BindBean User user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    @RegisterBeanMapper(User.class)
    User getUser(@Bind("id") Long id);

    @Transaction
    @SqlQuery("UPDATE users " +
            "SET first_name = :firstName, last_name = :lastName, password = :password " +
            "WHERE id = :id " +
            "returning id, first_name, last_name, username, account_created, account_updated ")
    @RegisterBeanMapper(User.class)
    User updateUser(@Bind("id") Long id, @BindBean User user);

}
