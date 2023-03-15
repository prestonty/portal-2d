public class Portal extends Sprite {
    private int enterXVel;
    private int enterYVel;

    private int leaveXVel;
    private int leaveYVel;

    private String portalColour;

    protected final int FORWARD = 4;

    Portal(int x, int y, String picName, int[] ANIMATION_ARRAY, int xVel, int yVel, String portalColour) {
        super(x, y, picName, xVel, yVel, ANIMATION_ARRAY);
        this.portalColour = portalColour;
    }

// Getters & setters -----------------------------------------------------------------------------

    public int getEnterXVel() {
      return this.enterXVel;
    }
    public void setEnterXVel(int enterXVel) {
      this.enterXVel = enterXVel;
    }
    public int getEnterYVel() {
      return this.enterYVel;
    }
    public void setEnterYVel(int enterYVel) {
      this.enterYVel = enterYVel;
    }


    public int getLeaveXVel() {
      return this.leaveXVel;
    }
    public void setLeaveXVel(int leaveXVel) {
      this.leaveXVel = leaveXVel;
    }
    public int getLeaveYVel() {
      return this.leaveYVel;
    }
    public void setLeaveYVel(int leaveYVel) {
      this.leaveYVel =leaveYVel;
    }

    public String getPortalColour() {
      return this.portalColour;
    }

    public void placePortal(Block block) {
      // adjustments so portal pops out of the surface
      final int ADJUST_RIGHT = -20;
      final int ADJUST_LEFT = 20;
      final int ADJUST_UP = -20;
      final int ADJUST_DOWN = -30;
      if(row == RIGHT) {
      this.setX(block.getX() + ADJUST_RIGHT);
      this.setY(block.getY() - block.getHeight()/2);
      } else if(row == LEFT) {
        this.setX(block.getX() + ADJUST_LEFT);
        this.setY(block.getY() - block.getHeight()/2);
      } else if(row == UP_R) {
        this.setX(block.getX() - block.getWidth()/2);
        this.setY(block.getY() + ADJUST_UP);
      } else if(row == DOWN_R) {
        this.setX(block.getX() - block.getWidth()/2);
        this.setY(block.getY() + ADJUST_DOWN);
      }
      this.setYVel(0);
      this.setXVel(0);
    }

    // shows the capsule bullets that contain the portal
    public void showForward() {
      this.row = FORWARD;
      this.col = (this.col + 1)%frames[row].length;
  }

  // check if portal is in moving form (can collide with blocks)
  public boolean canCollide() {
    if(row == FORWARD)
      return true;
    return false;
  }

  
}
