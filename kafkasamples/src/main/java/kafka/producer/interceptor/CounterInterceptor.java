package kafka.producer.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String, String> {
    private int errorCounter = 0;
    private int successCounter = 0;
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        // 一定要返回原始数据，不能为Null
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        /**
         * 统计成功和失败次数
         */
        if (e==null){
            successCounter++;
        }
        else {
            errorCounter++;
        }

    }

    @Override
    public void close() {
/**
 * 保存结果
 */
    System.out.println("成功" + successCounter);
    System.out.println("失败" + errorCounter);
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
