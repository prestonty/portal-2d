public final class Const{
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1200;

    // Movement constants
    public static final int GROUND = HEIGHT;
    public static final int GRAVITY = 1; // gravity
    public static final int INITIAL_Y_VEL = -15; // initial velocity of jump (15 -> 12. Lowest)
    public static final int AIR_FRICTION = 1;
    
    // I listed default values beside in case I change values for testing
    public static final int X_SPEED_GROUND = 8; // 8
    public static final int X_SPEED_AIR = 12; // 14
    // public static final int X_SPEED_BALL = 20; // 18 or 20
    // public static final int BACKGROUND_STEP = 10;
    public static final int FRAME_PERIOD = 40; // 40
/* does not go in idle, moving so slow */

    // public static final int BOSS_X_SPEED = 20;
    // public static final int ENEMY_X_SPEED = 10;
    

    // i can probably store all of these into a file later on
    // Lengths of each row used to create the jagged array
    public static final int[] SAMUS_ANIMATION_ARRAY = {10, 10, 8, 5, 8, 5, 1, 1};
    public static final int[] PORTAL_ANIMATION_ARRAY = {1, 1, 1, 1, 2};

    public static final int PORTAL_SPEED = 80; // maybe faster
    public static final String BLUE = "blue";
    public static final String PURPLE = "purple";

    // Blocks
    public static final int BLOCK_LENGTH = 50;
    public static final int NUMBER_OF_BLOCK_COLUMNS = WIDTH/BLOCK_LENGTH;
    public static final int NUMBER_OF_BLOCK_ROWS = HEIGHT/BLOCK_LENGTH;

    // Legend for blocks
    public static final String BLOCK = "x";
    // Tile that can have a portal
    public static final String P_BLOCK = "p";
    public static final String BUTTON = "b";
    public static final String DOOR = "d";
    public static final String ENTRANCE = "e";
    public static final String RAY = "r";
    public static final String SPACE = " ";

    private Const(){
    }
}