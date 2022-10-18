package org.firstinspires.ftc.teamcode.util;

public class Button {
    private boolean isPressed = false;
    public boolean val;
    public ButtonPressed whenPressed;

    public Button(boolean default_val, ButtonPressed whenPressed) {
        this.val = default_val;
        this.whenPressed = whenPressed;
    }
    public void update(boolean new_val) throws Exception {
        if (new_val && !isPressed) {
            val = !val;
            whenPressed.call(val);
        }
        isPressed = new_val;
    }
}
