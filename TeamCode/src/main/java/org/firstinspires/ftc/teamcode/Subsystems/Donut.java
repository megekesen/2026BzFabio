package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;


public class Donut {
    private Servo spindexServo;

    public enum SpindexPositions{INTAKE_1, INTAKE_2, INTAKE_3, SHOTER_1, SHOTER_2, SHOTER_3}

    private SpindexPositions currentPosition;
    private double spindexIntake1 = 0.0;
    private double spindexIntake2 = 0.0;
    private double spindexIntake3 = 0.0;

    private double spindexShoter1 = 0.0;
    private double spindexShoter2 = 0.0;
    private double spindexShoter3 = 0.0;

    private Servo pushUpServo;
    public enum PushUpPositions{UP, DOWN}
    private double pushUpDown = 0.0;
    private double pushUpUp = 0.0;

    private NormalizedColorSensor colorSensorLeft;
    private NormalizedColorSensor colorSensorRight;
    public enum BallColor {ORANGE, PURPLE, CONFLICTING}


    public int lowerPurpleLeve = 0;
    public int upperPurpleLevel = 0;
    public int lowerOrangeLevel = 0;
    public int upperOrangeLevel = 0;

    public Donut (@NonNull HardwareMap hwMap){
        spindexServo = hwMap.get(Servo.class, "spindexServo");
        pushUpServo = hwMap.get(Servo.class, "pushUpServo");

        colorSensorLeft = hwMap.get(NormalizedColorSensor.class, "colorLeft");
        colorSensorRight = hwMap.get(NormalizedColorSensor.class, "colorRight");
    }
    public void setSpindexPosition( SpindexPositions pos){
        switch(pos){
            case INTAKE_1 :
                spindexServo.setPosition(spindexIntake1);
                currentPosition = SpindexPositions.INTAKE_1;
                break;
            case INTAKE_2:
                spindexServo.setPosition(spindexIntake2);
                currentPosition = SpindexPositions.INTAKE_2;
                break;
            case INTAKE_3:
                spindexServo.setPosition(spindexIntake3);
                currentPosition = SpindexPositions.INTAKE_3;
                break;
            case SHOTER_1:
                spindexServo.setPosition(spindexShoter1);
                currentPosition = SpindexPositions.SHOTER_1;
                break;
            case SHOTER_2:
                spindexServo.setPosition(spindexShoter2);
                currentPosition = SpindexPositions.SHOTER_2;
                break;
            case SHOTER_3:
                spindexServo.setPosition(spindexShoter3);
                currentPosition = SpindexPositions.SHOTER_3;
                break;
        }
    }
    public void setPushUpServoPosition (PushUpPositions pos){
        switch (pos){
            case UP:
                pushUpServo.setPosition(pushUpUp);
                break;
            case DOWN:
                pushUpServo.setPosition(pushUpDown);
                break;
        }

    }
    public BallColor getColor(){
        NormalizedRGBA leftNormalizedColor = colorSensorLeft.getNormalizedColors();
        NormalizedRGBA rightNormalizedColor = colorSensorRight.getNormalizedColors();

        BallColor leftColor;
        BallColor rightColor;

        double leftHue = JavaUtil.colorToHue(leftNormalizedColor.toColor());
        if (leftHue >= lowerPurpleLeve && leftHue <= upperPurpleLevel){
            leftColor = BallColor.PURPLE;
        } else if (leftHue >= lowerOrangeLevel && leftHue <= upperOrangeLevel) {
            leftColor = BallColor.ORANGE;
        } else {
            leftColor = BallColor.CONFLICTING;
        }

        double rightHue = JavaUtil.colorToHue(rightNormalizedColor.toColor());
        if (rightHue >= lowerPurpleLeve && rightHue <= upperPurpleLevel){
            rightColor = BallColor.PURPLE;
        } else if (rightHue >= lowerOrangeLevel && rightHue <= upperOrangeLevel) {
            rightColor = BallColor.ORANGE;
        } else {
            rightColor = BallColor.CONFLICTING;
        }

        if(rightColor == leftColor){
            return rightColor;
        }else if (rightColor == BallColor.CONFLICTING && leftColor != BallColor.CONFLICTING){
            return leftColor;
        }else if (leftColor == BallColor.CONFLICTING && rightColor != BallColor.CONFLICTING){
            return rightColor;
        } else {
            return BallColor.CONFLICTING;
        }

    }


}
