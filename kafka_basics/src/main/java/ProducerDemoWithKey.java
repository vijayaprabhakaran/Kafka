import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class ProducerDemoWithKey {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithKey.class.getSimpleName());

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

        //ROUND ROBIN (not recommented because it sent every batch to diff partition
        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());
        //set smaller batch size for showing sticky
        //properties.setProperty("batch.size","400");

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int j=0;j<2;j++)
        {
            for(int i=0;i<10;i++)
            {

                String topic = "demo_java";
                String key = "id_"+i;
                String value = "hello world "+i;
                //create Producer Record
                ProducerRecord<String,String> record = new ProducerRecord<>(topic,key,value);

                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {

                        if(exception==null)
                        {
                            log.info("Key "+ key+ "| Partition "+ metadata.partition() );
                        }
                        else {
                            log.error("Error while parsing",exception);
                        }


                    }
                });
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        producer.flush();

        producer.close();




    }
}
