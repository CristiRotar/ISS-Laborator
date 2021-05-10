package repository;

import model.Farmacist;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Query;

public class BDFarmacistRepository implements FarmacistRepository {
    SessionFactory sessionFactory;

    public BDFarmacistRepository(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Farmacist findByUserAndPassword(String username, String password) {

        try(Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                Query query = session.createQuery("from Farmacist as f  where f.username =:username and f.password =:password");
                query.setParameter("username", username);
                query.setParameter("password", password);
                Farmacist farmacist = (Farmacist)query.getResultList().get(0);
                transaction.commit();
                return farmacist;
            }
            catch(Exception e){
                if(transaction != null)
                    transaction.rollback();
                System.out.println(e.getMessage());
                return null;
            }
        }

    }

    @Override
    public Farmacist findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Farmacist> findAll() {
        return null;
    }

    @Override
    public void save(Farmacist entity) {

    }



}

