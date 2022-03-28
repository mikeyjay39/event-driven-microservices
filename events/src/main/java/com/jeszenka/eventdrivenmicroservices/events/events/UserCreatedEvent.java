package com.jeszenka.eventdrivenmicroservices.events.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {

	private String userName;

	private String firstName;

	private String lastName;

}
