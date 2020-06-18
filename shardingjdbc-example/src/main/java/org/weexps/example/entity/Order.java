package org.weexps.example.entity;

/**
 * @date 2020/6/16 14:12
 */

public class Order {

    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 订单金额
     */
    private Long amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", uid=" + uid +
                ", amount=" + amount +
                '}';
    }
}
