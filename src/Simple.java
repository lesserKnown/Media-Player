import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Simple{

    protected static Clip clip;
    protected Container pane;
    protected static JSlider progress;
    protected static JButton button, forwardButton,backButton;
    //protected static JButton stopButton;
    protected static JList playListMenu;
    protected static LoadSound load;

    protected static int index = 0, framePos = 0;
    protected static JToggleButton replayButton, shuffleButton;
    protected ImageIcon pauseIcon, playIcon;
    protected static JTextArea lyricsText;
    protected static JLabel cover;
    protected static JFrame frame;

    protected static String mainDir;
    public static String currentName;
    protected static File lyrics;
    protected static File image;
    public static BufferedImage imgAlbum = null;
    protected static String lyricsFileContent = "";
    protected static BufferedImage defaultImage = null;

    protected static Random random;

    public Simple() throws MalformedURLException {
        mainDir = System.getProperty("user.home")+"\\Documents";
        lyrics = new File(mainDir+"\\Lyrics\\" + currentName +".txt");
        image = new File(mainDir + "\\Album Arts\\" + currentName + ".jpg");
        imgAlbum = null;
        lyricsFileContent = "";

        BufferedImage img = null,imgA = null,imgB = null,imgC = null,imgD = null,imgE = null,imgF = null,imgG = null;
        URL urlPlaylist = new URL("https://img.icons8.com/windows/32/000000/playlist.png");
        URL url = new URL("https://img.icons8.com/ios-glyphs/30/000000/play.png");
        URL urlPause = new URL("https://img.icons8.com/metro/52/000000/pause.png");
        URL urlReplay = new URL("https://img.icons8.com/ios-glyphs/30/000000/replay.png");
        //URL urlStop = new URL("https://img.icons8.com/ios-glyphs/26/000000/stop.png");
        URL urlForward = new URL("https://img.icons8.com/ios-filled/50/000000/fast-forward.png");
        URL urlBackward = new URL("https://img.icons8.com/ios-filled/50/000000/rewind.png");
        URL urlShuffle = new URL("https://img.icons8.com/android/24/000000/shuffle.png");
        try {
            img = ImageIO.read(url);
            imgA = ImageIO.read(urlPlaylist);
            imgB = ImageIO.read(urlPause);
            imgC = ImageIO.read(urlReplay);
            //imgD = ImageIO.read(urlStop);
            imgE = ImageIO.read(urlForward);
            imgF = ImageIO.read(urlBackward);
            imgG = ImageIO.read(urlShuffle);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame = new JFrame(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(340,550);
            }
        };
        frame.setIconImage(imgA);
        pane = frame.getContentPane();
        BoxLayout layout = new BoxLayout(pane,BoxLayout.Y_AXIS);
        pane.setLayout(layout);

        button = new JButton();
        button.setSize(30,30);
        button.setBackground(Color.white);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        /*stopButton = new JButton();
        stopButton.setSize(28,28);
        stopButton.setBackground(Color.white);
        stopButton.setBorderPainted(false);
        stopButton.setFocusPainted(false);*/

        shuffleButton = new JToggleButton();
        shuffleButton.setSize(30,28);
        shuffleButton.setBackground(Color.white);
        shuffleButton.setBorderPainted(false);
        shuffleButton.setFocusPainted(false);

        forwardButton = new JButton();
        forwardButton.setSize(30,30);
        forwardButton.setBackground(Color.white);
        forwardButton.setBorderPainted(false);
        forwardButton.setFocusPainted(false);

        backButton = new JButton();
        backButton.setSize(30,30);
        backButton.setBackground(Color.white);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);

        replayButton = new JToggleButton();
        replayButton.setSize(30,30);
        replayButton.setBackground(Color.white);
        replayButton.setBorderPainted(false);
        replayButton.setFocusPainted(false);


        JPanel albumArt = new JPanel();
        cover = new JLabel();
        SpringLayout artLayout = new SpringLayout();
        albumArt.setLayout(artLayout);
        artLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,cover,0,SpringLayout.HORIZONTAL_CENTER,albumArt);
        artLayout.putConstraint(SpringLayout.VERTICAL_CENTER,cover,0,SpringLayout.VERTICAL_CENTER,albumArt);
        albumArt.add(cover);
        albumArt.setBackground(Color.black);
        Dimension max = new Dimension(300,410);
        JTabbedPane tabs = new JTabbedPane(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(max);
            }
        };
        tabs.add("Cover",albumArt);
        lyricsText = new JTextArea();
        JScrollPane lyrics = new JScrollPane(lyricsText);

        lyrics.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        lyricsText.setEditable(false);
        lyricsText.setLineWrap(true);
        tabs.add("Lyrics",lyrics);

        load = new LoadSound();
        currentName = Main.playlist[index].getName().replace(".wav","");
        checkLyricsImageFile();

        JScrollPane playlistTab = new JScrollPane();
        playListMenu = new JList(playListNameDump.l1);
        playListMenu.setSelectedIndex(0);
        clip = load.setClip(playListMenu,clip);
        playlistTab.setViewportView(playListMenu);
        tabs.add("Playlist", playlistTab);

        progress = new JSlider();
        progress.setEnabled(true);
        progress.setBackground(Color.white);

        JPanel buttonPanel = new JPanel(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(300,60);
            }

            @Override
            public Dimension getMaximumSize(){
                return new Dimension(300,80);
            }
        };
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        JPanel buttonPanel2 = new JPanel(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(300,40);
            }
        };
        buttonPanel2.setLayout(new GridLayout(1,5));

        Image dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
        Image dimgB = imgB.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
        Image dimgC = imgC.getScaledInstance(replayButton.getWidth()-1,replayButton.getHeight()-1,Image.SCALE_SMOOTH);
        //Image dimgD = imgD.getScaledInstance(stopButton.getWidth()+1,stopButton.getHeight()+1,Image.SCALE_SMOOTH);
        Image dimgE = imgE.getScaledInstance(forwardButton.getWidth(),forwardButton.getHeight(),Image.SCALE_SMOOTH);
        Image dimgF = imgF.getScaledInstance(backButton.getWidth(),backButton.getHeight(),Image.SCALE_SMOOTH);
        Image dimgG = imgG.getScaledInstance(shuffleButton.getWidth(),shuffleButton.getHeight(),Image.SCALE_SMOOTH);
        playIcon = new ImageIcon(dimg);
        pauseIcon = new ImageIcon(dimgB);
        ImageIcon replayIcon = new ImageIcon(dimgC);
        //ImageIcon stopIcon = new ImageIcon(dimgD);
        ImageIcon forwardIcon = new ImageIcon(dimgE);
        ImageIcon backIcon = new ImageIcon(dimgF);
        ImageIcon shuffleIcon = new ImageIcon(dimgG);
        button.setIcon(playIcon);
        replayButton.setIcon(replayIcon);
        //stopButton.setIcon(stopIcon);
        forwardButton.setIcon(forwardIcon);
        backButton.setIcon(backIcon);
        shuffleButton.setIcon(shuffleIcon);

        JMenu menu = new JMenu("Open Playlist");
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        menuBar.setBackground(Color.white);
        frame.setJMenuBar(menuBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane.setBackground(Color.white);
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(progress);
        buttonPanel.add(buttonPanel2);
        buttonPanel2.add(replayButton);
        buttonPanel2.add(backButton);
        buttonPanel2.add(button);
        buttonPanel2.add(forwardButton);
        buttonPanel2.add(shuffleButton);
        //buttonPanel2.add(stopButton);
        pane.add(tabs);
        pane.add(buttonPanel);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);

        progress.setMaximum(clip.getFrameLength());

        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent menuEvent) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select your playlist");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fc.showOpenDialog(null);
                if(returnValue == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fc.getSelectedFile();
                    String s = selectedFile.getAbsolutePath();
                    playListNameDump nameDump = new playListNameDump();
                    playListMenu.updateUI();
                    Main.playlist = nameDump.dirToFile(s);
                    nameDump.playlistCompile(Main.playlist);
                    Main.clipArray = new Clip[Main.playlist.length];
                    LoadSound load = new LoadSound();
                    load.compileClip(Main.clipArray,Main.playlist);
                    if(Main.playlist.length>0) {
                        index = 0;
                        playListMenu.setSelectedIndex(index);
                        checkLyricsImageFile();
                        clip = load.setClip(playListMenu, clip);
                        progress.setMaximum(clip.getFrameLength());
                        framePos = 0;
                        clip.setFramePosition(framePos);
                        if (button.getIcon() == pauseIcon) {
                            button.setIcon(playIcon);
                        }
                    }
                    else{
                        cover.setIcon(null);
                        lyricsText.setText("");
                    }
                }
            }

            @Override
            public void menuDeselected(MenuEvent menuEvent) {

            }

            @Override
            public void menuCanceled(MenuEvent menuEvent) {

            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(button.getIcon() == playIcon) {
                    button.setIcon(pauseIcon);
                    clip.setFramePosition(framePos);
                    clip.start();
                }
                else{
                    button.setIcon(playIcon);
                    framePos = clip.getFramePosition();
                    clip.stop();
                }
            }
        });

        /*stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button.setIcon(playIcon);
                clip.loop(0);
                clip.stop();
                clip.setFramePosition(0);
                framePos = 0;
                progress.setValue(0);
            }
        });*/

        progress.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(Math.abs(progress.getValue() - clip.getFramePosition()) >  10000) {
                    clip.setFramePosition(progress.getValue());
                }
            }
        });


        playListMenu.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(playListMenu.getSelectedIndex() != index) { //change track
                    clip.stop();
                    framePos = 0;
                    progress.setValue(0);

                    index = playListMenu.getSelectedIndex();
                    try {
                        checkLyricsImageFile();
                        clip = load.setClip(playListMenu, clip);
                        progress.setMaximum(clip.getFrameLength());

                        if(button.getIcon() == playIcon){
                            button.setIcon(pauseIcon);
                        }

                        clip.setFramePosition(framePos);
                        clip.start();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(shuffleButton.isSelected()) {
                    random = new Random(System.currentTimeMillis());
                    index = random.nextInt(Main.playlist.length);
                }
                else {
                    ++index;
                    if (index >= Main.playlist.length) {
                        index = 0;
                    }
                }
                playListMenu.setSelectedIndex(index);
                checkLyricsImageFile();
                clip = load.setClip(playListMenu,clip);
                progress.setMaximum(clip.getFrameLength());
                framePos = 0;
                clip.setFramePosition(framePos);
                if(button.getIcon() == playIcon){
                    button.setIcon(pauseIcon);
                }
                clip.start();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (clip.getFramePosition() < 300000){
                    --index;
                    if (index < 0) {
                        index = Main.playlist.length - 1;
                    }
                    playListMenu.setSelectedIndex(index);
                    checkLyricsImageFile();
                    clip = load.setClip(playListMenu,clip);
                    progress.setMaximum(clip.getFrameLength());
                    framePos = 0;
                    clip.setFramePosition(framePos);
                    if(button.getIcon() == playIcon){
                        button.setIcon(pauseIcon);
                    }
                    clip.start();
                }
                else{
                    if(button.getIcon() == playIcon){
                        button.setIcon(pauseIcon);
                    }
                    framePos = 0;
                    clip.setFramePosition(framePos);
                    clip.start();
                }
            }
        });

    }

    public static void threadCheckLyricsImageFile(int i){
        String mainDir = System.getProperty("user.home")+"\\Documents";
        String currentName = Main.playlist[i].getName().replace(".wav","");
        File lyrics = new File(mainDir+"\\Lyrics\\" + currentName +".txt");
        File image = new File(mainDir + "\\Album Arts\\" + currentName + ".jpg");
        String lyricsFileContent = "";

        try {
            URL urlDefault = new URL("https://www.terumah.ca/wp-content/uploads/2016/01/playlist-songs-to-listen-to-in-the-dark-headphones.jpg");
            defaultImage = ImageIO.read(urlDefault);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage imgAlbum = defaultImage;

        try {
            if (!lyrics.exists() || !image.exists()) { //either does not exist
                lyricsOnline l = new lyricsOnline();
                l.getURL(currentName); //selenium get url
                URL imageURL = new URL(lyricsDump.albumImageURL); //imageURL

                if(lyrics.createNewFile()){ //can create lyrics aka lyrics does not exist
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(lyrics), "UTF8"));
                    lyricsFileContent = lyricsOnline.parseLyrics(lyricsDump.lyricsHTML0);
                    writer.write(lyricsFileContent);
                    writer.close();
                }
                if(image.createNewFile()){ //image does not exist
                    image.createNewFile();
                    imgAlbum = ImageIO.read(imageURL);
                    ImageIO.write(imgAlbum,"jpg",image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            if(!lyrics.exists()) lyrics.createNewFile();
            if(!image.exists()) {
                image.createNewFile();
                ImageIO.write(imgAlbum,"jpg",image);
            }
        } catch (IOException f){
            f.printStackTrace();
        }
    }

    public static void checkLyricsImageFile() {
        currentName = Main.playlist[index].getName().replace(".wav","");
        lyrics = new File(mainDir+"\\Lyrics\\" + currentName +".txt");
        image = new File(mainDir + "\\Album Arts\\" + currentName + ".jpg");
        imgAlbum = defaultImage;
        lyricsFileContent = "";

        try {
            imgAlbum = ImageIO.read(image);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(lyrics),"UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                lyricsFileContent = lyricsFileContent + line + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        lyricsText.setText(lyricsFileContent);
        lyricsText.setCaretPosition(0);

        Image dimgAlbum = imgAlbum.getScaledInstance(290, 370, Image.SCALE_SMOOTH);
        ImageIcon albumArtIcon = new ImageIcon(dimgAlbum);
        cover.setIcon(albumArtIcon);

        frame.setTitle(currentName);
    }

    public static LineListener generateLineListener(){
        LineListener line = new LineListener() {
            @Override
            public void update(LineEvent lineEvent) {
                if(lineEvent.getType() == LineEvent.Type.START){
                    while (clip.getFramePosition() < progress.getMaximum()){
                        progress.setValue(clip.getFramePosition());
                    }
                }
                if(lineEvent.getType() == LineEvent.Type.STOP){
                    if(!replayButton.isSelected()){
                        if(shuffleButton.isSelected()){
                            random = new Random(System.currentTimeMillis());
                            index = random.nextInt(Main.playlist.length);
                        }
                        else {
                            index = playListMenu.getSelectedIndex() + 1;
                            if (index >= Main.playlist.length) {
                                index = 0;
                            }
                        }
                        playListMenu.setSelectedIndex(index);
                        checkLyricsImageFile();
                        clip = load.setClip(playListMenu,clip);
                        progress.setMaximum(clip.getFrameLength());
                        framePos = 0;
                        clip.setFramePosition(framePos);
                        clip.start();
                    }
                    else{
                        framePos = 0;
                        clip.setFramePosition(framePos);
                        clip.loop(1);
                    }
                }
            }
        };
        return line;
    }
}
