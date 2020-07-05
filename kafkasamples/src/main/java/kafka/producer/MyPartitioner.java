package kafka.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区算法类
 */
public class MyPartitioner implements Partitioner {

    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        //可以自定义partition，但是在建topic的时候需要确保partition大于它
        // 最简单 return 2;
        String val = (String)o1;
        int result = val.hashCode()^ Integer.MAX_VALUE;
        return result%2;

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
