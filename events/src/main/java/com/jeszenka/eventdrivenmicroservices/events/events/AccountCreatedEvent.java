package com.jeszenka.eventdrivenmicroservices.events.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountCreatedEvent {

	private String accountNumber;

	private BigDecimal balance;

	private String userId;
}
