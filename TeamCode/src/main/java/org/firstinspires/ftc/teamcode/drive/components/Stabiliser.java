package org.firstinspires.ftc.teamcode.drive.components;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Stabiliser {
    public static double minServo = 0.8;
    public static double maxServo = 0.35;
    public static double buttonAffect = 0.1;
    public Servo servo;

    public Stabiliser(Servo servo) {
        this.servo = servo;
        this.servo.setPosition(minServo);
    }

    public void update(double ratio, boolean buttonPressed) {
        servo.setPosition(minServo + (maxServo - minServo) * ratio + (buttonPressed ? buttonAffect : 0));
    }
}
