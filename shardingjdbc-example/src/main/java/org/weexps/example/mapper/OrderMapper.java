package org.weexps.example.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.weexps.example.entity.Order;

import java.util.List;

/**
 * @date 2020/6/16 14:13
 */
@Mapper
@Repository
public interface OrderMapper {

    @Insert("insert into tb_order (uid, amount) values (#{uid}, #{amount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    void insertList(@Param("orders") List<Order> orders);

    @Select("select * from tb_order where uid = #{uid}")
    void selectByUid(@Param("uid") Long uid);

    List<Order> selectByUids(@Param("uids") List<Long> uids);


}
