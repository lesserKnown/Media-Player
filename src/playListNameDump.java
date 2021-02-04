import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;

public class playListNameDump {
    public static DefaultListModel<String> l1 = new DefaultListModel<>();

    public void playlistCompile(File[] filePath){
        l1.removeAllElements();
        for(int i = 0 ; i < filePath.length; i++){
            if (filePath[i].isFile()) {
                l1.addElement(filePath[i].getName().replace(".wav", ""));
            }
        }
    }

    public File[] dirToFile(String path){
        File dir = new File(path);
        File[] list;
        if(dir.isDirectory()){
            FilenameFilter wavFilter = new FilenameFilter(){
              public boolean accept(File dir, String name){
                  String lowercaseName = name.toLowerCase();
                  if(lowercaseName.endsWith(".wav")){
                      return true;
                  }
                  else{
                      return false;
                  }
              }
            };
            list = dir.listFiles(wavFilter);
        }
        else{
            return null;
        }
        return list;
    }

}
