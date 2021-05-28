import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameSourceCode extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Bricks bricks;

    private Timer timer;
    private final int delay = 10;

    private int playerPostionX = 310;

    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    public GameSourceCode(){
        bricks = new Bricks(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics graphics){
        //background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(1,1,692,592);

        // drawing bricks
        bricks.draw((Graphics2D) graphics);

        // scores
        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif",Font.BOLD, 20));
        graphics.drawString("SCORE :"+score, 570,30);
        
        //borders
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(0,0,2,592);
        graphics.fillRect(0,0,692,2);
        graphics.fillRect(684,0,3,592);

        //Bar
        graphics.setColor(Color.GREEN);
        graphics.fillRect(playerPostionX,550,100,8);

        //Ball
        graphics.setColor(Color.red);
        graphics.fillOval(ballPositionX,ballPositionY,20,20);

        if(totalBricks <= 0)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD, 30));
            graphics.drawString("You Won", 260,300);

            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD, 20));
            graphics.drawString("Press (Enter) to Restart", 230,350);
        }

        // when you lose the game
        if(ballPositionY > 570)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD, 30));
            graphics.drawString("Game Over, Scores: "+score, 190,300);

            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD, 20));
            graphics.drawString("Press (Enter) to Restart", 230,350);
        }
        graphics.dispose();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if(new Rectangle(ballPositionX,ballPositionY,20,20).intersects(
                    new Rectangle(playerPostionX,550,100,8))){
                ballYdir = -ballYdir;
            }
            else if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(
                    new Rectangle(playerPostionX + 70, 550, 30, 8)))
            {
                ballYdir = -ballYdir;
                ballXdir = ballXdir + 1;
            }
            else if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(
                    new Rectangle(ballPositionX + 30, 550, 40, 8)))
            {
                ballYdir = -ballYdir;
            }

            // check map collision with the ball
            A: for(int i = 0; i<bricks.map.length; i++)
            {
                for(int j =0; j<bricks.map[0].length; j++)
                {
                    if(bricks.map[i][j] > 0)
                    {
                        //scores++;
                        int brickX = j * bricks.brickWidth + 80;
                        int brickY = i * bricks.brickHeight + 50;
                        int brickWidth = bricks.brickWidth;
                        int brickHeight = bricks.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect))
                        {
                            bricks.setBrickValue(0, i, j);
                            score+=5;
                            totalBricks--;

                            // when ball hit right or left of brick
                            if(ballPositionX + 19 <= brickRect.x ||
                                    ballPositionX + 1 >= brickRect.x + brickRect.width)
                            {
                                ballXdir = -ballXdir;
                            }
                            // when ball hits top or bottom of brick
                            else
                            {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXdir;
            ballPositionY += ballYdir;

            if(ballPositionX < 0)
                ballXdir = - ballXdir;
            if(ballPositionX > 670)
                ballXdir = -ballXdir;
            if(ballPositionY < 0)
                ballYdir = -ballYdir;
        }
        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerPostionX < 10)
                playerPostionX = 10;
            else
                moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerPostionX >= 595)
                playerPostionX = 595;
            else
                moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerPostionX = 310;
                score = 0;
                totalBricks = 21;
                bricks = new Bricks(3, 7);

                repaint();

            }
        }
    }

    public void moveLeft(){
        play = true;
        playerPostionX += 20;
    }

    public void moveRight(){
        play = true;
        playerPostionX -= 20;
    }



    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }
}
