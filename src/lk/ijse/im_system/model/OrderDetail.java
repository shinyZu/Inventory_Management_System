package lk.ijse.im_system.model;

public class OrderDetail {

    private String orderId;
    private String invNo;
    private int orderQty;

    public OrderDetail() {}

    public OrderDetail(String orderId, String invNo, int orderQty) {
        this.setOrderId(orderId);
        this.setInvNo(invNo);
        this.setOrderQty(orderQty);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                ", invNo='" + invNo + '\'' +
                ", orderQty=" + orderQty +
                '}';
    }
}
