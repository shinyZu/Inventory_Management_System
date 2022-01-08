package lk.ijse.im_system.controller.util;

import java.sql.SQLException;

public interface FilterComboBoxEvent {

    void setFieldsOnInvNo() throws SQLException, ClassNotFoundException;

    void setFieldsOnDescrip() throws SQLException, ClassNotFoundException;
}
