package repository;

import model.Comanda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class BDComandaRepository implements ComandaRepository {

    private SessionFactory sessionFactory;

    public BDComandaRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comanda> findMyOrders(String username) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
               Query query =
                        session.createQuery("from Comanda as m where m.medicUsername=:username order by m.data asc", Comanda.class);
               query.setParameter("username", username);
               List<Comanda> comenzi = query.getResultList();
                for (Comanda m : comenzi) {
                    System.out.println(m);
                }
                tx.commit();
                return comenzi;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
                System.out.println(ex.getMessage());
                System.out.println("COMANDA ERROR!!");
                return null;
            }
        }
    }

    @Override
    public Comanda findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Comanda> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Comanda> comenzi =
                        session.createQuery("from Comanda as m order by m.data asc", Comanda.class).
                                list();
                tx.commit();
                return comenzi;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public void save(Comanda entity) {

    }
}
