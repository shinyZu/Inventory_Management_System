package lk.ijse.im_system.controller.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTime {

    public Label lblTime;
    public Label lblDate;
    public Label lblDay;

    public static String date;

    public void loadDateAndTime() {

        // load Time
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss");
            Calendar cal2 = new GregorianCalendar();
            Date date1 = cal2.getTime();
            lblTime.setText(simpleTimeFormat.format(date1));

        }),
                new KeyFrame(Duration.seconds(1))
        );

        time.setCycleCount(Animation.INDEFINITE);
        time.play();

        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        DateTime.date = lblDate.getText();
        
        // load Day
        DateFormat formatter = new SimpleDateFormat("EEEE", Locale.forLanguageTag("en"));
        Calendar cal1 = new GregorianCalendar();
        Date day = cal1.getTime();
        lblDay.setText(formatter.format(day));
    }
}
