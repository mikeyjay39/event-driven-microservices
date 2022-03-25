package com.jeszenka.eventdrivenmicroservices.events.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
/*@AllArgsConstructor
@NoArgsConstructor*/
public class AccountCreatedEvent extends MetaEvent {

	private String accountNumber;

	private BigDecimal balance;

	private String userId;

	public AccountCreatedEvent(String accountNumber, BigDecimal balance, String userId) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.userId = userId;
	}

	public AccountCreatedEvent() {
		super();
	}


}
