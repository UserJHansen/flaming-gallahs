package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.components.Arm;
import org.firstinspires.ftc.teamcode.drive.components.Bot;
import org.firstinspires.ftc.teamcode.drive.components.ConeTipper;
import org.firstinspires.ftc.teamcode.drive.components.Grabber;
import org.firstinspires.ftc.teamcode.drive.components.Stabiliser;
import org.firstinspires.ftc.teamcode.drive.components.TipperState;
import org.firstinspires.ftc.teamcode.util.Button;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;

/**
 * This opmode demonstrates how to create a teleop using just the SampleMecanumDrive class without
 * the need for an external robot class. This will allow you to do some cool things like
 * incorporating live trajectory following in your teleop. Check out TeleOpAgumentedDriving.java for
 * an example of such behavior.
 * <p>
 * This opmode is essentially just LocalizationTest.java with a few additions and comments.
 */
@Config
@TeleOp(group = "advanced")
public class TeleOpDrive extends LinearOpMode {
    public static double SlowmodeSpeed = 0.25;
    public static double TurnSlow = 1.3;
    public static double ForwardGrabber = 0.2;
    public static double BackwardLift = 0.1;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the Bot
        Bot drive = new Bot(hardwareMap);
        ConeTipper tipper = new ConeTipper(hardwareMap.servo.get("flip"));
        Arm arm = new Arm(hardwareMap.dcMotor.get("armMotor"));
        Grabber grabber = new Grabber(hardwareMap.crservo.get("grabLeft"), hardwareMap.crservo.get("grabRight"));
        Stabiliser stabiliser = new Stabiliser(hardwareMap.servo.get("stabiliser"));

        Button slowMode = new Button(false, (val) -> {
            drive.SLOW = val;
            if (val) {
                drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else {
                drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
        });

        Button tipperButton = new Button(true, (val) -> {
            if (val) {
                tipper.setLocation(TipperState.UP);
            } else {
                tipper.setLocation(TipperState.DOWN);
            }
        });

        // We want to turn off velocity control for teleop
        // Velocity control per wheel is not necessary outside of motion profiled auto
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Retrieve our pose from the PoseStorage.currentPose static field
        // See AutoTransferPose.java for further details
        drive.setPoseEstimate(PoseStorage.currentPose);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            // Declare telemetry packet for dashboard field drawing
            TelemetryPacket packet = new TelemetryPacket();
            Canvas fieldOverlay = packet.fieldOverlay();
            Pose2d target = drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * (slowMode.val ? SlowmodeSpeed : 1) + (gamepad1.dpad_up ? ForwardGrabber : 0) - (gamepad1.left_trigger * BackwardLift),
                            -gamepad1.left_stick_x * (slowMode.val ? SlowmodeSpeed : 1),
                            -gamepad1.right_stick_x * (slowMode.val ? SlowmodeSpeed * TurnSlow : 1)
                    )
            );
            telemetry.addData("Drive", MecanumKinematics.robotToWheelVelocities(new Pose2d(
                    -gamepad1.left_stick_y * (slowMode.val ? SlowmodeSpeed : 1) + (gamepad1.dpad_up ? ForwardGrabber : 0) - (gamepad1.left_trigger * BackwardLift),
                    gamepad1.left_stick_x * (slowMode.val ? SlowmodeSpeed : 1),
                    -gamepad1.right_stick_x * (slowMode.val ? SlowmodeSpeed * TurnSlow : 1)
            ),1.0,1.0,1.0));
            telemetry.addData("x-v", -gamepad1.left_stick_y * (slowMode.val ? SlowmodeSpeed : 1) + (gamepad1.dpad_up ? ForwardGrabber : 0) + (gamepad1.dpad_down ? -ForwardGrabber : 0) - (gamepad1.left_trigger * BackwardLift));
            telemetry.addData("y-v", gamepad1.left_stick_x * (slowMode.val ? SlowmodeSpeed : 1));
            telemetry.addData("h-v", -gamepad1.right_stick_x * (slowMode.val ? SlowmodeSpeed * TurnSlow : 1));

            // Update everything. Odometry. Etc.
            drive.update();
            try {
                slowMode.update(gamepad1.b);
                tipperButton.update(gamepad1.back);
            } catch (Exception e) {
                e.printStackTrace();
            }
            arm.changePosition(gamepad1.left_trigger - gamepad1.right_trigger);
            if (gamepad1.a) {
                arm.setPosition((int) (0.94 * Arm.topPosition));
            }
            grabber.update(gamepad1.dpad_up, gamepad1.dpad_down);
            stabiliser.update((float) arm.getPosition() / Arm.topPosition, gamepad1.y);

            // Read pose
            Pose2d poseEstimate = drive.getPoseEstimate();

            if (gamepad1.x) {
//                drive.setPoseEstimate(new Pose2d(
//                        poseEstimate.getX(),
//                        poseEstimate.getY(),
//                        Math.toRadians(Math.round(Math.toDegrees(poseEstimate.getHeading()) / 90) * 90)
//                ));
                arm.reset();
            }

            fieldOverlay.setStroke("#3F51B5");
            DashboardUtil.drawRobot(fieldOverlay, poseEstimate);
            fieldOverlay.setStroke("#fc0a36");
            DashboardUtil.drawRobot(fieldOverlay, new Pose2d(target.getX() + poseEstimate.getX(), target.getY() + poseEstimate.getY(), target.getHeading() + poseEstimate.getHeading()));
//            fieldOverlay.setStroke("#0afcd0");
//            DashboardUtil.drawRobot(fieldOverlay, drive.getVuforiaPoseEstimate());


            // Send telemetry packet off to dashboard
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
            // Print pose to telemetry
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("heading (deg)", Math.toDegrees(poseEstimate.getHeading()));
            telemetry.addData("arm", arm.getPosition());
            telemetry.addData("arm %", ((float) arm.getPosition()/(float) Arm.topPosition)*100);
            telemetry.addData("assist", drive.powerSteering());
            telemetry.addData("Flipper", tipperButton.val);
            telemetry.addData("Slow", slowMode.val);
            telemetry.update();
        }
    }
}
