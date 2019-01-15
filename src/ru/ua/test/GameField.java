package ru.ua.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener
{
    private final int SIZE = 320;
    private final int DOT_SIZE = 16; // размер одной ячейки змейки и яблочка
    private final int ALL_DOTS = 400; // Всего сколько игровых едениц может поместиться на поле
    private Image dot;
    private Image apple;
    private int appleX; //x позиция яблока
    private int appleY; //y позиция яблока
    private int[] x = new int[ALL_DOTS]; // в каких ячейках находится змейка для позиции x
    private int[] y = new int[ALL_DOTS]; // в каких ячейках находится змейка для позиции y
    private int dots; // размер змейки
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    public GameField()
    {
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    private void initGame()
    {
        this.dots = 3;

        for(int i = 0; i < this.dots; i++)
        {
           x[i] = 48 - i * DOT_SIZE;
           y[i] = 48;
        }

        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    private void loadImages()
    {
        ImageIcon apple = new ImageIcon("apple.png");
        this.apple = apple.getImage();
        ImageIcon dot = new ImageIcon("dot.png");
        this.dot = dot.getImage();
    }

    private void createApple()
    {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    private void move()
    {
        for (int i = dots; i > 0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if(left)
        {
            x[0] -= DOT_SIZE;
        }
        if(right)
        {
            x[0] += DOT_SIZE;
        }
        if(up)
        {
            y[0] -= DOT_SIZE;
        }
        if(down)
        {
            y[0] += DOT_SIZE;
        }
    }

    private void checkApple()
    {
        if(x[0] == appleX && y[0] == appleY)
        {
            dots++;
            createApple();
        }
    }

    private void checkCollissions()
    {
        for (int i = dots; i > 0; i--) {
            if(i > 4 && x[0] == x[i] && y[0] == y[i])
            {
                inGame = false;
            }
        }

        if(x[0] > SIZE)
        {
            inGame = false;
        }
        if(x[0] < 0)
        {
            inGame = false;
        }
        if(y[0] > SIZE)
        {
            inGame = false;
        }
        if(y[0] < 0)
        {
            inGame = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(inGame)
        {
            g.drawImage(apple, appleX, appleY, this);

            for (int i = 0; i < dots; i++)
            {
                g.drawImage(dot, x[i], y[i], this);
            }
        }
        else
        {
            String str = "Game over";
            Font f = new Font("Arial", Font.BOLD, 14);
            g.setColor(Color.white);
            g.setFont(f);
            g.drawString(str, 125, SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(inGame)
        {
            checkApple();
            checkCollissions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_LEFT && !right)
            {
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left)
            {
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && !down)
            {
                up = true;
                right = false;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && !up)
            {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
