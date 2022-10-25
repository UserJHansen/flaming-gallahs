package org.firstinspires.ftc.teamcode.drive.old;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bot {

    HardwareMap hardwareMap;

    public DcMotorEx frontLeft = null;
    public DcMotorEx backLeft = null;
    public DcMotorEx backRight = null;
    public DcMotorEx frontRight = null;
    private boolean isSlow = false;
    private boolean isAuto = false;

    public Bot(HardwareMap hwmap) {
        hardwareMap = hwmap;
    }

    public void init(HardwareMap hardwareMap) {
        frontLeft = (DcMotorEx) hardwareMap.get("frontLeft");
        frontRight = (DcMotorEx) hardwareMap.get("frontRight");
        backRight = (DcMotorEx) hardwareMap.get("backRight");
        backLeft = (DcMotorEx) hardwareMap.get("backLeft");

    }

    public void init(HardwareMap hardwareMap, boolean isAuto) {
        this.init(hardwareMap);
        this.isAuto = isAuto;
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setBrake(boolean brake) {
        if (brake) {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

    }

    public void setSlowMode(boolean slow) {
        this.setBrake(slow);
        this.isSlow = slow;
    }

    public void driveMech(double forward, double strafe, double rotation) {

        double md0 = this.calcMDrivePwr(0, forward, strafe, rotation);
        double md1 = this.calcMDrivePwr(1, forward, strafe, rotation);
        double md2 = this.calcMDrivePwr(2, forward, strafe, rotation);
        double md3 = this.calcMDrivePwr(3, forward, strafe, rotation);

        // Keep within usable range (and scale the rest if necessary)
        double max = Math.max(Math.max(md0, md1), Math.max(md2, md3));
        double min = Math.min(Math.min(md0, md1), Math.min(md2, md3));

        double bounds = 0.9;

        if (this.isSlow) {
            bounds = 0.38;
        } else if (this.isAuto) {
            bounds = 0.7;
        }

        if (max > bounds) {
            double SF = bounds / max;
            md0 *= SF;
            md1 *= SF;
            md2 *= SF;
            md3 *= SF;
        } else if (min < -bounds) {
            double SF = -bounds / min;
            md0 *= SF;
            md1 *= SF;
            md2 *= SF;
            md3 *= SF;
        }

        // Set motor power
        frontLeft.setPower(md2);
        frontRight.setPower(md3);
        backRight.setPower(-md0);
        backLeft.setPower(-md1);
    }

    public double calcMDrivePwr(int motor, double forward, double strafe, double rotation) {
        double power = 0;
        // Scale inputs
        forward *= 1;
        strafe *= 0.7;
        rotation *= 0.5;

        // Bunch of math to determine the power (different for each motor)
        switch (motor) {
            case 0:
                power = -forward + -strafe + rotation;
                break;
            case 1:
                power = forward + -strafe + rotation;
                break;
            case 2:
                power = -forward + strafe + rotation;
                break;
            case 3:
                power = forward + strafe + rotation;
                break;
        }

        // Scale power
        power *= 1;

        return power;
    }
}