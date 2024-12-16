package KakuroController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class KakuroGUI extends JFrame{
    private static final int CELL_SIZE = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private KakuroGame game;
    private JPanel boardPanel;
    private JTextField[][] cells;
    private JPanel mainPanel;
    private KakuroMenu menu;
    private Timer gameTimer;
    private int timeRemaining;
    private JLabel timerLabel;
    private static int GAME_DURATION = 600;
    private MessageWindow showQuestion;
    private BufferedImage imageW;
    private BufferedImage imageS;
    private BufferedImage backgroundImage;
    private MusicPlayer musicPlayer;
    public KakuroGUI(int size, int level, KakuroMenu menu){
        musicPlayer = MusicPlayer.getInstance();
        this.menu = menu;
        game = new KakuroGame(size, level);
        showQuestion = new MessageWindow(game, this, menu);
        cells = new JTextField[size][size];

        // Tao frame
        setTitle("Kakuro Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        //Them KeyListener de bat su kien phim 'M'
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()=='m' || e.getKeyChar()=='M'){
                    musicPlayer.toggleMusic("music/chill.wav");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Tao panel chinh
        try{
            backgroundImage = ImageIO.read(getClass().getResource("image/board.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        mainPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
//                Graphics2D g2d = (Graphics2D) g;
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(20));

        // Tao panel cho timer
        JLabel timer = createTimeLabel();
        mainPanel.add(timer);
        mainPanel.add(Box.createVerticalStrut(20));

        // Tao panel cho bang game
        createBoardPanel(size);
        mainPanel.add(boardPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        add(mainPanel);

        // Tao panel cho nut dieu khien
        JButton createCheck = createControlButton(size);
        mainPanel.add(createCheck);
        mainPanel.add(Box.createVerticalStrut(20));

        //pack(); // Can chinh kich thuoc frame cho phu hop
        setLocationRelativeTo(null); // Dat frame o giua man hinh
        ImageIcon icon = new ImageIcon(getClass().getResource("image/icon.png"));
        setIconImage(icon.getImage());
        startTimer();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    showQuestion.showQuestionExit();
                }
            }
        });
        //Dam bao co the nhan su kien phim
        setFocusable(true);
        requestFocusInWindow();
        //set music
        musicPlayer.playMusic("music/chill.wav");

    }
    public JLabel createTimeLabel(){
        timerLabel = new JLabel("Time Remaining: 10:00");
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
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                updateTimerLabel();
                if(timeRemaining<=0){
                    gameTimer.stop();
                    handleGameOver();
                }
            }
        });
        gameTimer.start();
    }
    private void updateTimerLabel(){
        int minutes = timeRemaining/60;
        int seconds = timeRemaining%60;
        timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
    }
    private void handleGameOver(){
        gameTimer.stop();
//        showQuestionGameOver();
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
            imageW = ImageIO.read(getClass().getResource("image/wall1.png"));
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
            imageS = ImageIO.read(getClass().getResource("image/square (1).png"));
        }catch (Exception e){
            e.printStackTrace();
        }
        JPanel sumCell = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
//                g.setColor(Color.LIGHT_GRAY);
                g.drawImage(imageS, 0, 0, getWidth(), getHeight(), null);
//                g.fillRect(0, 0, getWidth(), getHeight());
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
        cell.setFont(new Font("Arial", Font.BOLD, 20));
        if(game.getBoard()[row][col]>0){
            cell.setText(String.valueOf(game.getBoard()[row][col]));
            cell.setEditable(false);
            cell.setBackground(new Color(230, 230, 230));
        }else{
            cell.setBackground(Color.WHITE);
        }
        cell.addKeyListener(new KakuroKeylistener(cell, game, row, col, this, menu));
        return cell;
    }
    private JButton createControlButton(int size){
        JButton checkButton = new DrawButton("image/rectangle.png", "image/rectangle (1).png");
        checkButton.setPreferredSize(new Dimension(150, 40));
        checkButton.setText("Check Solution");
        checkButton.addActionListener(e->checkSolution(size));
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
        game.generateKakuroRandom();
        updateBoard();
        startTimer();
    }
    private void updateBoard(){
        mainPanel.remove(boardPanel);
        boardPanel.removeAll();
        int size = game.getSize();
        cells = new JTextField[size][size];
        createBoardPanel(size);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    public void dispose(){
            musicPlayer.stopMusic();
            super.dispose();
    }
}