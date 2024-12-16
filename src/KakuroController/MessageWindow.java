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
    public MessageWindow(KakuroGame game, KakuroGUI gameFrame, KakuroMenu menu){
        musicPlayer = MusicPlayer.getInstance();
        this.game = game;
        this.gameFrame = gameFrame;
        this.menu = menu;
    }
    public void showQuestionGameOver() {
        JDialog dialog = new JDialog(gameFrame, "Game Over", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel("You lost!", JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton buttonExit = new JButton("Main Menu");
        buttonExit.setFocusPainted(false);
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.dispose();
                menu.setVisible(true);
            }
        });
        JButton new_game = new JButton("New Game");
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
        JDialog dialog = new JDialog(gameFrame, "Correct Solution", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(gameFrame);
        dialog.setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel("Congratulations! Solution is correct!", JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton main_menu = new JButton("Main Menu");
        main_menu.setFocusPainted(false);
        main_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.dispose();
                menu.setVisible(true);
            }
        });
        JButton replay = new JButton("Replay");
        replay.setFocusPainted(false);
        replay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                gameFrame.startNewGame();
            }
        });
        JButton nextLevel = new JButton("Next Level");
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
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    public void showMessageFalseSolution(){
        JDialog dialog = new JDialog(gameFrame, "Incorrect Solution", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(gameFrame);
        dialog.setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel("Your solution is incorrect. Try again!", JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton button = new JButton("OK");
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(button);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    public void showQuestionExit(){
        JDialog dialog = new JDialog(gameFrame, "Thoát trò chơi?", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(gameFrame);
        dialog.setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel("Bạn muốn tiếp tục không?", JLabel.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton yes = new JButton("Yes");
        yes.setFocusPainted(false);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JButton no = new JButton("No");
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
        dialog.add(buttonPanel, BorderLayout.SOUTH);
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
