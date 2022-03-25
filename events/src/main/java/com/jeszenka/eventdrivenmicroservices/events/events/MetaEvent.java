package com.jeszenka.eventdrivenmicroservices.events.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaEvent {

	private Long spanId;

	public MetaEvent(Long spanId) {
		this.spanId = spanId;
		this.setSpan();
	}

	public MetaEvent() {
		System.out.println("constructor called");
		this.setSpan();
	}

	public void setSpan() {

	}


}
