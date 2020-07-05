package kafka.consumer;


import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.cluster.BrokerEndPoint;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;


import java.nio.ByteBuffer;
import java.util.Arrays;

public class LowApiConsumer {
    @SuppressWarnings("all")
    public static void main(String[] args) throws Exception {
        /**
         * 构造低级API，关键在于获取分区里leader的信息
         */
        BrokerEndPoint leader = null;

        // host和port应该是指定分区的leader，所以必须要获取分区leader
        SimpleConsumer metaConsumer = new SimpleConsumer("localhost",9092,500,10*1024,"metadata");
        // 获取元数据信息
        TopicMetadataRequest request = new TopicMetadataRequest(Arrays.asList("my-topic"));
        TopicMetadataResponse response = metaConsumer.send(request);

        leaderLabel:
        for (TopicMetadata topicsMetadatum : response.topicsMetadata()) {
            if ("my-topic".equals(topicsMetadatum.topic())){
                //关心的主题元数据信息
                for (PartitionMetadata partitionsMetadatum : topicsMetadatum.partitionsMetadata()) {
                    int partId = partitionsMetadatum.partitionId();
                    if (partId==0){
                        //关心的分区元数据信息
                        leader = partitionsMetadatum.leader();
                        break leaderLabel;
                    }
                }
            }
        }

        if (leader==null){
            System.out.println("分区不正确");
            return;
        }



         //创建简单消费者
        SimpleConsumer consumer = new SimpleConsumer(leader.host(),leader.port(),500,10*1024,"accessLeader");

        //抓取数据
        FetchRequest req = new FetchRequestBuilder().addFetch("my-topic",0,5,10*1024).build();
        FetchResponse resp = consumer.fetch(req);

        //消息集
        ByteBufferMessageSet messageSet  = resp.messageSet("my-topic",0);
        for (MessageAndOffset messageAndOffset : messageSet) {
            ByteBuffer buffer = messageAndOffset.message().payload(); //真正value
            //将buffer转换字符串
            byte[] bs = new byte[buffer.limit()];
            buffer.get(bs);
            String value = new String(bs,"UTF-8");
            System.out.println(value);

        }
    }
}
