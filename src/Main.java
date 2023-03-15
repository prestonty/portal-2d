import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

public class Main {
    JFrame gameWindow;
    GamePanel gamePanel;   
    MyKeyListener keyListener;
    BasicMouseListener mouseListener;

    final int SAMUS_VELOCITY = 10;
    final int SAMUS_MAX_JUMPS = 1;
    boolean right, left, up, down;

    // only 1 of each portal can exist
    final int MAX_PORTALS = 2;
    Portal[] portals = new Portal[MAX_PORTALS];

    // Block collision.
    static Block[][] blocks = new Block[Const.NUMBER_OF_BLOCK_ROWS][Const.NUMBER_OF_BLOCK_COLUMNS];
    static HashMap<Button, Door> doors = new HashMap<Button, Door>();

    // non animated block collisions
    
    // species collision with floor (applies to any enemies, players).I dont think I will include enemies
    // static ArrayList<Species> alive = new ArrayList<Species>();

    //game objects
    Samus samus = new Samus(600,600, "images/playerSprite/larger/samus", SAMUS_MAX_JUMPS, Const.SAMUS_ANIMATION_ARRAY);
    Background background = new Background(0, 0, "images/backgrounds/back", 1,1);
//------------------------------------------------------------------------------
    //instantiate game objects
    Main(){
        gameWindow = new JFrame("Game Window");

        // 1 game window to properly requested size not including window bar's dimensions
        gameWindow.getContentPane().setPreferredSize(new Dimension(Const.WIDTH, Const.HEIGHT));
        gameWindow.pack();

        gameWindow.setResizable(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gamePanel = new GamePanel();
        // gamePanel.setPreferredSize(new Dimension(Const.WIDTH,Const.HEIGHT));
        gameWindow.add(gamePanel);
        
        keyListener = new MyKeyListener();
        gamePanel.addKeyListener(keyListener);

        mouseListener = new BasicMouseListener();    
        gamePanel.addMouseListener(mouseListener);

        gameWindow.setVisible(true);
        
        // Make the mouse cursor disappear
        // BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        // cursorImg, new Point(0, 0), "blank cursor");
        // gameWindow.getContentPane().setCursor(blankCursor);
    }
//------------------------------------------------------------------------------  
    //main game loop
    public void run(){
        while (true) {
            gameWindow.repaint();
            try  {Thread.sleep(Const.FRAME_PERIOD);} catch(Exception e){}

// Movement logic -------------------------------------------------------------------------------

            // Moving horizontally
            if(right) {
                // difference speed in the air and on the ground
                // this is for y velocity, not air resistance
                if(samus.getYVel() == 0) {
                    samus.showRight();
                    samus.setXVel(Const.X_SPEED_GROUND);
                } else {
                    // if not samus is in the air
                    // samus.showUpRight(); // as of now, displaying the up animation completely messes up the hitbox and falls right through
                    samus.setXVel(Const.X_SPEED_AIR);
                }
            } else if(left) {
                // or Const.GRAVITY
                if(samus.getYVel() == 0) {
                    samus.showLeft();
                    samus.setXVel(-Const.X_SPEED_GROUND);
                } else {
                    // samus.showUpLeft();
                    samus.setXVel(-Const.X_SPEED_AIR);
                }
            } else {
                // Slow down samus' speed to a stop, do not cut their movement directly
                if(samus.getXVel() > 0) {
                    samus.showIdleRight();
                    samus.setXVel(samus.getXVel() - Const.AIR_FRICTION);
                } else if(samus.getXVel() < 0) {
                    samus.showIdleLeft();
                    samus.setXVel(samus.getXVel() + Const.AIR_FRICTION);
                }
            }

            // Horizontal Block collision
            samus.checkHorizontalBlocks(blocks);

            // Boundary at the sides of the JFrame
            if(samus.getX() + samus.getWidth() + samus.getXVel() > Const.WIDTH || samus.getX() + samus.getXVel() < 0) {
                samus.setXVel(-samus.getXVel());
            }

            // Move samus horizontally
            samus.setX(samus.getX() + samus.getXVel());
            

            // Vertical Collision with blocks
            if(samus.checkVerticalBlocks(blocks) && up) {
                samus.setYVel(Const.INITIAL_Y_VEL);
            } else if(samus.checkVerticalBlocks(blocks)) {
                samus.setYVel(0);
                // samus.setY(blocks - samus.getHeight()-1);
            } else if(!samus.checkVerticalBlocks(blocks)) {
                samus.accelerate();
                up = false;
            }
            // Move samus based on velocity and acceleration determined by conditions above
            samus.moveY();


            // Horizontal collision with blocks
            samus.getCannon().alignCannon();

            // Bullet movement (method works)
            for(int i = 0; i < portals.length; i++) {
                // bullets
                if(portals[i] != null) {
                    portals[i].setX(portals[i].getX() + portals[i].getXVel());
                    portals[i].setY(portals[i].getY() + portals[i].getYVel());

                    // Check collision with blocks only
                    portals[i].checkVerticalBlocks(blocks);
                    portals[i].checkHorizontalBlocks(blocks);
                    
                    int other = 0;
                    if(i == 0) {
                        other = 1;
                    } else if(i == 1) {
                        other = 0;
                    }
                    // Check collision with player
                    if(samus.checkVerticalCollision(portals[i])) {
                        samus.hitsPortal(portals[i], portals[other]);
                    }
                    if(samus.checkHorizontalCollision(portals[i])) {
                        samus.hitsPortal(portals[i], portals[other]);
                    }

                    // out of bounds
                    // may remove due to walls and collision
                    // This must be last to avoid errors with portals being null
                    if(portals[i].getX() < -50 || portals[i].getX() + portals[i].getWidth() > Const.WIDTH+50
                    || portals[i].getY() < -50 || portals[i].getY() + portals[i].getHeight() > Const.WIDTH+50) {
                        portals[i] = null;
                    }
                }

                // Animation for portals
            }

            // Portal collision with samus
            if(portals[0] != null && portals[1] != null && samus.checkVerticalCollision(portals[0])) {

            } else if(portals[0] != null && portals[1] != null && samus.checkVerticalCollision(portals[1])) {

            } else if(portals[0] != null && portals[1] != null && samus.checkHorizontalCollision(portals[0])) {

            } else if(portals[0] != null && portals[1] != null && samus.checkVerticalCollision(portals[1])) {

            }



            


        // create menu, simple, do level 1;
        // program level 1 for now
        // BLOCKS ----------------------------------------------------------------------------------------------
        }
    }

    public static void readLevel(String levelName) {
        try {
            File levelFile = new File("src/levelLayout/"+levelName+".txt");
            Scanner fileReader = new Scanner(levelFile);

            // keeps track of number of buttons & doors
            int buttonCount = 0;
            int doorCount = 0;

            // Make sure all levels are 32 characters horizontally by 24 characters vertically to avoid IOException
            for(int i = 0; i < Const.NUMBER_OF_BLOCK_ROWS; i++) { // rows 24
                String[] letters = fileReader.nextLine().split("");
                for(int j = 0; j < Const.NUMBER_OF_BLOCK_COLUMNS; j++) { // columns 32
                    if(letters[j].equals(Const.BLOCK)) {
                        blocks[i][j] = new Block(j*Const.BLOCK_LENGTH, i*Const.BLOCK_LENGTH, "images/blocks/tile", 1, 1);
                    } else if(letters[j].equals(Const.P_BLOCK)) {
                        blocks[i][j] = new Pblock(j*Const.BLOCK_LENGTH, i*Const.BLOCK_LENGTH, "images/blocks/p_tile", 1, 1);
                    } else if(letters[j].equals(Const.BUTTON)) {
                        blocks[i][j] = new Button(j*Const.BLOCK_LENGTH, i*Const.BLOCK_LENGTH, "images/blocks/button", 2, 1, buttonCount);
                        buttonCount++;
                    } else if(letters[j].equals(Const.DOOR)) {
                        blocks[i][j] = new Door(j*Const.BLOCK_LENGTH, i*Const.BLOCK_LENGTH, "images/blocks/door", 1, 1, doorCount);
                        doorCount++;
                    } else if(letters[j].equals(Const.ENTRANCE)) {
                        blocks[i][j] = new Entrance(j*Const.BLOCK_LENGTH, i*Const.BLOCK_LENGTH, "images/blocks/entrance", 1, 1);
                    }
                // if SPACE or another symbol, do nothing
                }
            }
            fileReader.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    public static void setPosition(Samus samus) {
        final int EXTRA_SPACE = 5;
        final int DEFAULT_X = 100;
        final int DEFAULT_Y = Const.HEIGHT - 100;
        Entrance entrance = getEntrance(blocks);
        if(entrance != null) {
           samus.setX(entrance.getX());
           samus.setY(entrance.getY() - samus.getHeight() - EXTRA_SPACE);
        } else {
           samus.setX(DEFAULT_X);
           samus.setY(DEFAULT_Y - samus.getHeight() - EXTRA_SPACE);
        }
    }


// LEVELS -------------------------------------------------------------------------------------------------------
    public static void enterLevel(Samus samus, String level, Portal[] portals) {
        // Reset portals for new level and get new blocks for new level
        blocks = new Block[Const.NUMBER_OF_BLOCK_ROWS][Const.NUMBER_OF_BLOCK_COLUMNS];
        portals[0] = null;
        portals[1] = null;
        readLevel(level);
        setPosition(samus);
    }

    public static Entrance getEntrance(Block[][] blocks) {
        for(int i = 0; i < Const.NUMBER_OF_BLOCK_ROWS; i++) {
            for(int j = 0; j < Const.NUMBER_OF_BLOCK_COLUMNS; j++) {
                if(blocks[i][j] instanceof Entrance) {
                    return (Entrance)blocks[i][j];
                }
            }
        }
        return null;
    }
    
//------------------------------------------------------------------------------
public class GamePanel extends JPanel{
    GamePanel(){
        setFocusable(true);
        requestFocusInWindow();
    }
    
    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g); //required

        background.draw(g);
        // temporary aiming
        // g.drawLine((int) samus.getCannon().getPivotX(), (int) (samus.getCannon().getPivotY()),
        //     (int) (samus.getCannon().getPivotX() + 200 * Math.cos(Math.toRadians(samus.getCannon().getAngle()))),
        //     (int) (samus.getCannon().getPivotY() + 200 * Math.sin(Math.toRadians(samus.getCannon().getAngle()))));


        /* Layer order: (from back of the screen to front)
         * background
         * blocks
         * portals
         * player
         */

        // Draw blocks
        if(blocks[0][0] != null) {
            for(int i = 0; i < Const.NUMBER_OF_BLOCK_ROWS; i++) { // 24 times
                for(int j = 0; j < Const.NUMBER_OF_BLOCK_COLUMNS; j++) { // 32 times
                    if(blocks[i][j] != null) {
                        blocks[i][j].draw(g);
                    }
                }
            }
        }

        // draw portals
        for(int i = 0; i < portals.length; i++) {
            if(portals[i] != null) {
                portals[i].draw(g);
            }
        }

        // Draw player last
        samus.draw(g);
        samus.getCannon().draw(g);
    }
}

//------------------------------------------------------------------------------  
    //act upon key events
    public class MyKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            // key cannot contain 2 values at same time, this is why you have different boolean variables
            if(key == KeyEvent.VK_D) {
                right = true;
            }
            if(key == KeyEvent.VK_A) {
                left = true;
            }
            if(key == KeyEvent.VK_SPACE) {
                up = true;
            }
            if(key == KeyEvent.VK_S) {
                down = true;
            }
        }
        @Override
        public void keyReleased(KeyEvent e){ 
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_D){
                right = false;
            } else if (key == KeyEvent.VK_A) {
                left = false;
            } else if (key == KeyEvent.VK_S){
                // not in ball form, go back to normal (basically same as up)
                // samus.moveDown();
            }

