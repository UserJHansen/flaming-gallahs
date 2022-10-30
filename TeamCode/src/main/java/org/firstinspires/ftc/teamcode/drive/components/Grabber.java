package org.firstinspires.ftc.teamcode.drive.components;

import com.qualcomm.robotcore.hardware.CRServo;

public class Grabber {
    private CRServo leftServo;
    private CRServo rightServo;
    private boolean isUp;

    public Grabber(CRServo leftServo, CRServo rightServo) {
        this.leftServo = leftServo;
        this.rightServo = rightServo;

        leftServo.setPower(0);
        rightServo.setPower(0);
    }

    public void update(boolean out, boolean in) {
        if (!(out || in)) {
            leftServo.setPower(0);
            rightServo.setPower(0);
            return;
        }
        if (out) {
            leftServo.setPower(-0.5);
            rightServo.setPower(0.5);
        } else {
            leftServo.setPower(0.5);
            rightServo.setPower(-0.5);
        }
    }
}
