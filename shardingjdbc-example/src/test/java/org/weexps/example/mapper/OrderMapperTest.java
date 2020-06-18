package org.weexps.example.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.weexps.example.entity.Order;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 样例测试在分表场景最主要的两个设计多分片行为: 批量插入 和 批量查询
 *
 * @date 2020/6/16 14:23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderMapperTest {

    @Autowired
    OrderMapper orderMapper;

    /**
     * 测试批量查询时 sharding-jdbc 的行为
     */
    @Test
    public void test_selectBatch() {

        final List<Integer> uids = Arrays.asList(1, 2, 3, 4, 5, 6);
        // 初始化一组数据
//        List<Order> orders = uids.stream().map(uid -> {
//            Order order = new Order();
//            order.setAmount(100L + uid);
//            order.setUid(0L + uid);
//            return order;
//        }).collect(Collectors.toList());
//        for (Order order : orders) {
//            orderMapper.insert(order);
//        }


        List<Order> news = orderMapper.selectByUids(uids.stream().map(Long::valueOf).collect(Collectors.toList()));
        for (Order order : news) {
            System.out.println(order);
        }
    }

    /**
     * 测试批量写入时 sharding-jdbc 的行为
     */
    @Test
    public void test_insertBatch() {
        final List<Integer> uids = Arrays.asList(100, 101);
        List<Order> orders = uids.stream().map(uid -> {
            Order order = new Order();
            order.setAmount(100L + uid);
            order.setUid(0L + uid);
            return order;
        }).collect(Collectors.toList());

        orderMapper.insertList(orders);

    }
}
