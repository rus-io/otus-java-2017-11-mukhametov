package hibernate.impl;

import hibernate.impl.dao.AddressDataSetDAO;
import hibernate.impl.dao.PhoneDataSetDAO;
import hibernate.impl.dao.UserDataSetDAO;
import hibernate.models.AddressDataSet;
import hibernate.models.PhoneDataSet;
import hibernate.models.UserDataSet;
import hibernate.db.DBService;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {

    private Server server;
    private SessionFactory sessionFactory;

    public DBServiceHibernateImpl() {
        try {
            server = Server.createTcpServer().start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sessionFactory = createSessionFactory(ConfigurationHelper.getConfiguration());
    }

    @Override
    public void saveUser(UserDataSet user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public UserDataSet readUser(long id) {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.read(id);
        });
    }

    @Override
    public void saveAddress(AddressDataSet address) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            AddressDataSetDAO dao = new AddressDataSetDAO(session);
            dao.save(address);
            session.getTransaction().commit();
        }
    }

    @Override
    public AddressDataSet readAddress(long id) {
        return runInSession(session -> {
            AddressDataSetDAO dao = new AddressDataSetDAO(session);
            return dao.read(id);
        });
    }

    @Override
    public void savePhone(PhoneDataSet phone) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            PhoneDataSetDAO dao = new PhoneDataSetDAO(session);
            dao.save(phone);
            session.getTransaction().commit();
        }
    }

    @Override
    public PhoneDataSet readPhone(long id) {
        return runInSession(session -> {
            PhoneDataSetDAO dao = new PhoneDataSetDAO(session);
            return dao.read(id);
        });
    }

    @Override
    public void close() {
        sessionFactory.close();
        System.out.println("Session is closed.");
        server.stop();
    }

    private SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}
