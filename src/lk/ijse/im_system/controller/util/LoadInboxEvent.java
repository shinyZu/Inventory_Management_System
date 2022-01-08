package lk.ijse.im_system.controller.util;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public interface LoadInboxEvent {

    void updateMailBox() throws MessagingException;

    Folder fetchInbox() throws MessagingException;

    default Message[] fetchMessages(boolean read) throws MessagingException {
        Folder inbox = fetchInbox();

        /**use READ_ONLY if you don't wish the messages
         * to be marked as read after retrieving its content
         */

        inbox.open(Folder.READ_WRITE);

        /**search for all "unseen" messages*/

        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, read);
        return inbox.search(unseenFlagTerm);
    }

    default ArrayList<String> sortMessages(ArrayList<String> msgList, Message[] msgs) throws MessagingException {
        /**Sort messages from recent to oldest*/

        Arrays.sort( msgs, (m1, m2 ) -> {
            try {
                return m2.getSentDate().compareTo( m1.getSentDate() );
            } catch ( MessagingException e ) {
                throw new RuntimeException( e );
            }
        } );

        for ( Message unreadMsg : msgs ) {
            msgList.add(unreadMsg.getSubject());
            //message.setFlag(Flags.Flag.SEEN, true);
        }

        ArrayList<String> uniqueList = null;

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(msgList);
        uniqueList = new ArrayList<>(hashSet);

        return uniqueList;
    }
}
