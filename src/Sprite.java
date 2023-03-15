import java.awt.Graphics;
import java.io.File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * Demonstrates how to load from file and draw animated picture
 * Author: Preston Tom-Ying
 * January 21, 2023
 */
public class Sprite {
    // YOU CANNOT CHANGE THE ORDER OF THESE CONSTANTS, WILL RUIN MY SAMUS ANIMATION
    protected final int RIGHT = 0;
    protected final int LEFT = 1;
    protected final int UP_R = 2;
    protected final int DOWN_R = 3;

    protected String picName;
    
    private int x;
    private int y;
    private int xVel;
    private int yVel;

    private Rectangle[] hitboxes;

    // make a jagged array
    protected BufferedImage[][] frames;
    protected int row;
    protected int col;

    protected int height[][];
    protected int width[][];
//------------------------------------------------------------------------------    

    // use for jagged arrays and velocity
    Sprite(int x, int y, String picName, int rows, int columns) {
        this.x = x;
        this.y = y;
        frames = new BufferedImage[rows][columns];
        this.height = new int[rows][columns];
        this.width = new int[rows][columns];
        this.hitboxes = new Rectangle[rows];
        try {
            for (int row=0; row<rows; row++) {
                String yValue = ""+row;
                for (int col=0; col<columns; col++){
                    String xValue = ""+col;
                    if(col >= 0 && col <= 9)
                        xValue = "0" + col;
                    if(row >= 0 && row <= 9)
                        yValue = "0" + row;

                    frames[row][col] = ImageIO.read(new File(picName+yValue+xValue+".png"));
                    height[row][col] = frames[row][col].getHeight();
                    width[row][col] = frames[row][col].getWidth();    
                }
                hitboxes[row] = new Rectangle(x, y, width[row][col], height[row][col]);
            }
        } catch (IOException ex){
            // The cannon will have an empty index so the rotated image can be stored there
            System.out.println(this.getClass().getSimpleName() + " has an empty index");
        }
        row = 0;
        col = 0;
    }

    // Use for fixed lengths with velocity
    Sprite(int x, int y, String picName, int rows, int columns, int xVel, int yVel){
        this(x, y, picName, rows, columns);
        this.xVel = xVel;
        this.yVel = yVel;
    }

    Sprite(int x, int y, String picName, int[] ANIMATION_ARRAY) {
        int rows = ANIMATION_ARRAY.length;
        this.x = x;
        this.y = y;
        this.xVel = 0;
        this.yVel = 0;
        frames = new BufferedImage[rows][];
        this.hitboxes = new Rectangle[rows];
        this.height = new int[rows][];
        this.width = new int[rows][];

        // Initialise the number of rows and cols of jagged array
        for(int row = 0; row < rows; row++) {
            boolean validImage = true;
            int col = 0;
            frames[row] = new BufferedImage[ANIMATION_ARRAY[row]];
            height[row] = new int[ANIMATION_ARRAY[row]];
            width[row] = new int[ANIMATION_ARRAY[row]];
            do {
                String xValue = ""+col;
                String yValue = ""+row;
                if(col >= 0 && col <= 9)
                    xValue = "0" + xValue;
                if(row >= 0 && row <= 9)
                    yValue = "0" + yValue;
                try {
                    frames[row][col] = ImageIO.read(new File(picName+yValue+xValue+".png"));
                    height[row][col] = frames[row][col].getHeight(); // built in java method
                    width[row][col] = frames[row][col].getWidth(); // built in java method
                    hitboxes[row] = new Rectangle(x, y, width[row][col], height[row][col]); // set hitbox
                } catch(IOException e) {
                    validImage = false;
                }
                col++;
            } while(validImage);
        }
    }

    // use for jagged arrays and velocity
    Sprite(int x, int y, String picName, int xVel, int yVel, int[] ANIMATION_ARRAY) {
        this(x, y, picName, ANIMATION_ARRAY);
        this.xVel = xVel;
        this.yVel = yVel;
    }

// GETTERS AND SETTERS -----------------------------------------------------------

    public int getX() {
        return this.x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }

//------------------------------------------------------------------------------    
    public int getXVel() {
        return this.xVel;
    }

    public int getYVel() {
        return this.yVel;
    }
    public void setXVel(int xVel) {
        this.xVel = xVel;
    }

