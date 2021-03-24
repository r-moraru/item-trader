package com.example.autotrader.item;

import com.example.autotrader.bid.Bid;
import com.example.autotrader.bid.BidRepository;
import com.example.autotrader.offer.Offer;
import com.example.autotrader.offer.OfferRepository;

import javax.transaction.Transactional;
import java.util.PriorityQueue;

public class ItemWrapper {
    private final PriorityQueue<Offer> offerPriorityQueue;
    private final PriorityQueue<Bid> bidPriorityQueue;

    public ItemWrapper() {
        offerPriorityQueue = new PriorityQueue<>();
        bidPriorityQueue = new PriorityQueue<>();
    }

    @Transactional
    public void matchBids(OfferRepository offerRepo, BidRepository bidRepo) {
        while (!(offerPriorityQueue.isEmpty() || bidPriorityQueue.isEmpty())) {

            if (getLowestOffer().getPrice() > getHighestBid().getPrice())
                break;

            Offer offer = offerPriorityQueue.remove();
            Bid bid = bidPriorityQueue.remove();

            offer.setMatched();
            bid.setMatched();

            offerRepo.save(offer);
            bidRepo.save(bid);
        }
    }

    public void insertOffer(Offer offer) {
        offerPriorityQueue.add(offer);
    }

    public void insertBid(Bid bid) {
        bidPriorityQueue.add(bid);
    }

    public Bid getHighestBid() {
        return bidPriorityQueue.peek();
    }

    public Offer getLowestOffer() {
        return offerPriorityQueue.peek();
    }

    public void removeHighestBid() {
        bidPriorityQueue.remove();
    }

    public void removeLowestOffer() {
        offerPriorityQueue.remove();
    }
}
