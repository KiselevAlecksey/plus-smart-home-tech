package ru.yandex.practicum.telemetry.aggregator.service.client;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Map;

public interface Client {
    Producer<String, SpecificRecordBase> getProducer();

    Map<String, String> getProducerTopics();

    Consumer<String, SpecificRecordBase> getConsumer();

    Map<String, String> getConsumerTopics();

    void stop();
}