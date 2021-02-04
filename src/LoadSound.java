import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class LoadSound {

    protected AudioInputStream audioInputStream;

    LoadSound(){
    }

    public Clip setClip(JList j, Clip c){
        if(c != null){
            c.stop();
        }
        int index = j.getSelectedIndex();
        c = Main.clipArray[index];
        return c;
    }

    public void compileClip(Clip[] clips, File[] files){
        for(int i = 0; i < files.length; i++){
            try {
                audioInputStream = AudioSystem.getAudioInputStream(files[i]);
                clips[i] = AudioSystem.getClip();
                clips[i].open(audioInputStream);
                clips[i].addLineListener(Simple.generateLineListener());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

}
