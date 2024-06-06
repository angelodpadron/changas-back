package com.changas.mappers;

import com.changas.dto.question.InquiryDTO;
import com.changas.model.Inquiry;

public class InquiryMapper {

    public static InquiryDTO toInquiryDTO(Inquiry inquiry) {
        return InquiryDTO
                .builder()
                .id(inquiry.getId())
                .question(inquiry.getQuestion())
                .answer(inquiry.getAnswer())
                .changaId(inquiry.getChanga().getId())
                .changa(ChangaMapper.toChangaOverviewDTO(inquiry.getChanga()))
                .customer(CustomerMapper.toCustomerOverviewDTO(inquiry.getCustomer()))
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