    public void setYVel(int yVel) {
        this.yVel = yVel;
    }
//------------------------------------------------------------------------------    
    public Rectangle getHitbox() {
        // return current hitbox
        return this.hitboxes[row];
    }

//------------------------------------------------------------------------------ 
    public void draw(Graphics g){
        g.drawImage(this.frames[this.row][this.col], this.x, this.y, null);
    }

    public void showRight(){
        this.row = RIGHT;
        // iterates from first to last of animation row
        this.col = (this.col + 1)%frames[row].length;
    }

    public void showLeft(){
        this.row = LEFT;
        this.col = (this.col + 1)%frames[row].length;
    }

    // Did not use (Turns samus into a ball when holding down 'D' o keypad)
    public void showDownRight() {
        this.row = DOWN_R;
        this.col = (this.col + 1)%frames[row].length;
    }

    public void showUpRight() {
        this.row = UP_R;
        this.col = (this.col+1)%frames[row].length;
    }

    // Get the width of the current animation image
    public int getWidth() {
        return width[row][col];
    }
    public void setWidth(int newWidth) {
        width[row][col] = newWidth;
    }
    // Get the height of the current animation image
    public int getHeight() {
        return height[row][col];
    }
    // Get the width of the current animation image
    public void setHeight(int newHeight) {
        height[row][col] = newHeight;
    }

    // updates the hitbox position of block
    public void updateHitbox() {
        // takes the row animation number and updates the row hitbox
        this.hitboxes[row].setLocation(this.getX(), this.getY());
    }

    

    // public void dashRight(){
    //     this.row = DASH_RIGHT;
    //     this.col = (this.col + 1)%frames[row].length;
    //     // MOVE BY LLONGER DISTANCES
    //     this.x += Const.DASH;
    // }
    // public void dashLeft(){
    //     this.row = DASH_LEFT;
    //     this.col = (this.col + 1)%frames[row].length;
    //     this.x -= Const.DASH;
    // }  


    public boolean checkVerticalCollision(Sprite block) {
        int firstY = this.getY();
        int secondY = this.getY() + this.getYVel();

        // Check if samus is falling through a platform at instant 2
        if (((this.getX() >= block.getX() && this.getX() <= block.getX() + block.getWidth()) || (this.getX()+this.getWidth() >= block.getX() && this.getX()+this.getWidth() <= block.getX() + block.getWidth()))
        && ((firstY + this.getHeight() <= block.getY() && secondY + this.getHeight() >= block.getY()))){
            hitsBottom(block);
            return true;
        }

        // do top collision (samus head with platform above)
        if (((this.getX() >= block.getX() && this.getX() <= block.getX() + block.getWidth()) || (this.getX()+this.getWidth() >= block.getX() && this.getX()+this.getWidth() <= block.getX() + block.getWidth()))
        && ((firstY >= block.getY() + block.getHeight() && secondY <= block.getY() + block.getHeight()))){
            // You need this plus one so the if statement code does not execute again and so samus' head does not sticks to the wall!
            hitsTop(block);
            return true;
        }
        return false;
    }


    public void hitsBottom(Sprite entity) {
        if(entity instanceof Button && this instanceof Samus) {
            ((Samus)this).setEnterDoor(true);
        } else if(this instanceof Samus && entity instanceof Door) {
            ((Door)entity).completeLevel();
        } else if(this instanceof Samus && entity instanceof Block) {
            this.setYVel(0); // this is the problem
            // technically can be just this.getHeight()
            this.setY((int) (entity.getY() - this.getHeight()));
        } else if(this instanceof Portal && entity instanceof Pblock && ((Portal)this).canCollide()) {
            // determine direction of portal based on y velocity (up or down) before removing y velocity
            this.showDownRight();
            ((Portal) this).placePortal((Pblock)entity);
        } else if(this instanceof Portal && entity instanceof Block && ((Portal)this).canCollide()) {
            // This was the only I could think of deleting the block (set it out of bounds, my boundary method deletes it)
            final int OUT_OF_BOUNDS = -100;
            this.setX(OUT_OF_BOUNDS);
            this.setY(OUT_OF_BOUNDS);
        }
    }

    public void hitsTop(Sprite entity) {
        if(entity instanceof Button && this instanceof Samus) {
            ((Samus)this).setEnterDoor(true);
        } else if(this instanceof Samus && entity instanceof Door) {
            ((Door)entity).completeLevel();
        } else if(this instanceof Samus && entity instanceof Block) {
            this.setY((int) (entity.getY() + entity.getHeight()+1));
        } else if(this instanceof Portal && entity instanceof Pblock && ((Portal)this).canCollide()) {
            // determine direction of portal based on y velocity (up or down) before removing y velocity
            this.showUpRight();
            ((Portal) this).placePortal((Pblock)entity);
        } else if(this instanceof Portal && entity instanceof Block && ((Portal)this).canCollide()) {
            // Delete portal if touching a any other block that is not a portal-able block
            final int OUT_OF_BOUNDS = -100;
            this.setX(OUT_OF_BOUNDS);
            this.setY(OUT_OF_BOUNDS);
        }
    }

