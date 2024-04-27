package com.changas.dto.notification;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import lombok.*;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HireChangaNotificationDTO {

    private CustomerOverviewDTO customer;
    private Instant createdAt;
    private Boolean isRead;
}
