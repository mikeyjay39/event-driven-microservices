package com.jeszenka.eventdrivenmicroservices.userservice.query.handler;

import com.jeszenka.eventdrivenmicroservices.events.events.UserCreatedEvent;
import com.jeszenka.eventdrivenmicroservices.userservice.query.model.UserDto;
import com.jeszenka.eventdrivenmicroservices.userservice.query.queries.FindUserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("userEventHandler")
@Slf4j
@RequiredArgsConstructor
public class UserEventHandler {

	private final UserRepository userAggregateRepository;

	@EventHandler
	public void on(UserCreatedEvent event) {
		log.info("User created: {}", event.getUserName());
	}

	@QueryHandler
	public UserDto handle(FindUserQuery query) {
		return userAggregateRepository.findByUserName(query.getUserName());
	}
}