    public void hitsRight(Sprite entity) {
        if(entity instanceof Button && this instanceof Samus) {
            ((Samus)this).setEnterDoor(true);
        } else if(this instanceof Samus && entity instanceof Door) {
            ((Door)entity).completeLevel();
        } else if(entity instanceof Door && this instanceof Samus && ((Samus)this).getEnterDoor()) {
            ((Door)entity).completeLevel();
        } else if(this instanceof Samus) {
            this.setX(this.getX() - this.getWidth()/2); // minus 1 so samus is no on block still
            this.setXVel(0);
        } else if(this instanceof Portal && entity instanceof Pblock && ((Portal)this).canCollide()) {
            // determine direction of portal based on y velocity (up or down) before removing y velocity
            this.showRight();
            ((Portal) this).placePortal((Pblock)entity);
        } else if(this instanceof Portal && entity instanceof Block && ((Portal)this).canCollide()) {
            final int OUT_OF_BOUNDS = -100;
            this.setX(OUT_OF_BOUNDS);
            this.setY(OUT_OF_BOUNDS);
        }
    }

    public void hitsLeft(Sprite entity) {
        if(entity instanceof Button && this instanceof Samus) {
            ((Samus)this).setEnterDoor(true);
        } else if(this instanceof Samus && entity instanceof Door) {
            ((Door)entity).completeLevel();
        } else if(this instanceof Samus) {
            this.setX(entity.getX() + entity.getWidth() + 1);
            this.setXVel(0);
        } else if(this instanceof Portal && entity instanceof Pblock && ((Portal)this).canCollide()) {
            // determine direction of portal based on y velocity (up or down) before removing y velocity
            this.showLeft();
            ((Portal) this).placePortal((Pblock)entity);
        } else if(this instanceof Portal && entity instanceof Block && ((Portal)this).canCollide()) {
            final int OUT_OF_BOUNDS = -100;
            this.setX(OUT_OF_BOUNDS);
            this.setY(OUT_OF_BOUNDS);
        }
    }

    // Left and Right collision
    public boolean checkHorizontalCollision(Sprite block) {
        int firstX = this.getX();
        int secondX = this.getX() + this.getXVel();
        // selected obj on right collide to obj on left collision (object's right collision)
        if (((this.getY() > block.getY() && this.getY() < block.getY() + block.getHeight()) || (this.getY()+this.getHeight() > block.getY() && this.getY()+this.getHeight() < block.getY() + block.getHeight()))
        && ((firstX + this.getWidth() <= block.getX() && secondX + this.getWidth() >= block.getX()))) {
            this.hitsRight(block);
            return true;
        }

        // Annoyingly, my sprite animations do not have the same dimensions. Their widths are different, making the animations clumsy
        // selected obj on left collide to obj on right collision (object's left collision)
        if (((this.getY() > block.getY() && this.getY() < block.getY() + block.getHeight()) || (this.getY()+this.getHeight() > block.getY() && this.getY()+this.getHeight() < block.getY() + block.getHeight()))
        && ((firstX >= block.getX() + block.getWidth() && secondX <= block.getX() + block.getWidth()))){
            this.hitsLeft(block);
            return true;
        }

        return false;
    }

    // iterate through array of blocks to detect collision
    // basically if anyone block is touching samus' feet, yVel should always be zero
    public boolean checkVerticalBlocks(Block[][] blocks) {
        for(int i = 0; i < Const.NUMBER_OF_BLOCK_ROWS; i++) {
            for(int j= 0; j < Const.NUMBER_OF_BLOCK_COLUMNS; j++) {
                if(blocks[i][j] != null && this.checkVerticalCollision(blocks[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkHorizontalBlocks(Block[][] blocks) {
        for(int i = 0; i < Const.NUMBER_OF_BLOCK_ROWS; i++) {
            for(int j = 0; j < Const.NUMBER_OF_BLOCK_COLUMNS; j++) {
                if(blocks[i][j] != null && this.checkHorizontalCollision(blocks[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }
}