package com.brickoutball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameAlgo extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int bricks = 21;
    private Timer time;
    private int delay = 4;
    private int player = 310;

    private int ball_posX = 120;
    private int ball_posY = 350;
    private int ball_dirX = -1;
    private int ball_dirY = -2;

    private MapGen map;

    public GameAlgo() {
        map = new MapGen(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay,this);
        time.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.darkGray);
        g.fillRect(1,1,692,592);

        // drawing map
        map.draw((Graphics2D)g);

        // borders
        g.setColor(Color.blue);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(692,0,3,592);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // paddle
        g.setColor(Color.green);
        g.fillRect(player,550,100,8);

        // ball
        g.setColor(Color.orange);
        g.fillOval(ball_posX,ball_posY,20,20);

        if (bricks <= 0) {
            play = false;
            ball_dirY = 0;
            ball_dirX = 0;
            g.setColor(new Color(990000));
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Play Again", 230,350);
        }

        if (ball_posY > 570) {
            play = false;
            ball_dirY = 0;
            ball_dirX = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if (play) {
            if (new Rectangle(ball_posX, ball_posY,20,20).intersects(new Rectangle(player,550,100,8))) {
                ball_dirY = -ball_dirY;
            }

            outer: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rec = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRec = new Rectangle(ball_posX, ball_posY,20,20);

                        if (ballRec.intersects(rec)) {
                            map.setBrickValue(0, i, j);
                            bricks--;
                            score += 5;

                            if (ball_posX + 19 <= rec.x || ball_posX + 1 >= rec.x + rec.width) {
                                ball_dirX = -ball_dirX;
                            }
                            else {
                                ball_dirY = -ball_dirY;
                            }
                            break outer;
                        }
                    }
                }
            }

            ball_posX += ball_dirX;
            ball_posY += ball_dirY;

            if (ball_posX < 0) {
                ball_dirX = -ball_dirX;
            }
            if (ball_posY < 0) {
                ball_dirY = -ball_dirY;
            }
            if (ball_posX > 670) {
                ball_dirX = -ball_dirX;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (player >= 600) {
                player = 600;
            }
            else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (player < 10) {
                player = 10;
            }
            else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ball_posX = 120;
                ball_posY = 350;
                ball_dirX = -1;
                ball_dirY = -2;
                player = 310;
                score = 0;
                bricks = 21;
                map = new MapGen(3,7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        player += 20;
    }

    public void moveLeft() {
        play = true;
        player -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
