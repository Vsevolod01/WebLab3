package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import views.AreaResult;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.*;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ManagedBean(name = "dotsBean", eager = true)
@ApplicationScoped
public class DotsBean {

//    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate");
//    EntityManager em = emf.createEntityManager();
    @Resource
    private UserTransaction transaction;

    @PersistenceContext(unitName = "hibernate")
    EntityManager em;


    //TODO - надо убрать
    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();


    private Dot dot = new Dot();
    private List<Dot> dots;

    @PostConstruct
    public void init() {
//        Session session = factory.openSession();
//        Query query = session.createQuery("from Dot");
//        dots = (ArrayList<Dot>) query.getResultList();
//        session.close();
        dots = em.createQuery("from Dot").getResultList();
    }

    public void addDot() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
            dot.setDate(dateFormat.format(new Date(System.currentTimeMillis())));
            dot.setResult(AreaResult.isItInArea(dot));
            transaction.begin();
            em.persist(dot);
            transaction.commit();
            dots.add(dot);
            System.out.println("addDot()");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (SystemException systemException) {
                systemException.printStackTrace();
            }
        }

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