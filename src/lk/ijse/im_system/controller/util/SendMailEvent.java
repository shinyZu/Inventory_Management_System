package lk.ijse.im_system.controller.util;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Properties;

public interface SendMailEvent {

    void sendMail() throws SQLException, ClassNotFoundException, MessagingException, IOException;

    Message prepareMessage(Session session, String senderMail, String recipient);

}
