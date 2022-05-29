package game.sokoban.gameplay;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Timers {
    int sec = 0;
    int min = 0;
    boolean isActive = true;

    public Timers(Label statusTimer, LvlChanger lvlChanger) {
        Thread timer = new Thread(() -> {
            while(isActive) {
                try {
                    Platform.runLater(() -> statusTimer.setText(" Время: " + setTime()));
                    Thread.sleep(1000);
                    if (lvlChanger.isSingleGame()) {
                        if (!lvlChanger.isOpenWindow())
                            sec++;
                    } else sec++;
                    if (sec == 60) {
                        sec = 0;
                        min++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
    }

    private String setTime() {
        String str;
        if (min < 10) str = "0" + min + ":";
        else str = min + ":";
        if (sec < 10) str += "0" + sec;
        else str += sec + "";
        return str;
    }

    public void stop() { isActive = false; }

    public int getTime() { return min*60 + sec - 1; }
}
