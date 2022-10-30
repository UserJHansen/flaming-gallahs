package org.firstinspires.ftc.teamcode.drive.components;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Config
public class WebcamVuforiaLocalizer implements Localizer {

    public static double LOW_FREQ_WEIGHT = 0.02;

    private static final float mmPerInch = 25.4f;
    private static final float mmFTCFieldWidth = (12 * 6) * mmPerInch;
    private static final float mmTargetHeight = (6) * mmPerInch;
    private static final float halfField = 72 * mmPerInch;
    private static final float halfTile = 12 * mmPerInch;
    private static final float oneAndHalfTile = 36 * mmPerInch;

    private OpenGLMatrix lastLocation = null;
    private VuforiaTrackables targets = null;
    private WebcamName webcamName = null;
    private List<VuforiaTrackable> allTrackables = new ArrayList<>();

    private boolean targetVisible = false;

    private Localizer highFreqLocalizer;
    private Pose2d poseEstimate;

    public WebcamVuforiaLocalizer(Localizer highFreqLocalizer, Camera camera) {
        this.highFreqLocalizer = highFreqLocalizer;

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targets = camera.getVuforia().loadTrackablesFromAsset("PowerPlay");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables.addAll(targets);

        identifyTarget(0, "Red Audience Wall", -halfField, -oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(1, "Red Rear Wall", halfField, -oneAndHalfTile, mmTargetHeight, 90, 0, -90);
        identifyTarget(2, "Blue Audience Wall", -halfField, oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(3, "Blue Rear Wall", halfField, oneAndHalfTile, mmTargetHeight, 90, 0, -90);

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(Camera.FORWARD_DISPLACEMENT, Camera.LEFT_DISPLACEMENT, Camera.VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(camera.getWebcamName(), cameraLocationOnRobot);
        }

        targets.activate();
    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {
        return poseEstimate;
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        this.poseEstimate = pose2d;
    }

    @Override
    public void update() {
        highFreqLocalizer.setPoseEstimate(poseEstimate);
        highFreqLocalizer.update();
        Pose2d highFreqPoseEstimate = highFreqLocalizer.getPoseEstimate();

        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                targetVisible = true;

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        if (targetVisible) {
            VectorF translation = lastLocation.getTranslation();
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);

            Pose2d lowFreqPoseEstimate = new Pose2d(translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, rotation.thirdAngle);
            poseEstimate = lowFreqPoseEstimate.times(LOW_FREQ_WEIGHT).plus(highFreqPoseEstimate.times(1 - LOW_FREQ_WEIGHT));
        } else {
            poseEstimate = highFreqPoseEstimate;
        }
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return highFreqLocalizer.getPoseVelocity();
    }

    public Pose2d getVuforiaPoseEstimate() {
        if (lastLocation != null){
        VectorF translation = lastLocation.getTranslation();
        Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);

        return new Pose2d(translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, rotation.thirdAngle);} else {
            return highFreqLocalizer.getPoseEstimate();
        }
    }

    /***
     * Identify a target by naming it, and setting its position and orientation on the field
     * @param targetIndex
     * @param targetName
     * @param dx, dy, dz  Target offsets in x,y,z axes
     * @param rx, ry, rz  Target rotations in x,y,z axes
     */
    void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }
}
