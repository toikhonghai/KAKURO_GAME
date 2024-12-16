package KakuroController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class DrawButton extends JButton implements MouseListener {
    private BufferedImage normalImage;
    private BufferedImage pressedImage;
    private boolean isCheckPressed;
    public DrawButton(String normalPath, String pressedPath){
        try{
            normalImage = ImageIO.read(getClass().getResource(normalPath));
            pressedImage = ImageIO.read(getClass().getResource(pressedPath));
        }catch (Exception e){
            e.printStackTrace();
        }
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage image = isCheckPressed ? pressedImage : normalImage;
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isCheckPressed = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isCheckPressed = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
