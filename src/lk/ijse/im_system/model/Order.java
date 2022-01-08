package lk.ijse.im_system.model;

public class Order {
    private String orderId;
    private String orderDate;
    private String wardNo;

    public Order() {}

    public Order(String orderId, String orderDate, String wardNo) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setWardNo(wardNo);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", wardNo='" + wardNo + '\'' +
                '}';
    }
}
