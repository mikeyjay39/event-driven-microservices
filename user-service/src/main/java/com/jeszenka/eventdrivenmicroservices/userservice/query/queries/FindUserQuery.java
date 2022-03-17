package com.jeszenka.eventdrivenmicroservices.userservice.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindUserQuery {

	private String userName;
}
