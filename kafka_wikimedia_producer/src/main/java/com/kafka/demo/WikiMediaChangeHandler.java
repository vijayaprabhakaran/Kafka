package com.kafka.demo;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiMediaChangeHandler implements EventHandler {
    KafkaProducer<String,String> kafkaProducer;
    String topic;
    private final Logger logger = LoggerFactory.getLogger(WikiMediaChangeHandler.class.getSimpleName());

    public WikiMediaChangeHandler(KafkaProducer<String,String> kafkaProducer,String topic)
    {
        this.kafkaProducer = kafkaProducer;
        this.topic = topic;

    }

    @Override
    public void onOpen(){

        //nothing

    }

    @Override
    public void onClosed() {

        kafkaProducer.close();

    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {

        logger.info(messageEvent.getData());

        //asynchronize
        kafkaProducer.send(new ProducerRecord<>(topic,messageEvent.getData()));

    }

    @Override
    public void onComment(String comment)  {

        //nothing

    }

    @Override
    public void onError(Throwable t) {

        logger.error("error in stream reading",t);
    }
}
