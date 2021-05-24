package repository;

import model.Comanda;
import model.Farmacist;
import model.Medicament;
import model.TipMedicament;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class BDMedicamentRepository implements MedicamentRepository {
    SessionFactory sessionFactory;

    public BDMedicamentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void update(Integer id, Integer cantitate) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Medicament medicament = session.load(Medicament.class, id);
                medicament.setCantitateTotala(cantitate);
                session.update(medicament);
                tx.commit();
            }
            catch(RuntimeException ex){
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Integer id, String nume, String producator, TipMedicament tip, Integer cantitate) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Medicament medicament = session.load(Medicament.class, id);
                medicament.setNume(nume);
                medicament.setProducator(producator);
                medicament.setTip(tip);
                medicament.setCantitateTotala(cantitate);
                session.update(medicament);
                tx.commit();
            }
            catch(RuntimeException ex){
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Medicament medicament) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Query query = session.createQuery("from Medicament as m where m.id=:id", Medicament.class);
                query.setParameter("id", medicament.getId());
                Medicament med = (Medicament)query.getResultList().get(0);
                session.delete(med);
                tx.commit();
            }
            catch(RuntimeException ex){
                if(tx != null){
                    tx.rollback();
                }
            }
        }
    }

    @Override
    public List<Medicament> findByName(String name) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery("from Medicament as m where m.nume=:nume", Medicament.class);
                query.setParameter("nume", name);
                List<Medicament> medicamente = query.getResultList();
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
    public Medicament findByNumeAndProducator(String nume, String producator, TipMedicament tip) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery("from Medicament as m where m.nume=:nume and m.producator=:producator and m.tip=:tip", Medicament.class);
                query.setParameter("nume", nume);
                query.setParameter("producator", producator);
                query.setParameter("tip", tip);
                Medicament medicament = (Medicament)query.getResultList().get(0);
                tx.commit();
                return medicament;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Medicament findOne(Integer integer) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                Query query = session.createQuery("from Medicament as f  where f.id =:integer");
                query.setParameter("integer", integer);
                Medicament medicament = (Medicament)query.getResultList().get(0);
                transaction.commit();
                return medicament;
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
