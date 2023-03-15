public class Entrance extends Block {

    Entrance(int x, int y, String picName, int rows, int columns) {
        super(x, y, picName, rows, columns);
    }

    public void setSpawn(Samus samus) {
        // set samus postiion to top of the block
        samus.setX(this.getX() + this.getWidth()/2);
        samus.setY(this.getY() - samus.getHeight());
    }
}
