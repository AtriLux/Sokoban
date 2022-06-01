package game.sokoban.gameplay.services;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class Sounds {

    private MediaPlayer player;
    private final Media mainTheme = new Media(Objects.requireNonNull(getClass().getResource("/sounds/mainTheme.mp3")).toString());
    private final Media gameTheme = new Media(Objects.requireNonNull(getClass().getResource("/sounds/sound.mp3")).toString());
    private final Media move = new Media(Objects.requireNonNull(getClass().getResource("/sounds/move.wav")).toString());
    private final Media moveSpike = new Media(Objects.requireNonNull(getClass().getResource("/sounds/moveSpike.wav")).toString());
    private final Media moveChest = new Media(Objects.requireNonNull(getClass().getResource("/sounds/moveChest.mp3")).toString());
    private final Media kick = new Media(Objects.requireNonNull(getClass().getResource("/sounds/kick.wav")).toString());
    private final Media gameOver = new Media(Objects.requireNonNull(getClass().getResource("/sounds/gameover.mp3")).toString());

    private void playSound(Media sound) {
        player = new MediaPlayer(sound);
        player.play();
    }

    private void playMusic(Media music) {
        if (player != null) player.stop();
        player = new MediaPlayer(music);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(0.1);
        player.play();
    }

    public void playMainMusic() { playMusic(mainTheme); }
    public void playGameMusic() { playMusic(gameTheme); }
    public void playMoveSound() { playSound(move); }
    public void playSpikeSound() { playSound(moveSpike); }
    public void playChestSound() { playSound(moveChest); }
    public void playKickSound() { playSound(kick); }
    public void playGameOverSound() { playSound(gameOver); }
}
