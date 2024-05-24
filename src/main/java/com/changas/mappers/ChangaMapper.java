package com.changas.mappers;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.model.Changa;

public class ChangaMapper {
    public static ChangaOverviewDTO toChangaOverviewDTO(Changa changa) {
        return ChangaOverviewDTO.builder().title(changa.getTitle()).id(changa.getId()).topics(changa.getTopics()).description(changa.getDescription()).photoUrl(changa.getPhotoUrl()).customerSummary(CustomerOverviewDTO.builder().id(changa.getProvider().getId()).name(changa.getProvider().getName()).email(changa.getProvider().getEmail()).photoUrl(changa.getProvider().getPhotoUrl()).build()).available(changa.getAvailable()).build();
    }
}
