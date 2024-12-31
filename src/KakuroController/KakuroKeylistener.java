package KakuroController;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KakuroKeylistener extends KeyAdapter {
    private final JTextField cell;
    private final KakuroGame game;
    private final int row;
    private final int col;
    private final MusicPlayer musicPlayer;
    public KakuroKeylistener(JTextField cell, KakuroGame game, int row, int col){
        musicPlayer = MusicPlayer.getInstance();
        this.cell = cell;
        this.game = game;
        this.row = row;
        this.col = col;
    }

    @Override
    public void keyTyped(KeyEvent e){
        char c = e.getKeyChar();
        if(!Character.isDigit(c) || c=='0' || cell.getText().length()>=1) e.consume();
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
