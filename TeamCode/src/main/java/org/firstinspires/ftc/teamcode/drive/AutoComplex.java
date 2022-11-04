package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.components.AprilDetection;
import org.firstinspires.ftc.teamcode.drive.components.Arm;
import org.firstinspires.ftc.teamcode.drive.components.Bot;
import org.firstinspires.ftc.teamcode.drive.components.Grabber;
import org.firstinspires.ftc.teamcode.drive.components.Stabiliser;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;


public class AutoComplex extends LinearOpMode {
    public static double topLift = 0.91;
    public static double downLift = 0.86;
    private boolean isRed = true;

    public final double oneeighty = Math.toRadians(isRed ? 180 : 0);
    public final double zero = Math.toRadians(isRed ? 0 : 180);
    public final double ninety = Math.toRadians(isRed ? 90 : 270);

    @Override
    public void runOpMode() throws InterruptedException {
        int m = isRed ? 1 : -1;

        // Declare your drive class
        Bot drive = new Bot(hardwareMap);
        Arm arm = new Arm(hardwareMap.dcMotor.get("armMotor"));
        Grabber grabber = new Grabber(hardwareMap.crservo.get("grabLeft"), hardwareMap.crservo.get("grabRight"));
        AprilDetection aprilDetection = new AprilDetection(drive.getCamera(), hardwareMap);

        // Right side of red alliance, almost exactly facing the cone
        Pose2d startPose = new Pose2d(38.5*m, -65*m, ninety);

        drive.setPoseEstimate(startPose);

        while (!isStarted()) {
            aprilDetection.update(telemetry);
            telemetry.update();
            sleep(20);
        }
        Stabiliser stabiliser = new Stabiliser(hardwareMap.servo.get("stabiliser"));

        if (isStopRequested()) return;

        Comparable<Integer> detected = aprilDetection.getDetected();

        if (detected == null) {
            telemetry.addData("No April tag detected", "");
            telemetry.update();

            detected = 2;
        }

        arm.setPosition((int) (topLift * Arm.topPosition));
        stabiliser.update(topLift * Arm.topPosition, false);

        TrajectorySequence start = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(37*m, -50*m), ninety)
                .splineTo(new Vector2d(24*m, -30*m), ninety)
                .build();
        drive.followTrajectorySequence(start);

        arm.setPosition((int) (downLift * Arm.topPosition));
        stabiliser.update(downLift * Arm.topPosition, false);
        grabber.update(false, true);

        sleep(3000);

        grabber.update(false,false);
        arm.setPosition((int) (topLift * Arm.topPosition));
        stabiliser.update(topLift * Arm.topPosition, false);

        sleep(3000);

        TrajectorySequenceBuilder finish = drive.trajectorySequenceBuilder(start.end()).setReversed(true);
        if (detected.compareTo(1) == 0) {
            drive.followTrajectorySequence(
                    finish
                            .splineTo(new Vector2d(10*m, -36*m), oneeighty)
                            .build()
            );
        } else if (detected.compareTo(2) == 0) {
            drive.followTrajectorySequence(
                    finish
                            .splineTo(new Vector2d(37*m, -36*m), zero)
                            .build()
            );
        } else if (detected.compareTo(3) == 0) {
            drive.followTrajectorySequence(
                    finish
                            .back(5)
                            .turn(Math.toRadians(90))
                            .splineTo(new Vector2d(64*m, -36*m), zero)
                            .build()
            );
        }


        // Transfer the current pose to PoseStorage so we can use it in TeleOp
        PoseStorage.currentPose = drive.getPoseEstimate();
    }
}