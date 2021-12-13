package com.company;

import javax.swing.*;
import java.util.Timer;  // чтоб таймер работал
import java.util.TimerTask;

/**
 * класс оисывающий таймер, наследует свойства класса JLabel
 */
public class TimerLabel extends JLabel {
    java.util.Timer timer = new Timer(); //объект класса таймер

    TimerLabel(){
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    /**
     * отображение времени
     */
    TimerTask timerTask = new TimerTask() {
        volatile int time;
        Runnable refresher = new Runnable() {
            public void run() {
                TimerLabel.this.setText(String.format("%02d : %02d", time/60, time % 60));
            }
        };

        public void run() {
            time++;
            SwingUtilities.invokeLater(refresher);
        }
    };

    /**
     * остановка таймера
     */
    void stopTimer() {
        timer.cancel();
    }
}