            // temporary level keys
            if(key == KeyEvent.VK_1) {
                enterLevel(samus, "level1", portals);
            } else if(key == KeyEvent.VK_2) {
                enterLevel(samus, "level2", portals);
            } else if(key == KeyEvent.VK_3) {
                enterLevel(samus, "level3", portals);
            } else if(key == KeyEvent.VK_4) {
                enterLevel(samus, "level4", portals);
            } else if(key == KeyEvent.VK_5) {
                enterLevel(samus, "level5", portals);
            }  else if(key == KeyEvent.VK_6) {
                enterLevel(samus, "level6", portals);
            }
        }
        @Override
        public void keyTyped(KeyEvent e){
        }
    }
//------------------------------------------------------------------------------

public class BasicMouseListener implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e){
    }

    // Pressed is more responsive than mouseclicked and actually performs more consistently
    @Override
    public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        // shooting portals
        final int bulletWidth = 24, bulletHeight = 24;
        
        // make sure there are only two portals on screen or else it will ruin the gameplay
        if(e.getButton() == MouseEvent.BUTTON1) {
            // left click
            int xVel = (int) Math.round(Const.PORTAL_SPEED*Math.cos(Math.toRadians(samus.getCannon().getAngle())));
            int yVel = (int) Math.round(Const.PORTAL_SPEED*Math.sin(Math.toRadians(samus.getCannon().getAngle())));
            // System.out.println(xVel + "  " + yVel + "  " + samus.getCannon().getAngle());
            portals[0] = new Portal((int) samus.getCannon().getPivotX() - bulletWidth/2, (int) samus.getCannon().getPivotY()-bulletHeight/2, "images/portals/bluePortal", Const.PORTAL_ANIMATION_ARRAY, xVel, yVel, Const.BLUE);
            portals[0].showForward();
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            // right click
            int xVel = (int) Math.round(Const.PORTAL_SPEED*Math.cos(Math.toRadians(samus.getCannon().getAngle())));
            int yVel = (int) Math.round(Const.PORTAL_SPEED*Math.sin(Math.toRadians(samus.getCannon().getAngle())));
            // System.out.println(xVel + "  " + yVel + "  " + samus.getCannon().getAngle());

            portals[1] = new Portal((int) samus.getCannon().getPivotX() - bulletWidth/2, (int) samus.getCannon().getPivotY()-bulletHeight/2, "images/portals/purplePortal", Const.PORTAL_ANIMATION_ARRAY, xVel, yVel, Const.PURPLE);
            portals[1].showForward();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
    }
    @Override
    public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
    }
    @Override
    public void mouseExited(MouseEvent e){    // MUST be implemented even if not used!
    }
    
}  

//------------------------------------------------------------------------------
    public static void main(String[] args){
        Main game = new Main();
        game.run();
    }
}