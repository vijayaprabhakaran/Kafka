package com.kafka.demo;


import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikiMediaProducer {
    private static final Logger logger =  LoggerFactory.getLogger(WikiMediaProducer.class.getSimpleName());
    public static void main(String[] args) throws InterruptedException{

        logger.info("PROGRAM STARTS HERE");

        String bootstrap_server = "127.0.0.1:9092";
        String topic = "wikimedia.recentchange";

        //Create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //High through put producer configs
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,"20"); //how long to wait until send the next batch
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32*1024)); //bunch of messages to create a bunch(increases performance)
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");

        //Create Kafka Producer

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        //Eventhandler to handle the events to send to producer
        EventHandler eventHandler = new WikiMediaChangeHandler(producer,topic);
        String url =  "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler,URI.create(url));
        EventSource eventSource = builder.build();


        //start the producer in another thread
        eventSource.start();

        // we can produce for 10 mins and block the program until then
        TimeUnit.MINUTES.sleep(10);





    }
}
