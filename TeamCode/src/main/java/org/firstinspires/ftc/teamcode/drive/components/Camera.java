package org.firstinspires.ftc.teamcode.drive.components;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class Camera {
    private static final String VUFORIA_KEY =
            "AWlNMyb/////AAABmYXZxpRFekMMmX3y7my9JDdD3LbYEz/ZEU+owaXOEoUyp3iOqFU/saX9Ml+q2/A5HdSwatV35NYcv4yGcNezVHzkpYyTwrWev4GU8EnjrhfyDYpROi2zShIhHXmrrErwVNOT+XqSwA56p080UbaDQRk4KFfwYgqtr5d6P0NRvhU3D1NdexO/ArwcisTlEpWLsQYQSDZYsYj6P+8KCO9y6VysbVTZ5/nqFoN4zbxEr8HFnfB+rZc5JRtecgbf7C7YhkQVdO6yqAzS6XdeO1Y/9c6b7kT9sHRKoBmskjutnvNVhsDMMCvzuglAD50KI/vMH3xX/DIF0mKfegVR2mA4laZOFjFGLV8YuEvcOpHxrqHc";

    public static int FORWARD_DISPLACEMENT = 60;   // eg: Camera is 110 mm in front of robot center
    public static int VERTICAL_DISPLACEMENT = 432;   // eg: Camera is 200 mm above ground
    public static int LEFT_DISPLACEMENT = 120;     // eg: Camera is ON the robot's center line

    private VuforiaLocalizer vuforia;
    private WebcamName webcamName;

    public Camera(HardwareMap hardwareMap) {
        webcamName = hardwareMap.get(WebcamName.class, "Webcam");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // We also indicate which camera we wish to use.
        parameters.cameraName = webcamName;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    public VuforiaLocalizer getVuforia() {
        return vuforia;
    }

    public WebcamName getWebcamName() {
        return webcamName;
    }
}
