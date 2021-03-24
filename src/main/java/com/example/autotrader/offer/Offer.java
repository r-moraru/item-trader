package com.example.autotrader.offer;

import com.example.autotrader.item.Item;
import com.example.autotrader.user.User;

import javax.persistence.*;

@Entity
@Table(name = "offers")
public class Offer implements Comparable<Offer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, name = "price")
    private Double price;

    @Column(nullable = false)
    private boolean matched;

    public double getPrice() {
        return this.price;
    }

    public Long getId() {
        return this.id;
    }

    public Item getItem() {
        return item;
    }

    public User getUser() {
        return user;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMatched() {
        this.matched = true;
    }

    public boolean isMatched() {
        return this.matched;
    }

    public int compareTo(Offer other) {
        if (this.getPrice() < other.getPrice())
            return -1;
        else if (this.getPrice() == other.getPrice())
            return 0;
        return 1;
    }
}
