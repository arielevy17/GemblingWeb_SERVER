package com.ashcollege;


import com.ashcollege.entities.Client;
import com.ashcollege.entities.Note;
import com.ashcollege.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return this.sessionFactory.getCurrentSession().createQuery("FROM User").list();
    }

    public Client getClientByFirstName(String firstName) {
        return (Client) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Client WHERE firstName = :firstName")
                .setParameter("firstName", firstName)
                .setMaxResults(1)
                .uniqueResult();
    }

    public User login(String username, String password) {
        return (User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username = :username AND password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<Note> getNotes(String secret) {
        return this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Note WHERE owner.secret = :secret")
                .setParameter("secret", secret)
                .list();
    }

    public List<Note> getNotesByCollegeName (String collegeName) {
        return this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Note WHERE owner.college.name = :collegeName")
                .setParameter("collegeName", collegeName)
                .list();
    }
}