package com.example.demo.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;


public class ConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("Started Consumer Demo");

        String bootstrap = "127.0.0.1:9092";

        String group_name  = "my-demo-app";

        String topic_name = "demo_java";

        //Create consumer config
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,group_name);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        //Create Kafka Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        //subscribe consumer to our topics
        consumer.subscribe(Arrays.asList(topic_name));

        while(true)
        {
            logger.info("POlling");
            ConsumerRecords<String,String> records =
                    consumer.poll(Duration.ofMillis(1000));

            for(ConsumerRecord<String,String> record : records)
            {
                logger.info("Key : "+record.key() + ", Value " + record.value() +
                        "partitions : "+record.partition() + ", Offset " + record.offset());
            }
        }






    }
}
