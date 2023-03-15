public class Species extends Sprite {

    // RIGHT IS EVEN NUMBERS
    // LEFT IS ODD NUMBERS
    protected final int UP_L = 4;
    protected final int DOWN_L = 5;
    protected final int IDLE_R = 6;
    protected final int IDLE_L = 7;
    protected final int DASH_R = 8;
    protected final int DASH_L = 9;
    protected final int MAX_JUMPS;
    protected int remainingJumps;

    Species(int x, int y, String picName, int xVel, int yVel, int MAX_JUMPS, int[] ANIMATION_ARRAY){
        super(x, y, picName, xVel, yVel, ANIMATION_ARRAY);
        this.remainingJumps = MAX_JUMPS;
        // Different enemies have different jumps (E.g. "flying" enemies can have multiple jumps)
        this.MAX_JUMPS = MAX_JUMPS;
    }

    Species(int x, int y, String picName, int MAX_JUMPS, int[] ANIMATION_ARRAY){
        super(x, y, picName, ANIMATION_ARRAY);
        this.remainingJumps = MAX_JUMPS;
        // Different enemies have different jumps (E.g. "flying" enemies can have multiple jumps)
        this.MAX_JUMPS = MAX_JUMPS;
    }

    public void showIdleRight(){
        row = IDLE_R;
        col = (col + 1)%frames[row].length;
    }
    public void showIdleLeft(){
        row = IDLE_L;
        col = (col + 1)%frames[row].length;
    }
    public void showDownLeft() {
        this.row = DOWN_L;
        this.col = (this.col + 1)%frames[row].length;

        // you go into ball mode only if condiion is fulfilled
    }

    public void showUpLeft() {
        this.row = UP_L;
        this.col = (this.col + 1)%frames[row].length;
    }

    public void refreshJump() {
        remainingJumps = this.MAX_JUMPS;
    }


    public void accelerate(){
        this.setYVel(this.getYVel() + Const.GRAVITY);
    }
    public void moveX(){
        this.setX(this.getX() + this.getXVel());
    }

    public void moveY() {
        this.setY(this.getY() + this.getYVel()); 
    }
    public boolean isOnLevel(int level){
        return (this.getY() + this.getHeight() == level);
    }

    // Used to determine what objecs can press buttons. E.g. projectiles & enemies cannot activate buttons
    public boolean hasMass() {
        return true;
    }

}
