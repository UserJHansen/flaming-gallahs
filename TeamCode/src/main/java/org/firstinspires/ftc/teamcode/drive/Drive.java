package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Drive", group="Iterative Opmode")

public class Drive extends OpMode
{
    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    private Bot robot = new Bot(hardwareMap);

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        robot.init(hardwareMap, false);

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (gamepad1.left_bumper || gamepad2.left_bumper){
            robot.setSlowMode(true);
        } else if (gamepad1.right_bumper|| gamepad2.right_bumper) {
            robot.setSlowMode(false);
        }

        robot.driveMech(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        telemetry.addData("Speed: RF ", robot.frontRight.getVelocity());
        telemetry.addData("LF",robot.frontLeft.getVelocity());
        telemetry.addData("LB",robot.backLeft.getVelocity());
        telemetry.addData("RB",robot.backRight.getVelocity());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
