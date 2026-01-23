package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.controller.PIDController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Configurable
public class Donut {
    public DcMotorEx spindexMotor;
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
    public static int spindexIntake1 = 0;
    public static int spindexIntake2 = 100;
    public static int spindexIntake3 = 195;

    public static int spindexShoter1 = 140;
    //could be used to speed up spindex movement so that the spindex does not have to go all the way around when going from 1 to 3
    // if you do not want to use it look at the commit from the 31 at 4 am(?) and use that instead
    public static int spindexShoter1From3 = 435;
    public static int spindexShoter2 = 240;
    public static int spindexShoter3 = 335;
    public static int spindexShoter3From1 = 48;

    private PIDController spindexController;
    public static double spindexP = 0.04;
    public static double spindexI = 0.0005;
    public static double spindexD;

    public static int spindexTarget;

    private Servo pushUpServo;
    public enum PushUpPositions{UP, DOWN}
    public static double pushUpDown = 0.605;
    public static double pushUpUp = 0.66;

    public DistanceSensor backDistanceSensor;
    public  NormalizedColorSensor colorSensorLeft;
    public NormalizedColorSensor colorSensorRight;
    public enum BallColor {GREEN, PURPLE, CONFLICTING, EMPTY}

    // all of these are hue values
    public double lowerPurpleLeve = 210;
    public double upperPurpleLevel = 260;
    public double lowerGreenLevel = 140;
    public double upperGreenLevel = 170;


    public Donut (@NonNull HardwareMap hwMap){
        spindexMotor = hwMap.get(DcMotorEx.class, "spindexMotor");
        spindexMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spindexMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        spindexMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        pushUpServo = hwMap.get(Servo.class, "pushUpServo");

        colorSensorLeft = hwMap.get(NormalizedColorSensor.class, "colorRight");
        colorSensorRight = hwMap.get(NormalizedColorSensor.class, "colorLeft");
        backDistanceSensor = hwMap.get(DistanceSensor.class, "backDistanceSensor");

        spindexController = new PIDController(spindexP, spindexI, spindexD);
    }
    public void setSpindexPosition( SpindexPositions pos){
        switch(pos){
            case INTAKE_1 :
                spindexTarget = spindexIntake1;
                currentPosition = SpindexPositions.INTAKE_1;
                break;
            case INTAKE_2:
                spindexTarget = spindexIntake2;
                currentPosition = SpindexPositions.INTAKE_2;
                break;
            case INTAKE_3:
                spindexTarget = spindexIntake3;
                currentPosition = SpindexPositions.INTAKE_3;
                break;
            case SHOTER_1:
                if(currentPosition == SpindexPositions.SHOTER_3){
                    spindexTarget = spindexShoter1From3;
                    currentPosition = SpindexPositions.SHOOTER_1_FROM_3;
                } else{
                    spindexTarget = spindexShoter1;
                    currentPosition = SpindexPositions.SHOTER_1;
                }
                break;
            case SHOTER_2:
                spindexTarget = spindexShoter2;
                currentPosition = SpindexPositions.SHOTER_2;
                break;
            case SHOTER_3:
                if(currentPosition == SpindexPositions.SHOTER_1){
                    spindexTarget = spindexShoter3From1;
                    currentPosition = SpindexPositions.SHOTER_3_FROM_1;
                } else{
                    spindexTarget = spindexShoter3;
                    currentPosition = SpindexPositions.SHOTER_3;
                }
                break;
            case SHOTER_3_FROM_1:
                spindexTarget = spindexShoter3From1;
                currentPosition = SpindexPositions.SHOTER_3_FROM_1;
                break;
            case SHOOTER_1_FROM_3:
                spindexTarget = spindexShoter1From3;
                currentPosition = SpindexPositions.SHOOTER_1_FROM_3;
                break;

        }
    }

    public void donutPID (){
        spindexController.setPID(spindexP, spindexI, spindexD);
        double pow = 0;
        double current_pos = spindexMotor.getCurrentPosition(); //in ticks per second
        pow = spindexController.calculate(current_pos, spindexTarget);

        spindexMotor.setPower(pow);
    }

    public void update(){
        donutPID();
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
