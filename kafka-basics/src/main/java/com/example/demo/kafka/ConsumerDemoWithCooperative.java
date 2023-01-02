package com.example.demo.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


public class ConsumerDemoWithCooperative {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemoWithCooperative.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("Started Consumer Demo");

        String bootstrap = "127.0.0.1:9092";

        String group_name  = "my-third-app";

        String topic_name = "demo_java";

        //Create consumer config
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,group_name);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        //Create Kafka Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        // get a reference to the current Thread
        final Thread mainThread = Thread.currentThread();

        //adding the shutdown hook

        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run()
            {
                logger.info("Detected a shutdown, lets exit by calling consumer.wakeup().... ");
                consumer.wakeup();

                //join the main Thread to allow the execution of the code in main thread.

                try{
                    mainThread.join();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        try
        {
            //subscribe consumer to our topics
            consumer.subscribe(Arrays.asList(topic_name));

            while(true)
            {
                //logger.info("POlling");
                ConsumerRecords<String,String> records =
                        consumer.poll(Duration.ofMillis(1000));

                for(ConsumerRecord<String,String> record : records)
                {
                    logger.info("Key : "+record.key() + ", Value " + record.value() +
                            "partitions : "+record.partition() + ", Offset " + record.offset());
                }
            }
        }
        catch (WakeupException e)
        {
            logger.info("Wake up exception");
            //this is an expected exception while closing the consumer
        }
        catch (Exception e)
        {
            logger.error("Unexpected Exceptions");
        }
        finally {
            consumer.close();
            logger.info("Consumer is gracefully closed");
        }






    }
}
