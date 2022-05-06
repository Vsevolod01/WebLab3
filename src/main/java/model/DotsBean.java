package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import views.AreaResult;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ManagedBean(name = "dotsBean", eager = true)
@ApplicationScoped
public class DotsBean {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate");
    EntityManager em = emf.createEntityManager();

    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();


    private Dot dot = new Dot();
    private List<Dot> dots;

    @PostConstruct
    public void init() {
        Session session = factory.openSession();
        Query query = session.createQuery("from Dot");
        dots = (ArrayList<Dot>) query.getResultList();
        session.close();
    }

    public void addDot() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        dot.setDate(dateFormat.format(new Date(System.currentTimeMillis())));
        dot.setResult(AreaResult.isItInArea(dot));
        em.getTransaction().begin();
        em.persist(dot);
        em.getTransaction().commit();
        dots.add(dot);
        System.out.println("addDot()");
    }

    public void addDotFromSvg() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        dot.setDate(dateFormat.format(new Date(System.currentTimeMillis())));
        dot.setResult(AreaResult.isItInArea(dot));
        em.getTransaction().begin();
        em.persist(dot);
        em.getTransaction().commit();
    }

    public void clearTable() {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        String stringQuery = "DELETE FROM Dot";
        Query query = session.createQuery(stringQuery);
        query.executeUpdate();
        transaction.commit();
        System.out.println("clearTable()");
        session.close();
        dots.clear();
    }

    public Dot getDot() {
        return dot;
    }

    public void setDot(Dot dot) {
        this.dot = dot;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
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