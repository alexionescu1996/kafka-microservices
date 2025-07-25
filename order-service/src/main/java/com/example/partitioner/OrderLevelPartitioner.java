package com.example.partitioner;

import com.example.model.Order;
import com.example.model.OrderEvent;
import com.example.model.OrderStatus;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class OrderLevelPartitioner
        implements Partitioner {

    private static final Logger log = LoggerFactory.getLogger(OrderLevelPartitioner.class);

    @Override
    public int partition(String topic,
                         Object objectKey,
                         byte[] keyBytes,
                         Object value,
                         byte[] valueBytes,
                         Cluster cluster) {

        String key = (String) objectKey;
        OrderEvent order = (OrderEvent) value;

        var condition = key != null
                && order != null
                && order.getOrderStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL);

        return condition ? 0 : findRandomPartition(cluster, topic);
    }

    private int findRandomPartition(Cluster cluster, String topic) {
        List<PartitionInfo> partitionInfoList = cluster.availablePartitionsForTopic(topic);
        log.info("cluster availablePartitionsForTopic :: {}, topic :: {}",
                partitionInfoList.size(), topic);

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
