package com.example.autotrader.bid;

import com.example.autotrader.item.Item;
import com.example.autotrader.user.User;

import javax.persistence.*;

@Entity
@Table(name = "bids")
public class Bid implements Comparable<Bid> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "item_id")
    private Item item;

    @Column(nullable = false)
    private boolean matched;

    public void setMatched() {
        this.matched = true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public boolean isMatched() {
        return this.matched;
    }

    public double getPrice() {
        return this.price;
    }

    public Long getId() {
        return this.id;
    }

    public int compareTo(Bid other) {
        if (this.getPrice() < other.getPrice())
            return 1;
        else if (this.getPrice() == other.getPrice())
            return 0;
        return -1;
    }
}
