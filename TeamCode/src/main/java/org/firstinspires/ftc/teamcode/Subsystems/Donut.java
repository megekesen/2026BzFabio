package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Donut {
    private Servo spindexServo;
    private double spindexIntake1;
    private double spindexIntake2;
    private double spindexIntake3;

    private double spindexShoter1;
    private double spindexShoter2;
    private double spindexShoter3;

    private Servo pushUpServo;
    private double downPosition = 0.0;
    private double upPosition = 0.0;

    private ColorSensor colorLeft;
    private ColorSensor colorRight;

    public Donut (@NonNull HardwareMap hwMap){
        spindexServo = hwMap.get(Servo.class, "spindexServo");
        pushUpServo = hwMap.get(Servo.class, "pushUpServo");

        colorLeft = hwMap.get(ColorSensor.class, "colorLeft");
        colorRight = hwMap.get(ColorSensor.class, "colorRight");

        //ihEIFZAf
    }

}
