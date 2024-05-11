package com.example.shop.patterns;

import com.example.shop.model.Pricing;
import com.example.shop.model.Topping;
import com.example.shop.model.Sauce;

import com.example.shop.repository.PricingRepository;

public class SauceFactory extends ToppingFactory{

    public SauceFactory(PricingRepository pricingRepo) {
        super(pricingRepo);
    }

    @Override
    public Topping createTopping(String name, String description, boolean isVegan){
        Topping sauce = new Sauce(name, description, isVegan);
        for (Pricing p : pricingRepo.findAll()){
            if (p.getDescription().equals("Sauce")){
                sauce.setPricing(p);
            }
        }
        return sauce;
    }
}