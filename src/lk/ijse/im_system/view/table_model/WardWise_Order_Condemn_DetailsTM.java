package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class WardWise_Order_Condemn_DetailsTM {
    private String order_condemn_Id;
    private String wardNo;
    private String invNo;
    private String description;
    private int qtyOrdered_Condemned;
    private String date_Ordered_Condemned;

    public WardWise_Order_Condemn_DetailsTM() {}

    public WardWise_Order_Condemn_DetailsTM(String order_condemn_Id, String wardNo, String invNo, String description, int qtyOrdered_Condemned, String date_Ordered_Condemned) {
        this.setOrder_condemn_Id(order_condemn_Id);
        this.setWardNo(wardNo);
        this.setInvNo(invNo);
        this.setDescription(description);
        this.setQtyOrdered_Condemned(qtyOrdered_Condemned);
        this.setDate_Ordered_Condemned(date_Ordered_Condemned);
    }

    public String getOrder_condemn_Id() {
        return order_condemn_Id;
    }

    public void setOrder_condemn_Id(String order_condemn_Id) {
        this.order_condemn_Id = order_condemn_Id;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyOrdered_Condemned() {
        return qtyOrdered_Condemned;
    }

    public void setQtyOrdered_Condemned(int qtyOrdered_Condemned) {
        this.qtyOrdered_Condemned = qtyOrdered_Condemned;
    }

    public String getDate_Ordered_Condemned() {
        return date_Ordered_Condemned;
    }

    public void setDate_Ordered_Condemned(String date_Ordered_Condemned) {
        this.date_Ordered_Condemned = date_Ordered_Condemned;
    }

    @Override
    public String toString() {
        return "WardWise_Order_Condemn_DetailsTM{" +
                "order_condemn_Id='" + order_condemn_Id + '\'' +
                ", wardNo='" + wardNo + '\'' +
                ", invNo='" + invNo + '\'' +
                ", description='" + description + '\'' +
                ", qtyOrdered_Condemned=" + qtyOrdered_Condemned +
                ", date_Ordered_Condemned='" + date_Ordered_Condemned + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WardWise_Order_Condemn_DetailsTM that = (WardWise_Order_Condemn_DetailsTM) o;
        return qtyOrdered_Condemned == that.qtyOrdered_Condemned &&
                Objects.equals(order_condemn_Id, that.order_condemn_Id) &&
                Objects.equals(wardNo, that.wardNo) &&
                Objects.equals(invNo, that.invNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(date_Ordered_Condemned, that.date_Ordered_Condemned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order_condemn_Id, wardNo, invNo, description, qtyOrdered_Condemned, date_Ordered_Condemned);
    }
}
