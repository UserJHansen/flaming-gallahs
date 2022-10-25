package org.firstinspires.ftc.teamcode.drive.components;

import com.qualcomm.robotcore.hardware.Servo;

public class ConeTipper {
    private Servo leftServo;
    private Servo rightServo;
    private TipperState state;

    public ConeTipper(Servo leftServo, Servo rightServo) {
        this.leftServo = leftServo;
        this.rightServo = rightServo;

        this.setLocation(TipperState.UP);
    }

    public void setLocation(TipperState state) {
        leftServo.setPosition(state.ordinal());
        rightServo.setPosition(1-state.ordinal());

        this.state = state;
    }

    public TipperState getState() {
        return state;
    }
}
