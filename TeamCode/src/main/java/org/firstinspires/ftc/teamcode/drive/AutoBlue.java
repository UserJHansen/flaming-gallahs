package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.components.AprilDetection;
import org.firstinspires.ftc.teamcode.drive.components.Bot;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

@Config
@Autonomous(group = "advanced")
public class AutoBlue extends LinearOpMode {
    public static double DistanceLeft = 28;
    public static double DistanceRight = 28;

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare your drive class
        Bot drive = new Bot(hardwareMap);
        AprilDetection aprilDetection = new AprilDetection(drive.getCamera(), hardwareMap);

        // Right side of red alliance, almost exactly facing the cone
        Pose2d startPose = new Pose2d(-38.5, 65, Math.toRadians(270));

        drive.setPoseEstimate(startPose);

        while (!isStarted()) {
            aprilDetection.update(telemetry);
            telemetry.update();
            sleep(20);
        }

        if (isStopRequested()) return;

        Comparable<Integer> detected = aprilDetection.getDetected();

        if (detected == null) {
            telemetry.addData("No April tag detected", "");
            telemetry.update();

            detected = 2;
        }

        TrajectorySequenceBuilder start = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(-37, 36), Math.toRadians(270))
                .waitSeconds(2);


        if (detected.compareTo(1) == 0) {
            drive.followTrajectorySequence(
                    start
                            .strafeLeft(DistanceLeft)
                            .build()
            );
        } else if (detected.compareTo(2) == 0) {
            drive.followTrajectorySequence(
                    start
                            .build()
            );
        } else if (detected.compareTo(3) == 0) {
            drive.followTrajectorySequence(
                    start
                            .strafeRight(DistanceRight)
                            .build()
            );
        }


        // Transfer the current pose to PoseStorage so we can use it in TeleOp
        PoseStorage.currentPose = drive.getPoseEstimate();
    }
}