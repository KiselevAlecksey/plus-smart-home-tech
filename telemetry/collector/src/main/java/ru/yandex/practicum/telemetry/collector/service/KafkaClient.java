package ru.yandex.practicum.telemetry.collector.service;

import com.google.protobuf.Message;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient {

    Producer<String, Message> getProducer();

    Consumer<String, Message> getConsumer();

    void stop();
}
