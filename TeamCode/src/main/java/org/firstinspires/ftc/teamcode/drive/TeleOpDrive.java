package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.util.Button;

/**
 * This opmode demonstrates how to create a teleop using just the SampleMecanumDrive class without
 * the need for an external robot class. This will allow you to do some cool things like
 * incorporating live trajectory following in your teleop. Check out TeleOpAgumentedDriving.java for
 * an example of such behavior.
 * <p>
 * This opmode is essentially just LocalizationTest.java with a few additions and comments.
 */
@TeleOp(group = "advanced")
public class TeleOpDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize SampleMecanumDrive
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Button slowMode = new Button(false, (val) -> {
            if (val) {
                drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else {
                drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        });

        // We want to turn off velocity control for teleop
        // Velocity control per wheel is not necessary outside of motion profiled auto
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Retrieve our pose from the PoseStorage.currentPose static field
        // See AutoTransferPose.java for further details
        drive.setPoseEstimate(PoseStorage.currentPose);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            gamepad1.left_stick_y * (slowMode.val ? 0.5 : 1),
                            -gamepad1.left_stick_x * (slowMode.val ? 0.5 : 1),
                            -gamepad1.right_stick_x * (slowMode.val ? 0.5 : 1)
                    )
            );

            // Update everything. Odometry. Etc.
            drive.update();
            try {
                slowMode.update(gamepad1.b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Read pose
            Pose2d poseEstimate = drive.getPoseEstimate();

            if (gamepad1.a) {
                drive.setPoseEstimate(new Pose2d(
                        poseEstimate.getX(),
                        poseEstimate.getY(),
                        Math.toRadians(Math.round(Math.toDegrees(poseEstimate.getHeading()) / 90) * 90)
                ));
            }

            // Print pose to telemetry
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("heading (deg)", Math.toDegrees(poseEstimate.getHeading()));
            telemetry.addData("assist", drive.powerSteering());
            telemetry.update();
        }
    }
}
