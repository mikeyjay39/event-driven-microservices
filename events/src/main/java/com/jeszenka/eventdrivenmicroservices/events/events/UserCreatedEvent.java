package com.jeszenka.eventdrivenmicroservices.events.events;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreatedEvent {

	private String userName;

	private String firstName;

	private String lastName;

}
