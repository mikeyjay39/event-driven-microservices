package com.jeszenka.eventdrivenmicroservices.events.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {

	private String accountNumber;

	private BigDecimal balance;

	private String userId;

}
