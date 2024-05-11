package com.example.shop.patterns;


import com.example.shop.model.Topping;
import com.example.shop.model.Pricing;

import com.example.shop.repository.PricingRepository;

public abstract class ToppingFactory {
    protected PricingRepository pricingRepo;

    public ToppingFactory(PricingRepository pricingRepo){
        this.pricingRepo = pricingRepo;
    }

    public Topping createTopping(String name, String description, boolean isVegan){
        return null;
    }
}
