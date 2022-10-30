package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.components.Bot;
import org.firstinspires.ftc.teamcode.drive.components.Detection;

import java.util.List;

@Autonomous(group = "advanced")
public class AutoRed extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare your drive class
        Bot drive = new Bot(hardwareMap);
        Detection detection = new Detection(drive.getCamera(), hardwareMap);

        // Right side of red alliance, almost exactly facing the cone
        Pose2d startPose = new Pose2d(35, -66, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        waitForStart();

        if (isStopRequested()) return;

        List<Recognition> detectedCones = detection.getRecognitions();
        int zone = 2;

        // If we can't detect any cones, just go to the default position
        if (detectedCones.size() != 0) {
            Recognition cone = detectedCones.get(0);
            zone = Integer.parseInt(cone.getLabel().substring(0, 1));
        }


        Trajectory traj = drive.trajectoryBuilder(startPose)
                .forward(20)
                .build();

        if (zone == 1) {
            drive.followTrajectory(
                    drive.trajectoryBuilder(traj.end(), true)
                            .splineTo(new Vector2d(12, -34), Math.toRadians(180))
                            .build()
            );
        } else if (zone == 2) {
            drive.followTrajectory(
                    drive.trajectoryBuilder(traj.end(), true)
                            .forward(10)
                            .build()
            );
        } else if (zone == 3) {
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