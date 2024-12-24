package KakuroController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageWindow {
    private final KakuroGame game;
    private final KakuroGUI gameFrame;
    private final KakuroMenu menu;
    private MusicPlayer musicPlayer;
    private int MAX_SCORE = 1000;
    private int GAME_DURATION = 600;
    public MessageWindow(KakuroGame game, KakuroGUI gameFrame, KakuroMenu menu){
        musicPlayer = MusicPlayer.getInstance();
        this.game = game;
        this.gameFrame = gameFrame;
        this.menu = menu;
    }
    private int calculateScore(int timeRemaining){
        return (int)Math.max(0, MAX_SCORE*((double)timeRemaining/GAME_DURATION));
    }
    public void showQuestionGameOver() {
        JDialog dialog = new JDialog(gameFrame, "Kết Thúc Trò Chơi", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel("Bạn Đã Thua!", JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton buttonExit = new JButton("Menu Chính");
        buttonExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonExit.setFocusPainted(false);
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.dispose();
                menu.setVisible(true);
            }
        });
        JButton new_game = new JButton("Trò Chơi Mới");
        new_game.setCursor(new Cursor(Cursor.HAND_CURSOR));
        new_game.setFocusPainted(false);
        new_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.startNewGame();
            }
        });
        buttonPanel.add(buttonExit);
        buttonPanel.add(new_game);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    public void showMessageTrueSolution(){
        JDialog dialog = new JDialog(gameFrame, "Giải Pháp Đúng", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(gameFrame);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(Box.createVerticalStrut(20));

        JLabel label = new JLabel("Chúc Mừng! Giải Pháp Chính Xác!");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);// can giua theo chieu ngang
        dialog.add(label);
        dialog.add(Box.createVerticalStrut(20));

        int score = calculateScore(gameFrame.getTimeRemaining());
        JLabel scoreLabel = new JLabel("Điểm: " + score);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(scoreLabel);
        dialog.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton main_menu = new JButton("Menu Chính");
        main_menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        main_menu.setFocusPainted(false);
        main_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.dispose();
                menu.setVisible(true);
            }
        });
        JButton replay = new JButton("Chơi Lại");
        replay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        replay.setFocusPainted(false);
        replay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.startNewGame();
            }
        });
        JButton nextLevel = new JButton("Cấp Tiếp");
        nextLevel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextLevel.setFocusPainted(false);
        nextLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();;
                startNextLevel();
            }
        });
        buttonPanel.add(main_menu);
        buttonPanel.add(replay);
        if(!isMaxLevel()) buttonPanel.add(nextLevel);
        dialog.add(buttonPanel);
        dialog.add(Box.createVerticalStrut(10));
        dialog.setVisible(true);
    }
    public void showMessageFalseSolution(){
        JDialog dialog = new JDialog(gameFrame, "Giải Pháp Sai", true);
        dialog.setSize(320, 160);
        dialog.setLocationRelativeTo(gameFrame);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(Box.createVerticalStrut(20));

        JLabel label = new JLabel("Giải Pháp Của Bạn Sai. Thử Lại!", JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(label);
        dialog.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton button = new JButton("OK");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(button);
        dialog.add(buttonPanel);
        dialog.add(Box.createVerticalStrut(30));
        dialog.setVisible(true);
    }
    public void showQuestionExit(){
        JDialog dialog = new JDialog(gameFrame, "Thoát trò chơi?", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(gameFrame);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(Box.createVerticalStrut(20));

        JLabel label = new JLabel("Bạn muốn tiếp tục không?", JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(label);
        dialog.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton yes = new JButton("Yes");
        yes.setAlignmentX(Component.CENTER_ALIGNMENT);
        yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yes.setFocusPainted(false);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JButton no = new JButton("No");
        no.setAlignmentX(Component.CENTER_ALIGNMENT);
        no.setCursor(new Cursor(Cursor.HAND_CURSOR));
        no.setFocusPainted(false);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.stopMusic();
                dialog.dispose();
                gameFrame.dispose();
                menu.setVisible(true);
                musicPlayer.playMusic("music/mainMenu.wav");
                musicPlayer.setVolume(0.7f);
            }
        });
        buttonPanel.add(yes);
        buttonPanel.add(no);
        dialog.add(buttonPanel);
        dialog.add(Box.createVerticalStrut(20));
        dialog.setVisible(true);
    }
    private boolean isMaxLevel(){
        int currentSize = game.getSize();
        int currentLevel = game.getLevel();
        return (currentSize==10 && currentLevel==6);
    }
    private void startNextLevel(){
        int nextSize = game.getSize();
        int nextLevel = game.getLevel();
        if(nextSize==5 && nextLevel==1){
            nextSize = 6;
            nextLevel = 2;
        }else if(nextSize==6 && nextLevel==2){
            nextSize = 7;
            nextLevel = 3;
        }else if(nextSize==7 && nextLevel==3){
            nextSize = 8;
            nextLevel = 4;
        }
        else if(nextSize==8 && nextLevel==4){
            nextSize = 9;
            nextLevel = 5;
        }
        else if(nextSize==9 && nextLevel==5){
            nextSize = 10;
            nextLevel = 6;
        }
        if(gameFrame!=null) gameFrame.dispose();
        KakuroGUI new_game = new KakuroGUI(nextSize, nextLevel, menu);
        new_game.setVisible(true);
    }
}
