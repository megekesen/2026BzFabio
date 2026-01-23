package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Configurable
public class Donut {
    private Servo spindexServo;
    public enum SpindexPositions{
        INTAKE_1, INTAKE_2, INTAKE_3, SHOTER_1, SHOTER_2, SHOTER_3, SHOOTER_1_FROM_3, SHOTER_3_FROM_1;
        private static final SpindexPositions[] VALUES = values();

        public SpindexPositions next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public SpindexPositions previous() {
            return VALUES[(ordinal() - 1 + VALUES.length) % VALUES.length];
        }
    }

    private SpindexPositions currentPosition;
    private double spindexIntake1 = 0.0;
    private double spindexIntake2 = 0.0;
    private double spindexIntake3 = 0.0;

    private double spindexShoter1 = 0.0;
    //could be used to speed up spindex movement so that the spindex does not have to go all the way around when going from 1 to 3
    // if you do not want to use it look at the commit from the 31 at 4 am(?) and use that instead
    private double spindexShoter1From3 = 0.0;
    private double spindexShoter2 = 0.0;
    private double spindexShoter3 = 0.0;
    private double spindexShoter3From1 = 0.0;

    private Servo pushUpServo;
    public enum PushUpPositions{UP, DOWN}
    private double pushUpDown = 0.0;
    private double pushUpUp = 0.0;

    public DistanceSensor backDistanceSensor;
    public  NormalizedColorSensor colorSensorLeft;
    public NormalizedColorSensor colorSensorRight;
    public enum BallColor {GREEN, PURPLE, CONFLICTING, EMPTY}

    // all of these are hue values
    public int lowerPurpleLeve = 0;
    public int upperPurpleLevel = 0;
    public int lowerGreenLevel = 0;
    public int upperGreenLevel = 0;


    public Donut (@NonNull HardwareMap hwMap){
        spindexServo = hwMap.get(Servo.class, "spindexServo");
        pushUpServo = hwMap.get(Servo.class, "pushUpServo");

        colorSensorLeft = hwMap.get(NormalizedColorSensor.class, "colorLeft");
        colorSensorRight = hwMap.get(NormalizedColorSensor.class, "colorRight");
        backDistanceSensor = hwMap.get(DistanceSensor.class, "backDistanceSensor");
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
                if(currentPosition == SpindexPositions.SHOTER_3){
                    spindexServo.setPosition(spindexShoter1From3);
                    currentPosition = SpindexPositions.SHOOTER_1_FROM_3;
                } else{
                    spindexServo.setPosition(spindexShoter1);
                    currentPosition = SpindexPositions.SHOTER_1;
                }
                break;
            case SHOTER_2:
                spindexServo.setPosition(spindexShoter2);
                currentPosition = SpindexPositions.SHOTER_2;
                break;
            case SHOTER_3:
                if(currentPosition == SpindexPositions.SHOTER_1){
                    spindexServo.setPosition(spindexShoter3From1);
                    currentPosition = SpindexPositions.SHOTER_3_FROM_1;
                } else{
                    spindexServo.setPosition(spindexShoter3);
                    currentPosition = SpindexPositions.SHOTER_3;
                }
                break;
        }
    }
    public void spinSpindex (int direction){
        if (direction == 1){
            setSpindexPosition( currentPosition.next());
        } else if (direction == -1) {
            setSpindexPosition(currentPosition.previous());
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

    public double getHueFromSensor (NormalizedColorSensor n){
        NormalizedRGBA rgba = n.getNormalizedColors();
        return JavaUtil.colorToHue(rgba.toColor());
    }
    public BallColor getColor(){
        NormalizedRGBA leftNormalizedColor = colorSensorLeft.getNormalizedColors();
        NormalizedRGBA rightNormalizedColor = colorSensorRight.getNormalizedColors();

        BallColor leftColor;
        BallColor rightColor;

        double leftHue = JavaUtil.colorToHue(leftNormalizedColor.toColor());
        if (leftHue >= lowerPurpleLeve && leftHue <= upperPurpleLevel){
            leftColor = BallColor.PURPLE;
        } else if (leftHue >= lowerGreenLevel && leftHue <= upperGreenLevel) {
            leftColor = BallColor.GREEN;
        } else {
            leftColor = BallColor.CONFLICTING;
        }

        double rightHue = JavaUtil.colorToHue(rightNormalizedColor.toColor());
        if (rightHue >= lowerPurpleLeve && rightHue <= upperPurpleLevel){
            rightColor = BallColor.PURPLE;
        } else if (rightHue >= lowerGreenLevel && rightHue <= upperGreenLevel) {
            rightColor = BallColor.GREEN;
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

    public double getFrontDistance(){
        DistanceSensor distance = (DistanceSensor) colorSensorRight;
        return distance.getDistance(DistanceUnit.CM);
    }

    public double getBackDistance(){
        return backDistanceSensor.getDistance(DistanceUnit.CM);
    }


}
