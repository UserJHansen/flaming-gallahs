package org.firstinspires.ftc.teamcode.drive.components;

import com.qualcomm.robotcore.hardware.Servo;

public class ConeTipper {
    private Servo servo;
    private TipperState state;

    public ConeTipper(Servo servo) {
        this.servo = servo;

        this.setLocation(TipperState.UP);
    }

    public void setLocation(TipperState state) {
        servo.setPosition(state.ordinal());

        this.state = state;
    }

    public TipperState getState() {
        return state;
    }
}
