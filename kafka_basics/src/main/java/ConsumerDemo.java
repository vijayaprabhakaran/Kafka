
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());
    public static void main(String[] args) {

        log.info("Consumer program starts running");

        String bootstrapserver = "127.0.0.1:9092";
        String groupId = "my-java-app";
        String toipc = "demo_java";

        Properties properties = new Properties();
        //Consumer Property
        properties.setProperty("bootstrap.servers",bootstrapserver);
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer",StringDeserializer.class.getName());
        properties.setProperty("group.id",groupId);
        properties.setProperty("auto.offset.reset","earliest");

        //Create Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        //subscribe to the topic
        consumer.subscribe(Arrays.asList(toipc));

        //poll for data
        while(true)
        {
            log.info("polling");
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(1000));

            for(ConsumerRecord<String,String> record:records)
            {
                log.info("KEY " +record.key() + "\n"+
                        "Value "+ record.value()+ " \n"+
                        "Partition "+ record.partition()+ " \n"+
                        "Offset "+ record.offset()+ " \n");
            }



        }







    }
}


