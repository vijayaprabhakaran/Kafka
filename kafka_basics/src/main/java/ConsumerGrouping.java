
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerGrouping {
    private static final Logger log = LoggerFactory.getLogger(ConsumerGrouping.class.getSimpleName());
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

        //get a reference of the main Thread
        final Thread mainThread = Thread.currentThread();

        //adding shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run(){
                log.info("Detected a shutdown, lets exist by callling consumer.wakeup()....");
                consumer.wakeup();

                //join the main Thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            //subscribe to the topic
            consumer.subscribe(Arrays.asList(toipc));

            //poll for data
            while (true) {
              //  log.info("polling");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("KEY " + record.key() + "\n" +
                            "Value " + record.value() + " \n" +
                            "Partition " + record.partition() + " \n" +
                            "Offset " + record.offset() + " \n");
                }


            }
        }
        catch (WakeupException e)
        {
            log.info("Consumer is starting to shut down");
        }
        catch (Exception e)
        {
            log.error("Unexcepted exception ",e);
        }
        finally {
            consumer.close();
            log.info("Consumer is gracefully closed");
        }








    }
}


