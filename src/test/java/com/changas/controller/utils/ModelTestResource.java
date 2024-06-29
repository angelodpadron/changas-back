package com.changas.controller.utils;

import com.changas.model.Customer;

import java.util.Set;

public class ModelTestResource {

    public static Customer getCustomer() {
        return new Customer(null, "Pepe", "pepe@email.com", "password", null, Set.of());
    }

}
