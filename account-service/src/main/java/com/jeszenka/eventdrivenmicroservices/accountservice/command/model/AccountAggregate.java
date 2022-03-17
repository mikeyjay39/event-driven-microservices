package com.jeszenka.eventdrivenmicroservices.accountservice.command.model;

import com.jeszenka.eventdrivenmicroservices.accountservice.command.commands.CreateAccountCommand;
import com.jeszenka.eventdrivenmicroservices.accountservice.command.commands.UpdateAccountCommand;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.events.events.AccountUpdatedEvent;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Aggregate
@Entity
@Table(name = "account")
public class AccountAggregate {

	@AggregateIdentifier
	@Id
	private String accountNumber;

	private BigDecimal balance;

	private String userId;

	protected AccountAggregate() {
	}

	@CommandHandler
	public AccountAggregate(CreateAccountCommand command) {
		AggregateLifecycle.apply(new AccountCreatedEvent(command.getAccountNumber(),
				command.getBalance(),
				command.getUserId()));
	}

	@EventSourcingHandler
	public void on(AccountCreatedEvent event) {
		this.accountNumber = event.getAccountNumber();
		this.balance = event.getBalance();
		this.userId = event.getUserId();
	}

	@CommandHandler
	public void handle(UpdateAccountCommand command) {
		AggregateLifecycle.apply(new AccountUpdatedEvent(command.getAccountNumber(),
				command.getBalance()));
	}

	@EventSourcingHandler
	public void on(AccountUpdatedEvent event) {
		this.accountNumber = event.getAccountNumber();
		this.balance = event.getBalance();
	}


}
