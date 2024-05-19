package com.changas.mappers;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.model.Customer;

public class CustomerMapper {

    public static CustomerOverviewDTO toCustomerOverviewDTO(Customer customer) {
        return CustomerOverviewDTO
                .builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .photoUrl(customer.getPhotoUrl())
                .build();
    }

}
