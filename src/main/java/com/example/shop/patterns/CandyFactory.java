package com.example.shop.patterns;

import com.example.shop.model.Pricing;
import com.example.shop.model.Topping;
import com.example.shop.model.Candy;

import com.example.shop.repository.PricingRepository;

public class CandyFactory extends ToppingFactory{

    public CandyFactory(PricingRepository pricingRepo) {
        super(pricingRepo);
    }

    @Override
    public Topping createTopping(String name, String description, boolean isVegan){
        Candy candy = new Candy(name, description, isVegan);
        for (Pricing p : pricingRepo.findAll()){
            if (p.getDescription().equals("Candy")){
                candy.setPricing(p);
            }
        }
        return candy;
    }
}
