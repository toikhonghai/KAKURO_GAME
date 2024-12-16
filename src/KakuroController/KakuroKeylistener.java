package KakuroController;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KakuroKeylistener extends KeyAdapter {
    private final JTextField cell;
    private final KakuroGame game;
    private final int row;
    private final int col;
    private final KakuroGUI gameFrame;
    private final KakuroMenu menu;
    private MusicPlayer musicPlayer;
    public KakuroKeylistener(JTextField cell, KakuroGame game, int row, int col, KakuroGUI gameFrame, KakuroMenu menu){
        musicPlayer = MusicPlayer.getInstance();
        this.cell = cell;
        this.game = game;
        this.row = row;
        this.col = col;
        this.gameFrame = gameFrame;
        this.menu = menu;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            showQuestionExit();
        }
        if(e.getKeyChar()=='m' || e.getKeyChar()=='M'){
            musicPlayer.toggleMusic("music/chill.wav");
        }
    }
    private void showQuestionExit(){
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
                dialog.dispose();
                gameFrame.dispose();
                menu.setVisible(true);
            }
        });
        buttonPanel.add(yes);
        buttonPanel.add(no);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e){
        char c = e.getKeyChar();
        if(!Character.isDigit(c) || c=='0' || cell.getText().length()>1) e.consume();
    }
    @Override
    public void keyReleased(KeyEvent e){
        String text = cell.getText();
        if(!text.isEmpty() && text.matches("[1-9]")){
            int value = Integer.parseInt(text);
            game.updateBoardValues(row, col, value);
        }
    }
}
