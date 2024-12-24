package KakuroController;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.util.Objects;

public class MusicPlayer {
    private static MusicPlayer instance;
    private Clip clip;
    private boolean isPlaying = true;
    private String currentSong = null;

    //Dảm bảo rằng chỉ có một thể hiện duy nhất của lớp được tạo
    public static synchronized MusicPlayer getInstance(){// tu khao synchronized chi cho phep 1 luong duoc chay tai 1 thoi diem
        if(instance==null) instance = new MusicPlayer();
        return instance;
    }
    public void playMusic(String filename){
        stopMusic();
        try{
            // Nhan luong dau vao
            BufferedInputStream audioInputStream = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(filename)));
            AudioInputStream stream = AudioSystem.getAudioInputStream(audioInputStream);
            //Tao 1 clip va chay
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying = true;
            setVolume(0.7f);
            System.out.println(currentSong + "1");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void stopMusic(){
        if(clip!= null){
            clip.stop();
            clip.close();
            clip = null;
            isPlaying = false;
        }
    }
    //Dieu chinh am luong tu 0.0 den 1.0
    public void setVolume(float volume){
        if(clip!=null){
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            //Chuyen doi tu 0.0 - 1.0 sang db
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            float gain = volumeControl.getMinimum() + (range*volume);
            volumeControl.setValue(gain);
        }
    }
    public void toggleMusic(){
        if(isPlaying){
            stopMusic();
        }else{
            playMusic(currentSong);
        }
    }

    public String getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(String currentSong) {
        this.currentSong = currentSong;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
