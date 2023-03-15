import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Samus extends Species {
    private Cannon cannon;
    private boolean enterDoor;
    Samus(int x, int y, String picName, int MAX_JUMPS, int[] ANIMATION_ARRAY) {
        super(x, y, picName, MAX_JUMPS, ANIMATION_ARRAY);
        // all right sprites have an even row number, odd for left

        // Only 2 cannon pictures for left and right. Rotate the cannon
        this.cannon = new Cannon(x, y, "images/playerSprite/larger/straightCannon", 4, 1);
        this.enterDoor = false;
    }

    // allows samus to enter doors if button is pressed
    public boolean getEnterDoor() {
        return this.enterDoor;
    }
    public void setEnterDoor(boolean state) {
        this.enterDoor = state;
    }

    public Cannon getCannon() {
        return this.cannon;
    }

    // if samus hits enter portal, change exit portals stuff
    public void hitsPortal(Portal enterPortal, Portal exitPortal) {
        final int BOOST = 3;
        if(exitPortal != null && enterPortal != null) {

            enterPortal.setEnterXVel(this.getXVel());
            enterPortal.setEnterYVel(this.getYVel());

            // Portal velocity logic

            // SAME AXIS TELEPORTATION
            if(enterPortal.row == DOWN_R && exitPortal.row == DOWN_R) { // points up, placed down on tile
                exitPortal.setLeaveYVel(-enterPortal.getEnterYVel() - BOOST); // set exit to correct vel. Want boost so samus makes it out of portal
                this.setX(exitPortal.getX() + exitPortal.getWidth()/2);
                this.setY(exitPortal.getY() - this.getHeight());
            } else if(enterPortal.row == UP_R && exitPortal.row == UP_R) {
                exitPortal.setLeaveYVel(-enterPortal.getEnterYVel()); // set exit to correct vel
                this.setX(exitPortal.getX() + exitPortal.getWidth()/2); // set position to exit portal
                this.setY(exitPortal.getY() - exitPortal.getHeight());
            } else if(enterPortal.row == UP_R && exitPortal.row == DOWN_R) {
                exitPortal.setLeaveYVel(enterPortal.getEnterYVel()); // set exit to correct vel (no need to change). Should I add boost?
                this.setX(exitPortal.getX() + exitPortal.getWidth()/2);
                this.setY(exitPortal.getY() - exitPortal.getHeight()/2);
            } else if (enterPortal.row == DOWN_R && exitPortal.row == UP_R) { // points up, placed down on tile
                exitPortal.setLeaveYVel(enterPortal.getEnterYVel()); // set exit to correct vel (no need to change). Should I add boost?
                this.setX(exitPortal.getX() + exitPortal.getWidth()/2);
                this.setY(exitPortal.getY() + exitPortal.getHeight()/2);
            } else if(enterPortal.row == RIGHT && exitPortal.row == RIGHT) {
                exitPortal.setLeaveXVel(-enterPortal.getEnterXVel()); // set exit to correct vel
                this.setX(exitPortal.getX() + -this.getWidth());
                this.setY(exitPortal.getY());
            } else if(enterPortal.row == LEFT && exitPortal.row == LEFT) {
                exitPortal.setLeaveXVel(-enterPortal.getEnterXVel()); // set exit to correct vel
                this.setX(exitPortal.getX() + exitPortal.getWidth());
                this.setY(exitPortal.getY());
            } else if(enterPortal.row == RIGHT && exitPortal.row == LEFT) {
                exitPortal.setLeaveXVel(enterPortal.getEnterXVel()); // set exit to correct vel
                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY());
            } else if(enterPortal.row == LEFT && exitPortal.row == RIGHT) {
                exitPortal.setLeaveXVel(enterPortal.getEnterXVel()); // set exit to correct vel
                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY());
            } else if((enterPortal.row == RIGHT || enterPortal.row == LEFT) && exitPortal.row == UP_R) { // DIFFERENT AXIS TELEPORTATION
                // make it negative
                if(enterPortal.getEnterXVel() <= 0)
                    exitPortal.setLeaveYVel(-enterPortal.getEnterXVel());
                else
                    exitPortal.setLeaveYVel(enterPortal.getEnterXVel()); // set exit to correct vel

                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY());
            } else if(enterPortal.row == RIGHT && exitPortal.row == DOWN_R) {
                // make it positive
                if(enterPortal.getEnterXVel() >= 0)
                    exitPortal.setLeaveYVel(-enterPortal.getEnterXVel());
                else
                    exitPortal.setLeaveYVel(enterPortal.getEnterXVel()); // set exit to correct vel
                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY() - this.getHeight());
            } else if( enterPortal.row == LEFT && exitPortal.row == DOWN_R) {
                // make it positive
                if(enterPortal.getEnterXVel() >= 0)
                    exitPortal.setLeaveYVel(-enterPortal.getEnterXVel());
                else
                    exitPortal.setLeaveYVel(enterPortal.getEnterXVel()); // set exit to correct vel
                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY() - this.getHeight());
            } else if((enterPortal.row == UP_R || enterPortal.row == DOWN_R) && exitPortal.row == RIGHT) {
                // make it negative (move left, portal on right)
                if(enterPortal.getEnterYVel() >= 0)
                    exitPortal.setLeaveXVel(-enterPortal.getEnterYVel());
                else
                    exitPortal.setLeaveXVel(enterPortal.getEnterYVel()); // set exit to correct vel
                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY());
            } else if((enterPortal.row == UP_R || enterPortal.row == DOWN_R) && exitPortal.row == LEFT) {
                // make it positive (move right, portal on left)
                if(enterPortal.getEnterYVel() <= 0)
                    exitPortal.setLeaveXVel(-enterPortal.getEnterYVel());
                else
                    exitPortal.setLeaveXVel(enterPortal.getEnterYVel()); // set exit to correct vel
                this.setX(exitPortal.getX());
                this.setY(exitPortal.getY());
            }

            this.setXVel(exitPortal.getLeaveXVel());
            this.setYVel(exitPortal.getLeaveYVel());
        }
    }

    // Cannon refers to Samus' arm
    public class Cannon extends Sprite {
        // This gun will just be attached to samus at a certain point (the exact middle of the image (the shoulder))
        private double angle;
        Cannon(int x, int y, String picName, int rows, int columns) {
            super(x, y, picName, rows, columns);
        }

        // Aligns cannon onto samus' shoulder
        public void alignCannon() {
            // aligns cannon with shoulder better, must change if I change image
            final int X_DIST_SHOULDER_R = 36;
            final int X_DIST_SHOULDER_L = 30;
            final int Y_DIST_SHOULDER = 18;

            // makes sure all elements have proper dimensions since animation indices 2 and 3 are empty at initialization
            if(this.getWidth() == 0) {
                this.setWidth(this.frames[0][0].getWidth());
            }
            if(this.getHeight() == 0) {
                this.setHeight(this.frames[0][0].getHeight());
            }

            // Object is facing right if animation row number is even, left if animation row number is odd
            if(Samus.this.row % 2 == 0) {
                this.row = 2;
                this.col = 0;
                this.setX(Samus.this.getX() + X_DIST_SHOULDER_R - (this.getWidth()/2));
            } else {
                this.row = 3;
                this.col = 0;
                this.setX(Samus.this.getX() + X_DIST_SHOULDER_L - (this.getWidth()/2));
            }
            this.setY(Samus.this.getY() + Y_DIST_SHOULDER - (this.getHeight()/2));

            // Cannon Rotation. Rotated images are in indices 2 and 3 (right and left respectively)
            this.frames[Samus.this.row%2 + 2][0] = this.frames[Samus.this.row%2][0];
            this.frames[Samus.this.row%2 + 2][0] = this.rotateImage(this.frames[Samus.this.row%2][0]);
        }

        // the pivot point for image is in the middle of the shoulder
        public double getPivotX() {
            return (this.getX() + (this.getWidth()/2));
        }
        public double getPivotY() {
            return (this.getY() + (this.getHeight()/2));
        }

        public double getMouseX() {
            Point point = MouseInfo.getPointerInfo().getLocation();
            return point.getX();
        }
        public double getMouseY() {
            Point point = MouseInfo.getPointerInfo().getLocation();
            return point.getY();
        }

        public double getAngle() {
            // inverse tangent of rise over run gives angle in radians
            double rise = -this.getPivotY() + this.getMouseY(); // ***pivot minus mouse because of inverted y axis
            double run = -this.getPivotX() + this.getMouseX();
            return Math.toDegrees(Math.atan2(rise,run));
        }


        


        //------------------------------------------------------------------------------    
        public BufferedImage rotateImage(BufferedImage image){
            BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            Graphics2D g2d = rotatedImage.createGraphics();
            if(this.row % 2 == 0) {
                // facing right
                this.angle = this.getAngle();
            } else if(this.row % 2 == 1) {
                // facing left
                this.angle = this.getAngle() + 180;
            }

            g2d.rotate(Math.toRadians(this.angle), image.getWidth()/2, image.getHeight()/2);
            g2d.drawImage(image, null, 0, 0);
            g2d.dispose();
            
            return rotatedImage;
        }
    }
}
