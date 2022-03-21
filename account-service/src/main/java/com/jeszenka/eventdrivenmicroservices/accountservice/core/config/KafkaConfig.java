package com.jeszenka.eventdrivenmicroservices.accountservice.core.config;

import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.extensions.kafka.KafkaProperties;
import org.axonframework.extensions.kafka.configuration.KafkaMessageSourceConfigurer;
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.consumer.AsyncFetcher;
import org.axonframework.extensions.kafka.eventhandling.consumer.ConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.DefaultConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.Fetcher;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.KafkaEventMessage;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.axonframework.extensions.kafka.eventhandling.consumer.subscribable.SubscribableKafkaMessageSource;
import org.axonframework.extensions.kafka.eventhandling.producer.DefaultProducerFactory;
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaEventPublisher;
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaPublisher;
import org.axonframework.extensions.kafka.eventhandling.producer.ProducerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KafkaConfig {

	@Autowired
	private EventProcessingConfigurer eventProcessingConfigurer;

	private String groupId = "accountservice";

	private String processorName = "accountservice";

	/*@Bean("event-bus")
	EventBus eventBus() {
		return new SimpleEventBus.Builder().build();
	}*/


	@Bean
	public ProducerFactory<String, byte[]> producerFactory(//Duration closeTimeout,
														   KafkaProperties producerConfiguration
														   // ConfirmationMode confirmationMode,
														   // String transactionIdPrefix
	) {
		return DefaultProducerFactory.<String, byte[]>builder()
				//.closeTimeout(closeTimeout)                 // Defaults to "30" seconds
				//.producerCacheSize(producerCacheSize)       // Defaults to "10"; only used for "TRANSACTIONAL" mode
				.configuration(producerConfiguration.buildProducerProperties()) // Hard requirement
				//.confirmationMode(confirmationMode)         // Defaults to a Confirmation Mode of "NONE"
				//.transactionalIdPrefix(transactionIdPrefix) // Hard requirement when in "TRANSACTIONAL" mode
				.build();
	}


	@Bean
	public KafkaPublisher<String, byte[]> kafkaPublisher(
			//String topic,
			ProducerFactory<String, byte[]> producerFactory
			//KafkaMessageConverter<String, byte[]> kafkaMessageConverter,
			//int publisherAckTimeout
	) {
		KafkaPublisher<String, byte[]> kafkaPublisher = KafkaPublisher.<String, byte[]>builder()
				.topic("topic1")                               // Defaults to "Axon.Events"
				.producerFactory(producerFactory)           // Hard requirement
				//.messageConverter(kafkaMessageConverter)    // Defaults to a "DefaultKafkaMessageConverter"
				//.publisherAckTimeout(publisherAckTimeout)   // Defaults to "1000" milliseconds; only used for "WAIT_FOR_ACK" mode
				.build();
		//eventBus().subscribe(eventMessages -> eventMessages.forEach(kafkaPublisher::send));
		return kafkaPublisher;
	}


	@Bean
	public KafkaEventPublisher<String, byte[]> kafkaEventPublisher(KafkaPublisher<String, byte[]> kafkaPublisher) {
		KafkaEventPublisher<String, byte[]> kafkaEventPublisher = KafkaEventPublisher.<String, byte[]>builder()
				.kafkaPublisher(kafkaPublisher)             // Hard requirement
				.build();

		registerPublisherToEventProcessor(eventProcessingConfigurer, kafkaEventPublisher);
		return kafkaEventPublisher;
	}

	private void registerPublisherToEventProcessor(EventProcessingConfigurer eventProcessingConfigurer,
												   KafkaEventPublisher<String, byte[]> kafkaEventPublisher) {
		String processingGroup = KafkaEventPublisher.DEFAULT_PROCESSING_GROUP;
		eventProcessingConfigurer.registerEventHandler(configuration -> kafkaEventPublisher)
				.assignHandlerTypesMatching(
						processingGroup,
						clazz -> clazz.isAssignableFrom(KafkaEventPublisher.class)
				)
				.registerSubscribingEventProcessor(processingGroup);
		// Replace `registerSubscribingEventProcessor` for `registerTrackingEventProcessor` to use a tracking processor
	}

	@Bean
	public ConsumerFactory<String, byte[]> consumerFactory(KafkaProperties consumerConfiguration) {
		return new DefaultConsumerFactory<>(consumerConfiguration.buildConsumerProperties());
	}

	@Bean
	public Fetcher<?, ?, ?> fetcher(//long timeoutMillis,
									//ExecutorService executorService
	) {
		return AsyncFetcher.builder()
				//.pollTimeout(timeoutMillis)          // Defaults to "5000" milliseconds
				//.executorService(executorService)    // Defaults to a cached thread pool executor
				.build();
	}

	/*@Bean
	public StreamableKafkaMessageSource<String, byte[]> streamableKafkaMessageSource(//List<String> topics,
																					// String groupIdPrefix,
																					// Supplier<String> groupIdSuffixFactory,
																					 ConsumerFactory<String, byte[]> consumerFactory,
																					 Fetcher<String, byte[], KafkaEventMessage> fetcher
																					// KafkaMessageConverter<String, byte[]> messageConverter,
																					// int bufferCapacity
	) {
		return StreamableKafkaMessageSource.<String, byte[]>builder()
				.topics(List.of(new String[]{"topic1"}))                                                 // Defaults to a collection of "Axon.Events"
				//.groupIdPrefix(groupIdPrefix)                                   // Defaults to "Axon.Streamable.Consumer-"
				//.groupIdSuffixFactory(groupIdSuffixFactory)                     // Defaults to a random UUID
				.consumerFactory(consumerFactory)                               // Hard requirement
				.fetcher(fetcher)                                               // Hard requirement
				//.messageConverter(messageConverter)                             // Defaults to a "DefaultKafkaMessageConverter"
				//.bufferFactory(
				//		() -> new SortedKafkaMessageBuffer<>(bufferCapacity))   // Defaults to a "SortedKafkaMessageBuffer" with a buffer capacity of "1000"
				.build();
	}

	public void configureStreamableKafkaSource(EventProcessingConfigurer eventProcessingConfigurer,
											   String processorName,
											   StreamableKafkaMessageSource<String, byte[]> streamableKafkaMessageSource) {
		eventProcessingConfigurer.registerTrackingEventProcessor(
				processorName,
				configuration -> streamableKafkaMessageSource
		);
	}*/


	@Bean
	public KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer(Configurer configurer) {
		KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer = new KafkaMessageSourceConfigurer();
		configurer.registerModule(kafkaMessageSourceConfigurer);
		return kafkaMessageSourceConfigurer;
	}

	@Bean
	public SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource(//List<String> topics,
																						 //String groupId,
																						 ConsumerFactory<String, byte[]> consumerFactory,
																						 Fetcher<String, byte[], EventMessage<?>> fetcher,
																						 //KafkaMessageConverter<String, byte[]> messageConverter,
																						 //int consumerCount,
																						 KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer) {
		SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource = SubscribableKafkaMessageSource.<String, byte[]>builder()
				//.topics(topics)                     // Defaults to a collection of "Axon.Events"
				.groupId(groupId)                   // Hard requirement
				.consumerFactory(consumerFactory)   // Hard requirement
				.fetcher(fetcher)                   // Hard requirement
				//.messageConverter(messageConverter) // Defaults to a "DefaultKafkaMessageConverter"
				//.consumerCount(consumerCount)       // Defaults to a single Consumer
				.build();
		// Registering the source is required to tie into the Configurers lifecycle to start the source at the right stage
		kafkaMessageSourceConfigurer.configureSubscribableSource(configuration -> subscribableKafkaMessageSource);
		//this.configureSubscribableKafkaSource(eventProcessingConfigurer, processorName,
		//		subscribableKafkaMessageSource);
		return subscribableKafkaMessageSource;
	}


	private void configureSubscribableKafkaSource(EventProcessingConfigurer eventProcessingConfigurer,
												  String processorName,
												  SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource) {
		eventProcessingConfigurer.registerSubscribingEventProcessor(
				processorName,
				configuration -> subscribableKafkaMessageSource
		);
	}


}
