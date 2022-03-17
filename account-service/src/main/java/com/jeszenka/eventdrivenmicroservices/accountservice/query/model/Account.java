package com.jeszenka.eventdrivenmicroservices.accountservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Account {

	private String accountNumber;

	private BigDecimal balance;

	private String userId;
}
