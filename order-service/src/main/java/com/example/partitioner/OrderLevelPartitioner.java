package com.example.partitioner;

import com.example.model.Order;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class OrderLevelPartitioner
        implements Partitioner {

    @Override
    public int partition(String topic,
                         Object objectKey,
                         byte[] keyBytes,
                         Object value,
                         byte[] valueBytes,
                         Cluster cluster) {

        Long key = (Long) objectKey;
        Order order = (Order) value;
        var condition = key != null && order != null && order.getIsDelivered();

        return condition ? 0 : findRandomPartition(cluster, topic);
    }

    private int findRandomPartition(Cluster cluster, String topic) {
        List<PartitionInfo> partitionInfoList = cluster.availablePartitionsForTopic(topic);

        Random random = new Random();
        return random.nextInt(1, partitionInfoList.size());
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
