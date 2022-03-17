package com.jeszenka.eventdrivenmicroservices.userservice.query.handler;

import com.jeszenka.eventdrivenmicroservices.userservice.command.model.UserAggregate;
import com.jeszenka.eventdrivenmicroservices.userservice.query.model.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAggregate, Long> {

	UserDto findByUserName(String userName);
}