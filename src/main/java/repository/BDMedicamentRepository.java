package repository;

import model.Comanda;
import model.Medicament;
import model.TipMedicament;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class BDMedicamentRepository implements MedicamentRepository {
    SessionFactory sessionFactory;

    public BDMedicamentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void update(Medicament medicament) {
    }

    @Override
    public void delete(Medicament medicament) {

    }

    @Override
    public Medicament findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Medicament> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Medicament> medicamente =
                        session.createQuery("from Medicament as m order by m.nume asc", Medicament.class).
                                list();
                tx.commit();
                return medicamente;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public void save(Medicament entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }
}
