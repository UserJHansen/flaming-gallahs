package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.components.AprilDetection;
import org.firstinspires.ftc.teamcode.drive.components.Bot;

@Autonomous(group = "advanced")
public class AutoRed extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare your drive class
        Bot drive = new Bot(hardwareMap);
        AprilDetection aprilDetection = new AprilDetection(drive.getCamera(), hardwareMap);

        // Right side of red alliance, almost exactly facing the cone
        Pose2d startPose = new Pose2d(35, -66, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        while (!isStarted()) {
            aprilDetection.update(telemetry);
            sleep(20);
        }

        if (isStopRequested()) return;

        Comparable<Integer> detected = aprilDetection.getDetected();

        if (detected == null) {
            telemetry.addData("No April tag detected", "");
            telemetry.update();

            detected = 2;
        }

        Trajectory traj = drive.trajectoryBuilder(startPose)
                .forward(20)
                .build();

        if (detected.compareTo(1) == 0) {
            drive.followTrajectory(
                    drive.trajectoryBuilder(traj.end(), true)
                            .splineTo(new Vector2d(12, -34), Math.toRadians(180))
                            .build()
            );
        } else if (detected.compareTo(2) == 0) {
            drive.followTrajectory(
                    drive.trajectoryBuilder(traj.end(), true)
                            .forward(10)
                            .build()
            );
        } else if (detected.compareTo(3) == 0) {
            drive.followTrajectory(
                    drive.trajectoryBuilder(traj.end(), true)
                            .splineTo(new Vector2d(58, -34), Math.toRadians(0))
                            .build()
            );
        }


        // Transfer the current pose to PoseStorage so we can use it in TeleOp
        PoseStorage.currentPose = drive.getPoseEstimate();
    }
}