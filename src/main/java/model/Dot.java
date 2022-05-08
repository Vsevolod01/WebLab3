package model;

import javax.persistence.*;
import java.io.Serializable;

//TODO: check with lombok getter/setters
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="DOT_TABLE")
public class Dot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @SequenceGenerator(name = "native", sequenceName = "native", allocationSize = 1)
    @Column(name = "ID")
    private int id;

    @Column(name = "X")
    private double x;

    @Column(name = "Y")
    private double y;

    @Column(name = "R")
    private double r = 1;

    @Column(name = "RESULT")
    private boolean result;

    @Column(name = "LAIKAS")
    private String date;

    public Dot() {
        r = 1;
    }

    public Dot(double x, double y, double r, boolean result, String date) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
        this.date = date;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
