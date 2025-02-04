package com.userjhansen.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d start = new Pose2d(38.5, -65, Math.toRadians(90));
        Vector2d coneAvoid = new Vector2d(37, -50);
        Vector2d coneCap = new Vector2d(24, -30);

        RoadRunnerBotEntity red1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 11)
                .setDimensions(16, 11.2)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(start)
                                .splineTo(coneAvoid, Math.toRadians(90))
                                .splineTo(coneCap, Math.toRadians(90))
                                .setReversed(true)
                                .splineTo(new Vector2d(10, -36), Math.toRadians(180))
                                .build()
                );

        RoadRunnerBotEntity red2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 11)
                .setDimensions(16, 11.2)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(start)
                                .splineTo(coneAvoid, Math.toRadians(90))
                                .splineTo(coneCap, Math.toRadians(90))
                                .setReversed(true)
                                .splineTo(new Vector2d(37, -36), Math.toRadians(0))
                                .build()
                );

        RoadRunnerBotEntity red3 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 11)
                .setDimensions(16, 11.2)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(start)
                                .splineTo(coneAvoid, Math.toRadians(90))
                                .splineTo(coneCap, Math.toRadians(90))
                                .setReversed(true)
                                .back(5)
                                .turn(Math.toRadians(90))
                                .splineTo(new Vector2d(64, -36), Math.toRadians(0))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(red1)
                .addEntity(red2)
                .addEntity(red3)
                .start();
    }
}
