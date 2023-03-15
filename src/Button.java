public class Button extends Block {
    private int doorNumber;
    static public boolean pressed; // this needs to be static as the door and button share the same variable
    Button(int x, int y, String picName, int rows, int columns, int doorNumber) {
        super(x, y, picName, rows, columns);
        this.doorNumber = doorNumber;
        this.pressed = false;
    }

    // public int getDoorNumber() {
    //     return this.doorNumber;
    // }
    // public void setDoorNumber(int doorNumber) {
    //     this.doorNumber = doorNumber;
    // }

    // public boolean getPressed() {
    //     return pressed;
    // }

    // want to make generic. Use E
    // public boolean isPressed(Object obj) {
    //     // check collision
    //     if(obj.hasMass() == true && )
    // }

    // public <E> void isPressed(E obj) {
    //     // Only objects with mass can press button (not portals)
    //     if(obj instanceof Species && ((Species)obj).hasMass()) {
    //         this.pressed = true;
    //         this.row = LEFT; // set to pressed animation
    //     }
    //     // create another object for inanimate objects
    // }
}
