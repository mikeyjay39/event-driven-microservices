package com.jeszenka.eventdrivenmicroservices.userservice.core.config;

import com.google.common.collect.Lists;
import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.async.SequentialPerAggregatePolicy;
import org.axonframework.extensions.kafka.KafkaProperties;
import org.axonframework.extensions.kafka.configuration.KafkaMessageSourceConfigurer;
import org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.consumer.AsyncFetcher;
import org.axonframework.extensions.kafka.eventhandling.consumer.ConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.DefaultConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.Fetcher;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.KafkaEventMessage;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.SortedKafkaMessageBuffer;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.axonframework.extensions.kafka.eventhandling.consumer.subscribable.SubscribableKafkaMessageSource;
import org.axonframework.extensions.kafka.eventhandling.producer.DefaultProducerFactory;
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaEventPublisher;
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaPublisher;
import org.axonframework.extensions.kafka.eventhandling.producer.ProducerFactory;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

@Configuration
public class KafkaConfig {

	/*@Autowired
	private EventProcessingConfigurer eventProcessingConfigurer;*/

	private String groupId = "userservice";

	private String processorName = "com.jeszenka.eventdrivenmicroservices.userservice.query.handler";

	@Bean
	public ProducerFactory<String, byte[]> producerFactory(KafkaProperties producerConfiguration) {
		return DefaultProducerFactory.<String, byte[]>builder()
				.configuration(producerConfiguration.buildProducerProperties())       // Hard requirement
				.build();
	}


	@Bean
	public KafkaPublisher<String, byte[]> kafkaPublisher(
			ProducerFactory<String, byte[]> producerFactory,
			KafkaMessageConverter<String, byte[]> kafkaMessageConverter
	) {
		KafkaPublisher<String, byte[]> kafkaPublisher = KafkaPublisher.<String, byte[]>builder()
				.topic("topic1")// Defaults to "Axon.Events"
				.producerFactory(producerFactory)           // Hard requirement
				.messageConverter(kafkaMessageConverter)    // Defaults to a "DefaultKafkaMessageConverter"
				.build();
		return kafkaPublisher;
	}


	@Bean
	public KafkaEventPublisher<String, byte[]> kafkaEventPublisher(KafkaPublisher<String, byte[]> kafkaPublisher) {
		KafkaEventPublisher<String, byte[]> kafkaEventPublisher = KafkaEventPublisher.<String, byte[]>builder()
				.kafkaPublisher(kafkaPublisher)             // Hard requirement
				.build();

		//registerPublisherToEventProcessor(eventProcessingConfigurer, kafkaEventPublisher);
		return kafkaEventPublisher;
	}


	@Autowired
	public void registerPublisherToEventProcessor(EventProcessingConfigurer eventProcessingConfigurer,
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
	public Fetcher<?, ?, ?> fetcher() {
		return AsyncFetcher.builder()
				.build();
	}

	@Bean
	public KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer(Configurer configurer) {
		KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer = new KafkaMessageSourceConfigurer();
		configurer.registerModule(kafkaMessageSourceConfigurer);
		return kafkaMessageSourceConfigurer;
	}

	/*@Bean
	public StreamableKafkaMessageSource<String, byte[]> streamableKafkaMessageSource(//List<String> topics,
																					 //String groupIdPrefix,
																					 //Supplier<String> groupIdSuffixFactory,
																					 ConsumerFactory<String, byte[]> consumerFactory,
																					 Fetcher<String, byte[], KafkaEventMessage> fetcher,
																					 KafkaMessageConverter<String, byte[]> messageConverter
																					 //int bufferCapacity
																					 ) {
		return StreamableKafkaMessageSource.<String, byte[]>builder()
				.topics(List.of("topic1"))                                                 // Defaults to a collection of "Axon.Events"
				.groupIdPrefix(groupId)                                   // Defaults to "Axon.Streamable.Consumer-"
				//.groupIdSuffixFactory(groupIdSuffixFactory)                     // Defaults to a random UUID
				.consumerFactory(consumerFactory)                               // Hard requirement
				.fetcher(fetcher)                                               // Hard requirement
				.messageConverter(messageConverter)                             // Defaults to a "DefaultKafkaMessageConverter"
				.bufferFactory(
						() -> new SortedKafkaMessageBuffer<>(1000))   // Defaults to a "SortedKafkaMessageBuffer" with a buffer capacity of "1000"
				.build();
	}

	@Autowired
	public void configureStreamableKafkaSource(EventProcessingConfigurer eventProcessingConfigurer,
											   StreamableKafkaMessageSource<String, byte[]> streamableKafkaMessageSource) {
		eventProcessingConfigurer.registerTrackingEventProcessor(
				processorName,
				configuration -> streamableKafkaMessageSource
		);
	}*/

	@Bean
	public SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource(ConsumerFactory<String, byte[]> consumerFactory,
																						 Fetcher<String, byte[], EventMessage<?>> fetcher,
																						 KafkaMessageConverter<String, byte[]> messageConverter,
																						 KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer) {
		SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource = SubscribableKafkaMessageSource.<String, byte[]>builder()
				.topics(List.of("topic1"))
				.groupId(groupId)                   // Hard requirement
				.consumerFactory(consumerFactory)   // Hard requirement
				.fetcher(fetcher)                   // Hard requirement
				.messageConverter(messageConverter) // Defaults to a "DefaultKafkaMessageConverter"
				//.autoStart()
				.build();
		// Registering the source is required to tie into the Configurers lifecycle to start the source at the right stage
		kafkaMessageSourceConfigurer.configureSubscribableSource(configuration -> subscribableKafkaMessageSource);
		//this.configureSubscribableKafkaSource(eventProcessingConfigurer, processorName, subscribableKafkaMessageSource);
		return subscribableKafkaMessageSource;
	}

	@Autowired
	public void configureSubscribableKafkaSource(EventProcessingConfigurer eventProcessingConfigurer,
												 SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource) {

		eventProcessingConfigurer.assignProcessingGroup(groupId, processorName);
		eventProcessingConfigurer.registerSubscribingEventProcessor(
				processorName,
				configuration -> subscribableKafkaMessageSource
		);

	}

	@Bean
	KafkaMessageConverter<String, byte[]> kafkaMessageConverter() {
		JacksonSerializer serializer = JacksonSerializer.defaultSerializer();
		return DefaultKafkaMessageConverter.builder()
				.serializer(serializer)
				.sequencingPolicy(SequentialPerAggregatePolicy.instance())
				.build();
	}





}
