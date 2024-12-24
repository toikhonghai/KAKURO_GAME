package KakuroController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        musicPlayer.setCurrentSong("music/mainMenu.wav");
        if(musicPlayer.isPlaying()) musicPlayer.playMusic(musicPlayer.getCurrentSong());
        //Them KeyListener de bat su kien phim 'M'
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.getID()==KeyEvent.KEY_PRESSED){
                    if(e.getKeyChar()=='m' || e.getKeyChar()=='M'){
                        musicPlayer.toggleMusic();
                        return true;
                    }
                }
                return false;
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
        titleLabel.setFont(new Font("Chalkboard", Font.BOLD, 30));
        titleLabel.setForeground(Color.GREEN); //mau xam khoi
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] difficulties = {"Easy", "Medium", "Hard", "Expert", "Master", "Extreme"};
        JComboBox<String> difficultyCB = new JComboBox<>(difficulties);
        difficultyCB.setMaximumSize(new Dimension(200, 30));
        difficultyCB.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyCB.setCursor(new Cursor(Cursor.HAND_CURSOR));//khi di chuot vao chuyen thanh hinh ban tay

        JButton startButton = new DrawButton("image/rectangle.png", "image/rectangle (1).png");
        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.setText("Bắt Đầu");
        startButton.setFont(new Font("Chalkboard", Font.BOLD, 12));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
//            musicPlayer.stopMusic();
            gamePlay = new KakuroGUI(size, level, this);
            gamePlay.setVisible(true);
            setVisible(false);
        });

        JLabel rulesLabel = new JLabel("Hướng dẫn");
        rulesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rulesLabel.setForeground(new Color(0, 255, 255));
        rulesLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rulesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rulesLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showRulesDialog();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                rulesLabel.setForeground(new Color(0, 0, 150));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rulesLabel.setForeground(new Color(0, 255, 255));
            }
        });

        mainPanel.add(Box.createVerticalStrut(240)); //tao khoang trong o phia trem cua mainpanel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); //khoang trong giua tieu de va start
        mainPanel.add(startButton);
        mainPanel.add(Box.createVerticalStrut(20)); //khoang trong giua start va difficultyCB
        mainPanel.add(difficultyCB);
        mainPanel.add(Box.createVerticalStrut(200));
        mainPanel.add(rulesLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        add(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(gamePlay!=null){
//                    musicPlayer.stopMusic();
                    gamePlay.dispose();
                }
//                musicPlayer.stopMusic();
                dispose();
            }
        });
//        musicPlayer.playMusic("music/mainMenu.wav");
    }
    private void showRulesDialog(){
        JDialog rulesDialog = new JDialog(this, "Luật chơi Kakuro", true);
        rulesDialog.setSize(500, 400);
        rulesDialog.setLocationRelativeTo(this);

        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Hướng dẫn chơi Kakuro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea rulesText = new JTextArea(
                "1. Mục tiêu:\n" +
                        "   - Điền các số từ 1 đến 9 vào các ô trống(ô màu trắng)\n\n" +
                        "2. Các số trong ô màu xám là tổng của dãy số cần điền:\n" +
                        "   - Số phía trên: Tổng của các số trong cột bên dưới\n" +
                        "   - Số bên trái: Tổng của các số trong hàng bên phải\n\n" +
                        "3. Quy tắc quan trọng:\n" +
                        "   - Không được lặp lại số trong cùng một dãy số\n" +
                        "   - Mỗi tổng phải sử dụng các chữ số khác nhau\n" +
                        "   - Chỉ được sử dụng các số từ 1 đến 9\n\n" +
                        "4. Mẹo chơi:\n" +
                        "   - Bắt đầu với các ô có tổng nhỏ hoặc ít khả năng\n" +
                        "   - Tìm các tổng bắt buộc phải sử dụng một số cụ thể\n" +
                        "   - Sử dụng phương pháp loại trừ để tìm số phù hợp"
        );
        rulesText.setEditable(false); //Khong the chinh sua
        rulesText.setWrapStyleWord(true); //Xuong dong van giua nguyen tu
        rulesText.setLineWrap(true); //Tu dong xuong dong
        rulesText.setOpaque(false); //Lam cho nen tro nen trong suot
        rulesText.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(rulesText);
        scrollPane.setBorder(null);

        JButton closeButton = new JButton("Đóng");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> rulesDialog.dispose());

        rulesPanel.add(titleLabel);
        rulesPanel.add(Box.createVerticalStrut(20));
        rulesPanel.add(scrollPane);
        rulesPanel.add(Box.createVerticalStrut(20));
        rulesPanel.add(closeButton);

        rulesDialog.add(rulesPanel);
        rulesDialog.setVisible(true);
    }
}