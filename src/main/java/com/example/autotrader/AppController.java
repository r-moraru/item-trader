package com.example.autotrader;

import com.example.autotrader.bid.Bid;
import com.example.autotrader.bid.BidRepository;
import com.example.autotrader.item.Item;
import com.example.autotrader.item.ItemRepository;
import com.example.autotrader.item.ItemWrapper;
import com.example.autotrader.offer.Offer;
import com.example.autotrader.offer.OfferRepository;
import com.example.autotrader.offer.BidWrapper;
import com.example.autotrader.user.User;
import com.example.autotrader.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.*;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private BidRepository bidRepo;

    private final Map<String, ItemWrapper> itemMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createItemList() {
        for (Item item : itemRepo.findAll()) {
            ItemWrapper itemWrapper = new ItemWrapper();

            for (Offer offer : item.getOffers()) {
                if (!offer.isMatched())
                    itemWrapper.insertOffer(offer);
            }

            for (Bid bid : item.getBids()) {
                if (!bid.isMatched())
                    itemWrapper.insertBid(bid);
            }

            itemWrapper.matchBids(offerRepo, bidRepo);

            itemMap.put(item.getName(), itemWrapper);
        }
    }

    // ...

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "register_form";
    }

    @PostMapping("/process_registration")
    public String processRegistration(User user) {
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        String encodedPass = passEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        userRepo.save(user);

        return "registration_success";
    }

    // get - my_offers

    @GetMapping("/my_offers")
    @Transactional
    public String showOffers(Authentication authentication, Model model) {
        String userName = authentication.getName();
        User user = userRepo.findByEmail(userName);

        List<Offer> offers = user.getOffers();
        model.addAttribute("offers", offers);

        return "offers/my_offers";
    }

    // get - new_offer
    @GetMapping("/new_offer")
    public String showOfferForm(Model model) {
        model.addAttribute("offer", new BidWrapper());

        return "offers/offer_form";
    }

    @PostMapping("/offer_added")
    @Transactional
    public String processOffer(BidWrapper bidWrapper,
                               Authentication authentication) {
        double price = bidWrapper.getPrice();
        Offer offer = new Offer();
        String itemName = bidWrapper.getItemName();

        Item item = itemRepo.findByName(itemName);
        if (item == null) {
            item = new Item();
            item.setName(itemName);
            itemMap.put(itemName, new ItemWrapper());
        }

        itemRepo.save(item);

        offer.setPrice(price);
        offer.setItem(item);
        offer.setUser(userRepo.findByEmail(authentication.getName()));

        offerRepo.save(offer);
        itemMap.get(offer.getItem().getName())
                    .insertOffer(offer);
        itemMap.get(offer.getItem().getName())
                .matchBids(offerRepo, bidRepo);

        return "offers/process_offer";
    }

    @GetMapping("/my_bids")
    @Transactional
    public String showBids(Authentication authentication, Model model) {
        String userName = authentication.getName();
        User user = userRepo.findByEmail(userName);

        List<Bid> bids = user.getBids();

        model.addAttribute("bids", bids);

        return "bids/my_bids";
    }

    @GetMapping("/new_bid")
    public String viewBidForm(Model model) {
        model.addAttribute("bid", new BidWrapper());

        return "bids/bid_form";
    }

    @PostMapping("/bid_added")
    @Transactional
    public String processBid(BidWrapper bidWrapper,
                             Authentication authentication) {
        double price = bidWrapper.getPrice();
        Bid bid = new Bid();
        String itemName = bidWrapper.getItemName();

        Item item = itemRepo.findByName(itemName);
        if (item == null) {
            item = new Item();
            item.setName(itemName);
            itemMap.put(itemName, new ItemWrapper());
        }

        itemRepo.save(item);

        bid.setPrice(price);
        bid.setItem(item);
        bid.setUser(userRepo.findByEmail(authentication.getName()));

        bidRepo.save(bid);
        itemMap.get(bid.getItem().getName())
                .insertBid(bid);
        itemMap.get(bid.getItem().getName())
                .matchBids(offerRepo, bidRepo);

        return "bids/process_bid";
    }
}

