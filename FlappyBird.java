package BTTH2;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    int birdX = 360 / 8;
    int birdY = 640 / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    // Quan ly trang thai
    Bird bird;
    ArrayList<Pipe> pipes;
    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;
    int velocityY = 0;
    int gravity = 1;

    boolean canRestart = false;
    boolean cooldownStarted = false;

    // 1. Tao cua so Flappy Bird va thay hinh nen
    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // 1. Them file anh flappybirdbg.png
        backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
        // 2. Them giao dien su dung hinh anh flappybird.png
        birdImg = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
        // 3. Them hinh anh toppipe.png va bottompipe.png
        topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

        bird = new Bird(birdImg, birdX, birdY, birdWidth, birdHeight);
        pipes = new ArrayList<Pipe>();

        // 3. Cot bat dau xuat hien
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        // 3. Them chu trinh game loop
        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    // 3. Thiet lap do dai ngan khac nhau cua cac cot
    public void placePipes() {
        int rdPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg, pipeX, rdPipeY, pipeWidth, pipeHeight);
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg, pipeX, topPipe.y + pipeHeight + openingSpace, pipeWidth, pipeHeight);
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, boardWidth, boardHeight);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = g.getFontMetrics();

            String msg1 = "GAME OVER";
            String msg2 = "Score: " + (int) score;

            int x1 = (boardWidth - metrics.stringWidth(msg1)) / 2;
            int x2 = (boardWidth - metrics.stringWidth(msg2)) / 2;

            g.drawString(msg1, x1, boardHeight / 2 - 20);
            g.drawString(msg2, x2, boardHeight / 2 + 30);

            if (canRestart) {
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                String msg3 = "Press Space to Restart";
                int x3 = (boardWidth - g.getFontMetrics().stringWidth(msg3)) / 2;
                g.drawString(msg3, x3, boardHeight / 2 + 80);
            }
        } else {
            // Render backgroud
            g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

            // Render bird
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

            // Render pipes
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }

            // Render score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    // 2. Thiet lap hieu ung len xuong khi an space hoac enter
    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);

            // 3. Thiet lap hieu ung di chuyen cua doi tuong Pipe
            pipe.x -= 4;

            // 4. Cap nhap co che tinh diem
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5;
            }

            // 4. Ham tinh va cham
            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (pipes.size() > 0 && pipes.get(0).x + pipeWidth < 0) {
            pipes.remove(0);
        }

        // 4. Cap nhat co che game over
        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        } else {
            placePipesTimer.stop();
            gameLoop.stop();
            repaint();

            if (!cooldownStarted) {
                cooldownStarted = true;
                Timer cooldownTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        canRestart = true;
                        repaint();
                    }
                });
                cooldownTimer.setRepeats(false);
                cooldownTimer.start();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!gameOver) {
                // 2. Di chuyen len cao khi an phim
                velocityY = -10;
            } else if (canRestart) {
                // 4. Thiet lap tinh nang restart
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                canRestart = false;
                cooldownStarted = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}