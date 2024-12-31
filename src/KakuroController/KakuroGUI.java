package KakuroController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class KakuroGUI extends JFrame{
    private static final int CELL_SIZE = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private KakuroGame game;
    private JPanel boardPanel;
    private JTextField[][] cells;
    private final JPanel mainPanel;
    private final KakuroMenu menu;
    private Timer gameTimer;
    private int timeRemaining;
    private JLabel timerLabel;
    private static final int GAME_DURATION = 600;
    private final MessageWindow showQuestion;
    private BufferedImage imageW;
    private BufferedImage imageS;
    private BufferedImage backgroundImage;

    public KakuroGUI(int size, int level, KakuroMenu menu){
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        this.menu = menu;
        game = new KakuroGame(size, level);
        showQuestion = new MessageWindow(game, this, menu);
        cells = new JTextField[size][size];
        // Tao frame
        setTitle("Kakuro Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        // Tao panel chinh
        try{
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("image/board.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
        mainPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(20));

        // Tao panel cho timer
        JLabel timer = createTimeLabel();
        mainPanel.add(timer);
        mainPanel.add(Box.createVerticalStrut(20));

        //Tao panel cho bang game
        createBoardPanel(size);
        mainPanel.add(boardPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        add(mainPanel);

        //Tao panel cho nut dieu khien
        JButton createCheck = createControlButton(size);
        mainPanel.add(createCheck);
        mainPanel.add(Box.createVerticalStrut(20));

        musicPlayer.setCurrentSong("music/chill.wav");
        if(musicPlayer.isPlaying()) musicPlayer.playMusic(musicPlayer.getCurrentSong());
        //Dang ki KeyEventDispatcher de bat phim o muc toan cuc
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if(!menu.isVisible()){
                if(e.getID()==KeyEvent.KEY_PRESSED){
                    if(e.getKeyChar()==KeyEvent.VK_ESCAPE){
                        showQuestion.showQuestionExit();
                        return true;
                    }
                }
            }
            return false;
        });

        setLocationRelativeTo(null); //Dat frame o giua man hinh
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("image/icon.png")));
        setIconImage(icon.getImage());
        startTimer();

        //Dam bao co the nhan su kien phim
        setFocusable(true);
        requestFocusInWindow();
    }
    public JLabel createTimeLabel(){
        timerLabel = new JLabel("Thời Gian: 10:00");
        timerLabel.setFont(new Font("Chalkboard", Font.BOLD, 15));
        timerLabel.setForeground(Color.white);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // căn vị tri theo chieu ngang
        return timerLabel;
    }
    private void startTimer(){
        timeRemaining = GAME_DURATION;
        if(gameTimer!=null){
            gameTimer.stop();
        }
        gameTimer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimerLabel();
            if(timeRemaining<=0){
                gameTimer.stop();
                handleGameOver();
            }
        });
        gameTimer.start();
    }
    private void updateTimerLabel(){
        int minutes = timeRemaining/60;
        int seconds = timeRemaining%60;
        timerLabel.setText(String.format("Thời Gian: %02d:%02d", minutes, seconds));
    }
    private void handleGameOver(){
        gameTimer.stop();
        showQuestion.showQuestionGameOver();
    }
    private void createBoardPanel(int size){
        boardPanel = new JPanel(new GridLayout(size, size, 2, 2));
        boardPanel.setBackground(Color.BLACK);
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(game.getBoard()[i][j]==-1){
                    JPanel wallCell = createWallCell();
                    wallCell.setBackground(Color.BLACK);
//                  wallCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                    boardPanel.add(wallCell);
                }else if(game.getBoard()[i][j]==-2){
                    JPanel sumCell = createSumCell(i, j);
                    boardPanel.add(sumCell);
                }else{
                    cells[i][j] = createInputCell(i, j);
                    boardPanel.add(cells[i][j]);
                }
            }
        }
    }
    private JPanel createWallCell(){
        try{
            imageW = ImageIO.read(Objects.requireNonNull(getClass().getResource("image/wall1.png")));
        }catch (Exception e){
            e.printStackTrace();
        }
        JPanel wallCell = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageW, 0, 0, getWidth(), getHeight(), null);
            }
        };
        return wallCell;
    }
    private JPanel createSumCell(int row, int col){
        try{
            imageS = ImageIO.read(Objects.requireNonNull(getClass().getResource("image/square (1).png")));
        }catch (Exception e){
            e.printStackTrace();
        }
        JPanel sumCell = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageS, 0, 0, getWidth(), getHeight(), null);
                g.setColor(Color.BLACK);
                g.drawLine(0, 0, getWidth(), getHeight());
                int verticalSum = game.getVerticalSums()[row][col];
                int horizontalSum = game.getHorizontalSums()[row][col];
                if(verticalSum>0) g.drawString(String.valueOf(verticalSum), 5, getHeight()-15);
                if(horizontalSum>0) g.drawString(String.valueOf(horizontalSum), getWidth()-20, 15);
            }
        };
        sumCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        return sumCell;
    }
    private JTextField createInputCell(int row, int col){
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cell.setFont(new Font("Arial", Font.BOLD, 20));
        if(game.getBoard()[row][col]>0){
            cell.setText(String.valueOf(game.getBoard()[row][col]));
            cell.setEditable(false);
            cell.setBackground(new Color(255, 218, 185));
        }else{
            cell.setBackground(Color.WHITE);
            //theo doi cac thay doi cua van ban
            cell.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    cell.setBackground(new Color(255, 218, 185));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(cell.getText().isEmpty()){
                        cell.setBackground(Color.WHITE);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(cell.isEditable() && cell.getText().isEmpty()){//doi mau khi re chuot vao
                        cell.setBackground(new Color(255, 216, 185));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if(cell.isEditable() && cell.getText().isEmpty()){
                        cell.setBackground(Color.WHITE);
                    }
                }
            });
        }
        cell.addKeyListener(new KakuroKeylistener(cell, game, row, col));
        return cell;
    }
    private JButton createControlButton(int size){
        JButton checkButton = new DrawButton("image/rectangle.png", "image/rectangle (1).png");
        checkButton.setPreferredSize(new Dimension(150, 40));
        checkButton.setText("Kiểm Tra");
        checkButton.addActionListener(e->checkSolution(size));
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return checkButton;
    }
    private void checkSolution(int size){
        boolean isCorrect = true;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(game.getBoard()[i][j]==-2){
                    if(!game.checkResult(i, j)){
                        isCorrect = false;
                        break;
                    }
                }
            }
        }
        if(isCorrect){
            gameTimer.stop();
            showQuestion.showMessageTrueSolution();
        }else{
            showQuestion.showMessageFalseSolution();
        }
    }

    public void startNewGame(){
        timeRemaining = GAME_DURATION;
        timerLabel.setText("Time Remaining: 10:00");
        int size = game.getSize();
        int level = game.getLevel();
        game = new KakuroGame(size, level);
//        game.generateKakuroRandom();
        updateBoard();
        startTimer();
    }
    private void updateBoard(){
        mainPanel.removeAll();
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(timerLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        int size = game.getSize();
        cells = new JTextField[size][size];
        createBoardPanel(size);
        mainPanel.add(boardPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        JButton checkButton = createControlButton(size);
        mainPanel.add(checkButton);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    public void dispose(){
        super.dispose();
    }
    public int getTimeRemaining(){
        return timeRemaining;
    }
}