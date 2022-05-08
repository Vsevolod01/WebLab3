package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import views.AreaResult;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ManagedBean(name = "dotsBean", eager = true)
@ApplicationScoped
public class DotsBean {

    @Resource
    private UserTransaction transaction;

    @PersistenceContext(unitName = "hibernate")
    EntityManager em;

    static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();


    private Dot dot = new Dot();
    private List<Dot> dots;
    private String y;

    @PostConstruct
    public void init() {
        Session session = factory.openSession();
        Query query = session.createQuery("from Dot");
        dots = (ArrayList<Dot>) query.getResultList();
        session.close();
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
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (SystemException systemException) {
                systemException.printStackTrace();
            }
        }

    }

    public void clearTable() {
        try {
            transaction.begin();
            String stringQuery = "DELETE FROM Dot";
            em.createQuery(stringQuery).executeUpdate();
            transaction.commit();
            System.out.println("clearTable()");

            dots.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
        dot.setY(Double.parseDouble(y));
    }
}