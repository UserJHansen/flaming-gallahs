package org.firstinspires.ftc.teamcode.drive.components;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Arm {
    private final DcMotor armMotor;

    public static int topPosition = 1400;
    public static double moveFactor = 12f;

    public Arm(DcMotor armMotor) {
        this.armMotor = armMotor;

        this.reset();
        this.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setPower(0.5);
    }

    public void reset() {
        this.armMotor.setTargetPosition(0);
        this.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void changePosition(double change) {
        this.armMotor.setTargetPosition((int) (this.armMotor.getTargetPosition() + change * moveFactor));
    }

    public void setPosition(int pos) {
        this.armMotor.setTargetPosition(pos);
    }

    public int getPosition() {
        return this.armMotor.getCurrentPosition();
    }
}
