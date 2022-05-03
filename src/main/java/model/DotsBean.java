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
import java.util.ArrayList;


@ManagedBean(name = "dotsBean", eager = true)
@ApplicationScoped
public class DotsBean {

    private Dot dot;
    private ArrayList<Dot> dots;


    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    Session session = factory.openSession();

    public void addDot() {
        Transaction transaction = session.beginTransaction();
        session.save(dot);
        transaction.commit();
    }


    public void clearTable() {
        String stringQuery = "DELETE FROM DOT_TABLE";
        Query query = session.createQuery(stringQuery);
        query.executeUpdate();
    }

    public Dot getDot() {
        return dot;
    }

    public void setDot(Dot dot) {
        this.dot = dot;
    }

    public ArrayList<Dot> getDots() {
        Query query = session.createQuery("from DOT_TABLE");
        return (ArrayList<Dot>) query.getResultList();
//        return (ArrayList<Dot>) session.createQuery("from DOT_TABLE").list();
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