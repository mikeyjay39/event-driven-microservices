package com.jeszenka.eventdrivenmicroservices.accountservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateAccountCommand {

	@TargetAggregateIdentifier
	private String accountNumber;

	private BigDecimal balance;

	private String userId;
}
