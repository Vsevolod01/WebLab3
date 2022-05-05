package model;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@ManagedBean(name = "dotsBean", eager = true)
@ApplicationScoped
public class DotsBean {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate");
    EntityManager em = emf.createEntityManager();

    private Dot dot = new Dot();
    private ArrayList<Dot> dots = new ArrayList<>();


    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    Session session = factory.openSession();

    public void addDot() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        dot.setDate(dateFormat.format(new Date(System.currentTimeMillis())));
        em.getTransaction().begin();
        em.persist(dot);
        em.getTransaction().commit();

    }


    public void clearTable() {
        Transaction transaction = session.beginTransaction();
        String stringQuery = "DELETE FROM Dot";
        Query query = session.createQuery(stringQuery);
        query.executeUpdate();
        transaction.commit();
    }

    public Dot getDot() {
        return dot;
    }

    public void setDot(Dot dot) {
        this.dot = dot;
    }

    public ArrayList<Dot> getDots() {
        Query query = session.createQuery("from Dot");
        return (ArrayList<Dot>) query.getResultList();
    }

    public void setDots(ArrayList<Dot> dots) {
        this.dots = dots;
    }

    public void toggleX(ActionEvent event) {
        UIComponent component = event.getComponent();
        System.out.print(component.getAttributes());
        String value = (String) component.getAttributes().get("value");
        dot.setX(Integer.parseInt(value));
    }

    public void toggleR(ActionEvent event) {
        UIComponent component = event.getComponent();
        System.out.print(component.getAttributes());
        String value = (String) component.getAttributes().get("value");
        dot.setR(Double.parseDouble(value));
    }
}