package org.firstinspires.ftc.teamcode.drive.components;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Arm {
    private DcMotor armMotor;

    public static int topPosition = 1400;
    public static double moveFactor = 12f;

    public Arm(DcMotor armMotor) {
        this.armMotor = armMotor;

        this.armMotor.setTargetPosition(0);
        this.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(0.5);
    }

    public void changePosition(double change) {
        this.armMotor.setTargetPosition(Math.min((int) (this.armMotor.getTargetPosition() + change * moveFactor), topPosition));
    }

    public void setPosition(int pos) {
        this.armMotor.setTargetPosition(pos);
    }

    public boolean isUp() {
        return this.armMotor.getTargetPosition() < topPosition / 2;
    }

    public int getPosition() {
        return this.armMotor.getCurrentPosition();
    }
}
