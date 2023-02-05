import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {


        //System.out.println("Runs");

        log.info("Producer program starts running");

        /*
        1. create producer property
        2. create create the producer
        3. send data
        4. flush the data and close producer

         */

        String bootstrapserver = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers",bootstrapserver);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        //create Producer Record
        ProducerRecord<String,String> record = new ProducerRecord<>("demo_java","hello world");

        producer.send(record);

        producer.flush();

        producer.close();




    }
}
