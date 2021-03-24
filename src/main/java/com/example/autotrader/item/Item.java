package com.example.autotrader.item;

import com.example.autotrader.bid.Bid;
import com.example.autotrader.offer.Offer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "name", length = 64)
    private String name;

    // each object has two priority queues
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<Offer> offers;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<Bid> bids;

    @Transactional(propagation=Propagation.REQUIRED)
    public List<Offer> getOffers() {
        return offers;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public List<Bid> getBids() {
        return bids;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}