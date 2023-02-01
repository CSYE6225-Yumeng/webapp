package com.yumeng.webapp.repository;

import com.yumeng.webapp.dao.UserDao;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.util.HashUtil;
import org.jdbi.v3.core.Jdbi;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class UserRepository  {
    private Jdbi jdbi;

    public UserRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public User createUser(User user) throws PSQLException {
        user.setPassword(HashUtil.getHash(user.getPassword()));
        User newUser = jdbi.withExtension(UserDao.class, dao -> dao.createUser(user));
        String authId = jdbi.withExtension(UserDao.class, dao -> dao.createAuth(newUser));
        return newUser;
    }

    public User getUsers(Long id){
        return jdbi.withExtension(UserDao.class, dao -> dao.getUser(id));
    }

    public User updateUsers(Long id, User userNew) throws PSQLException{
        User userOld = getUsers(id);
        if(userNew.getFirstName() == null){
            userNew.setFirstName(userOld.getFirstName());
        }
        if(userNew.getLastName() == null){
            userNew.setLastName(userOld.getLastName());
        }
        if(userNew.getPassword() == null){
            userNew.setPassword(userOld.getPassword());
        }
        return jdbi.withExtension(UserDao.class, dao -> dao.updateUser(id, userNew));
    }


}
