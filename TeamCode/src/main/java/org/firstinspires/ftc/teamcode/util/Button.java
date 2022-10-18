package org.firstinspires.ftc.teamcode.util;

public class Button {
    private boolean isPressed = false;
    public boolean val;
    public whenPressed;

    public Button(boolean default_val, void() whenPressed) {
        this.val = default_val;
        this.whenPressed = whenPressed;
    }
    public void update(boolean new_val) {
        if (new_val && !isPressed) {
            val = !val;
            whenPressed();
        }
        isPressed = new_val;
    }
}
