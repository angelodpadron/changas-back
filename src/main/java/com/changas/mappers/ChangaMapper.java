package com.changas.mappers;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.model.Changa;

public class ChangaMapper {
    public static ChangaOverviewDTO toChangaOverviewDTO(Changa changa) {
        return ChangaOverviewDTO
                .builder()
                .title(changa.getTitle())
                .id(changa.getId())
                .topics(changa.getTopics())
                .description(changa.getDescription())
                .photoUrl(changa.getPhotoUrl())
                .customerSummary(CustomerMapper.toCustomerOverviewDTO(changa.getProvider()))
                .available(changa.getAvailable())
                .build();
    }


}
