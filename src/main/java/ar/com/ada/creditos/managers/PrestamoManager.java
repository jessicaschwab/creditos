package ar.com.ada.creditos.managers;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ar.com.ada.creditos.entities.*;

// crear un listado de prestamos de cada cliente
public class PrestamoManager {

    protected SessionFactory sessionFactory;

    public void setup() {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
                                                                                                  // from
                                                                                                  // hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw ex;
        }

    }

    public void exit() {
        sessionFactory.close();
    }

    public void create(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public Prestamo read(int prestamoId) {
        Session session = sessionFactory.openSession();

        Prestamo prestamo = session.get(Prestamo.class, prestamoId);

        session.close();

        return prestamo;
    }
    public Cliente readByDNI(String dni) {
        int dnientero= Integer.parseInt(dni);
        Session session = sessionFactory.openSession();

        Cliente cliente = session.byNaturalId(Cliente.class).using("dni",dnientero).load();

        session.close();

        return cliente;
    }

    public void update(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Este metodo en la vida real no debe existir ya qeu puede haber miles de
     * usuarios
     * 
     * @return
     */
    public List<Prestamo> buscarTodos() {
      
        Session session = sessionFactory.openSession();
        /// NUNCA HARCODEAR SQLs nativos en la aplicacion.
        // ESTO es solo para nivel educativo
        Query query = session.createNativeQuery("SELECT * FROM prestamo"+ Prestamo.class);
        List<Prestamo> todos = query.getResultList();

        return todos;

    }

   
    public List<Prestamo> buscarPorDni(int dni)  {
      
        Session session = sessionFactory.openSession();
        // SQL Injection vulnerability exposed.
        // Deberia traer solo aquella del nombre y con esto demostrarmos que trae todas
        // si pasamos
        // como nombre: "' or '1'='1"
        //Query query = session.createNativeQuery("SELECT * FROM cliente where cliente id  = '" + dni.getClienteId()
        //+ "'", Prestamo.class);
        Query query=session.createQuery(" SELECT p FROM Prestamo p WHERE p.cliente.dni=:dni");
        query.setParameter("dni", dni);
        List<Prestamo> prestamos= query.getResultList();
        return prestamos;

    }


}