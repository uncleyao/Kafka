import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.streams.processor.To;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Properties;

public class KafkaConsumerAssignApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id","test");

        // 创建消费者对象
        KafkaConsumer myConsumer = new KafkaConsumer(props);


        ArrayList<TopicPartition> partitions = new ArrayList<TopicPartition>();
        TopicPartition myTopicPart0 = new TopicPartition("my-topic", 0);

        partitions.add(myTopicPart0);

        myConsumer.assign(partitions);
        try {
            while (true) {

                ConsumerRecords<String, String> records = myConsumer.poll(10);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("topics: %s, Partition: %d, key: %s, Value: %s",
                            record.topic(),record.partition(),record.offset(), record.key(), record.value()));
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            myConsumer.close();
        }
    }
}
