public class Door extends Block {
    private int buttonNumber;
    private boolean locked;
    Door(int x, int y, String picName, int rows, int columns, int buttonNumber) {
        super(x, y, picName, rows, columns);
        this.buttonNumber = buttonNumber;
        this.locked = true;
    }

    // public int getButtonNumber() {
    //     return this.buttonNumber;
    // }
    // public void setButtonNumber(int buttonNumber) {
    //     this.buttonNumber = buttonNumber;
    // }

    // public boolean unlocked(Button button) {
    //     if(button.getPressed() == true) {
    //         return true;
    //     }
    // }

    public void completeLevel() {
        // return to main menu.
        System.out.println("BEAT LEVEL ");
    }
    
}
