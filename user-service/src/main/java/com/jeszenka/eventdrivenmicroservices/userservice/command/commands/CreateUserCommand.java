package com.jeszenka.eventdrivenmicroservices.userservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class CreateUserCommand {

	@TargetAggregateIdentifier
	private String userName;

	private String firstName;

	private String lastName;
}
