import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class ProducerDemoPartitionerClass {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoPartitionerClass.class.getSimpleName());

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
        properties.setProperty("batch.size","400");

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int j=0;j<10;j++)
        {
            for(int i=0;i<30;i++)
            {
                //create Producer Record
                ProducerRecord<String,String> record = new ProducerRecord<>("demo_java","hello world");

                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {

                        if(exception==null)
                        {
                            log.info("received new metadata \n"+ "" +
                                    "Topic "+ metadata.topic()+ "\n"+
                                    "Partition "+ metadata.partition() + "\n"+
                                    "Offset"+ metadata.offset()+ "\n"+
                                    "timestamp"+ metadata.timestamp());
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
