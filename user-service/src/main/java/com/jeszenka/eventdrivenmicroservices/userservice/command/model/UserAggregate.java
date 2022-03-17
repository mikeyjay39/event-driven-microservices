package com.jeszenka.eventdrivenmicroservices.userservice.command.model;

import com.jeszenka.eventdrivenmicroservices.events.events.UserCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.userservice.command.commands.CreateUserCommand;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Aggregate
@Entity
@Table(name = "app_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAggregate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@AggregateIdentifier
	@EqualsAndHashCode.Include
	@Column(unique = true)
	private String userName;

	private String firstName;

	private String lastName;

	@CommandHandler
	public UserAggregate(CreateUserCommand command) {
		AggregateLifecycle.apply(new UserCreatedEvent(command.getUserName(),
				command.getFirstName(),
				command.getLastName()));
	}

	@EventSourcingHandler
	public void on(UserCreatedEvent event) {
		this.userName = event.getUserName();
		this.firstName = event.getFirstName();
		this.lastName = event.getLastName();
	}
}
