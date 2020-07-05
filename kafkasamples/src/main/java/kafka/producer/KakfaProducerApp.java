package kafka.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class KakfaProducerApp {
    public static void  main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("acks","1");
        //一批消息处理大小
        props.put("batch.size",16384);
        // 请求延时
        props.put("linger.ms",1);
        // 发送缓冲区内存大小
        props.put("buffer.memory",33554432);

        // 增加分区类
        props.put("partitioner.class","kafka.producer.MyPartitioner");

        // 配置拦截器
        List<String> interceptors = new ArrayList<String>();
        interceptors.add("kafka.producer.interceptor.CounterInterceptor");
        interceptors.add("kafka.producer.interceptor.TimeInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);

        
        /**
         * 定义一个producer
         * */
        KafkaProducer<String, String> myProducer = new KafkaProducer<String, String>(props);

        try {
            for (int i=0;i< 5;i++) {
                /**
                 * send是个异步过程
                 */
                // ProducerRecord record = new ProducerRecord<>("my-topic", Integer.toString(i), "MyMessage: " + Integer.toString(i));
                ProducerRecord record = new ProducerRecord<>("my-topic2", "MyMessage: " + Integer.toString(i));
                // 有个default partitioner

                /** 异步操作 */
                // myProducer.send(record);
                /** 同步操作 */
                // myProducer.send(record).get();

                /** 异步操作，增加回调函数 */
                myProducer.send(record, new Callback() {
                    /** 回调方法 */
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        // 发送哪个分区
                        System.out.println(recordMetadata.partition());
                        // 发送数据偏移量
                        System.out.println(recordMetadata.offset());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myProducer.close();
        }
    }
}
