package com.example.shop.patterns;

import com.example.shop.model.Pricing;
import com.example.shop.model.Topping;
import com.example.shop.model.Fruit;

import com.example.shop.repository.PricingRepository;

public class FruitFactory extends ToppingFactory{

    public FruitFactory(PricingRepository pricingRepo) {
        super(pricingRepo);
    }

    @Override
    public Topping createTopping(String name, String description, boolean isVegan){
        Fruit fruit = new Fruit(name, description);
        for (Pricing p : pricingRepo.findAll()){
            if (p.getDescription().equals("Fruit")){
                fruit.setPricing(p);
            }
        }
        return fruit;
    }
}
