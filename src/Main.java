import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;


public class Main{

    public static Simple s;
    public static File[] playlist;
    protected static String dir = System.getProperty("user.home")+"\\Documents\\Playlist";
    public static Clip[] clipArray;

    public static void main(String[] args) {
        playListNameDump nameDump = new playListNameDump();
        playlist = nameDump.dirToFile(dir);
        nameDump.playlistCompile(playlist);
        clipArray = new Clip[playlist.length];

        Thread t1 = new Thread(){
            public void run(){
                Runnable GUI = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            s = new Simple();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                };

                SwingUtilities.invokeLater(GUI);
            }
        };

        Thread t2 = new Thread(){
            @Override
            public void run(){
                LoadSound load = new LoadSound();
                load.compileClip(clipArray,playlist);
            }
        };

        Thread t3 = new Thread(){
            @Override
            public void run(){
                for(int i = 0; i < playlist.length; i++){
                    Simple.threadCheckLyricsImageFile(i);
                }
            }
        };

        t2.start();
        try{
            t2.join();
        } catch (Exception e){
            e.printStackTrace();
        }
        t3.start();
        t1.start();
    }

}
