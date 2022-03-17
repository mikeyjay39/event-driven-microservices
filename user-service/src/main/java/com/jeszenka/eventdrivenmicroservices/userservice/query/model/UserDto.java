package com.jeszenka.eventdrivenmicroservices.userservice.query.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto {
	private final Long id;
	private final String userName;
	private final String firstName;
	private final String lastName;
}
