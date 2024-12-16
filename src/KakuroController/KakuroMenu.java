package KakuroController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class KakuroMenu extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private KakuroGUI gamePlay;
    private BufferedImage backgroundImage;
    private MusicPlayer musicPlayer;
    public KakuroMenu(){
        musicPlayer = MusicPlayer.getInstance();
        setTitle("Kakuro Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon(getClass().getResource("image/icon.png"));
        setIconImage(icon.getImage());

        //Them KeyListener de bat su kien phim 'M'
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()=='m' || e.getKeyChar()=='M'){
                    musicPlayer.toggleMusic("music/mainMenu.wav");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        //Dam bao co the nhan su kien phim
        setFocusable(true);
        requestFocusInWindow();

        try{
            backgroundImage = ImageIO.read(getClass().getResource("image/board1.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        JPanel mainPanel  = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("KAKURO GAME");
        titleLabel.setFont(new Font("Chalkboard", Font.BOLD, 24));
        titleLabel.setForeground(Color.GREEN); //mau xam khoi
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] difficulties = {"Easy", "Medium", "Hard", "Expert", "Master", "Extreme"};
        JComboBox<String> difficultyCB = new JComboBox<>(difficulties);
        difficultyCB.setMaximumSize(new Dimension(200, 50));
        difficultyCB.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new DrawButton("image/rectangle.png", "image/rectangle (1).png");
        startButton.setPreferredSize(new Dimension(200, 40));
        startButton.setText("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e->{
            int size;
            int level;
            switch (difficultyCB.getSelectedIndex()){
                case 0:
                    size = 5;
                    level = 1;
                    break;
                case 1:
                    size = 6;
                    level = 2;
                    break;
                case 2:
                    size = 7;
                    level = 3;
                    break;
                case 3:
                    size = 8;
                    level = 4;
                    break;
                case 4:
                    size = 9;
                    level = 5;
                    break;
                case 5:
                    size = 10;
                    level = 6;
                    break;
                default:
                    size = 5;
                    level = 1;
            }

            if(gamePlay != null){
                gamePlay.dispose();
            }
            musicPlayer.stopMusic();
            gamePlay = new KakuroGUI(size, level, this);
            gamePlay.setVisible(true);
            setVisible(false);
        });

        mainPanel.add(Box.createVerticalGlue()); //tao khoang trong o phia trem cua mainpanel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); //khoang trong giua tieu de va start
        mainPanel.add(startButton);
        mainPanel.add(Box.createVerticalStrut(20)); //khoang trong giua start va difficultyCB
        mainPanel.add(difficultyCB);
        mainPanel.add(Box.createVerticalGlue());
        add(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(gamePlay!=null){
                    musicPlayer.stopMusic();
                    gamePlay.dispose();
                }
                musicPlayer.stopMusic();
                dispose();
            }
        });
        musicPlayer.playMusic("music/mainMenu.wav");
    }
}
