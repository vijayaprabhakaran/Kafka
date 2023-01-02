package com.example.demo.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class ProducerDemoWithKeys {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("Started Producer Demo");

        //Producer property

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create the Kafka producer

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);



        for(int i=1;i<=10;i++)
        {
            String topic = "demo_java";
            String Key = "id"+i;
            String Value = "hello world"+i;

            //create a producer record
            ProducerRecord<String,String> producerRecord =
                    new ProducerRecord<>(topic,Value , Key);

            //Callback
            //send data - asynchronous
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(exception == null)
                    {
                        logger.info("Received Metadata/ \n" +
                                "Topic : " + metadata.topic() + "\n" +
                                "Key : " + producerRecord.key() + "\n" +
                                "Partition : " + metadata.partition() + "\n" +
                                "Offset : " + metadata.offset() + "\n" +
                                "Timestamp : " + metadata.timestamp());
                    }
                    else {
                        logger.info("Error while producing records : \n  *********"+ exception + "\n *********");

                    }
                }
            });

           /* try{
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }*/

        }

        //flush and close the producer - asynchronous
        producer.flush();

        //close the producer
        producer.close();
    }
}
