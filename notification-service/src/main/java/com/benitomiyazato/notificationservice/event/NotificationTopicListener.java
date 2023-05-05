package com.benitomiyazato.notificationservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationTopicListener {

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // send an email notification
        log.info("Received notification from order service. Order number: {}", orderPlacedEvent.orderNumber());
    }
}
