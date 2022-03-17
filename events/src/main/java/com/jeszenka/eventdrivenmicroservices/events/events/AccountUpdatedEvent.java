package com.jeszenka.eventdrivenmicroservices.events.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountUpdatedEvent {

	private String accountNumber;

	private BigDecimal balance;
}
