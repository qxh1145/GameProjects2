package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeGame extends JFrame implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private final int DELAY = 140;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int appleX;
    private int appleY;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private int score = 0;

    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(new MyKeyAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        initializeGame();
        setVisible(true);
    }

    private void initializeGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 100 - i * DOT_SIZE;
            y[i] = 100;
        }
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void locateApple() {
        int r = (int) (Math.random() * (WIDTH / DOT_SIZE));
        appleX = r * DOT_SIZE;
        r = (int) (Math.random() * (HEIGHT / DOT_SIZE));
        appleY = r * DOT_SIZE;
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++;
            score += 10;
            locateApple();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (inGame) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
                } else {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
                }
            }
            Toolkit.getDefaultToolkit().sync();
            drawScore(g);
        } else {
            gameOver(g);
        }
    }

    private void drawScore(Graphics g) {
        String scoreString = "Score: " + score;
        g.setColor(Color.WHITE);
        g.drawString(scoreString, 10, 20);
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 36);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
        g.drawString("Your score: " + score, (WIDTH - metrics.stringWidth("Your score: " + score)) / 2, (HEIGHT / 2) + 50);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}


