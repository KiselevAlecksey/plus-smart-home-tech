package ru.yandex.practicum.kafka.serializer;

import com.google.protobuf.Message;
import org.apache.kafka.common.serialization.Serializer;

public class ProtobufKafkaSerializer implements Serializer<Message> {
    @Override
    public byte[] serialize(String topic, Message data) {
        return data != null ? data.toByteArray() : null;
    }
}
