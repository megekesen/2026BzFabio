package org.firstinspires.ftc.teamcode.Subsystems;


import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;


public class DriveTrain {
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightBack;
    private GoBildaPinpointDriver pinpoint;
    private double angle;
    private boolean train_pos;
    private boolean train_on;
    public double fine_move = 1;
    private static double lStickX = 0.0;
    private static double lStickY = 0.0;
    private static double rStickX = 0.0;
    private static double rStickY = 0.0;
    private static final double TWO_PI = 2 * Math.PI;
    public static double rot_target = 0;
    public static double rot = 0;
    public boolean run_rotational = true;
    public double update_str_target = 0;

    private static PIDFController rotational_controller;
    public static double rot_kP = 0.7;
    public static double rot_kI = 0.0;
    public static double rot_kD = 0.0;


    public static double submersibleYaw = - (Math.PI / 2);
    public static double rungsYaw = 0.0;
    public static double basketYaw = Math.toRadians(-45);


    public DriveTrain(@NonNull HardwareMap hwMap){

        this.leftFront = hwMap.get(DcMotorEx.class, "leftFront");
        this.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        this.rightFront = hwMap.get(DcMotorEx.class, "rightFront");
        this.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.leftBack = hwMap.get(DcMotorEx.class, "leftBack");
        this.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        this.rightBack = hwMap.get(DcMotorEx.class, "rightBack");
        this.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        rotational_controller = new PIDFController(rot_kP, rot_kI, rot_kD, 0);

        pinpoint = hwMap.get(GoBildaPinpointDriver.class,"pinpoint");
        pinpoint.recalibrateIMU();
        pinpoint.setOffsets(10,-16, DistanceUnit.CM);
    }


    public void reset_odo(boolean reset){
        if (reset){pinpoint.resetPosAndIMU();}
    }
    public void Drive(double lStickX, double lStickY, double rStickX){

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double y = lStickY; // Remember, this is reversed!
        double x = lStickX; // this is strafing
        double rx = rStickX;
        update_run_rotational();
        rot = 0;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = ((y + x + rx) / denominator) - rot;
        double leftRearPower = ((y - x + rx) / denominator) - rot;
        double rightFrontPower = ((y - x - rx) / denominator) + rot;
        double rightRearPower = ((y + x - rx) / denominator) + rot;

        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftBack.setPower(leftRearPower);
        rightBack.setPower(rightRearPower);
    }


    public void DriveCentric(double lStickX, double lStickY, double rStickX){

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double y = -lStickY; // Remember, this is reversed!
        double x = lStickX; // this is strafing
        double rx = rStickX;
        pinpoint.update(GoBildaPinpointDriver.ReadData.ONLY_UPDATE_HEADING);
        double robotAngle = pinpoint.getHeading(UnnormalizedAngleUnit.RADIANS);

        double theta = Math.atan2(y, x);
        double r = Math.hypot(y, x);

        theta = AngleUnit.normalizeRadians(theta - robotAngle);

        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        Drive(newRight, newForward, rx);

    }
    public void finecontrols(boolean move){
        if(move && !train_pos){
            train_on = !train_on;
            if (train_on) {
                fine_move = 0.5;
            } else {
                fine_move = 1;
            }
        }
        train_pos = move;
    }

    public double Rotational_PID(){
        double pow = 0;
        pinpoint.update(GoBildaPinpointDriver.ReadData.ONLY_UPDATE_HEADING);
        double current_pos = pinpoint.getHeading(UnnormalizedAngleUnit.RADIANS);
        pow = rotational_controller.calculate(current_pos, current_pos + smallestAngleDifference());

        if (rotational_controller.atSetPoint()){
            pow = 0;
            run_rotational = false;
        };
        return pow;
    }
    public void update_run_rotational(){
        if (update_str_target != rot_target){
            update_str_target = rot_target;
            run_rotational = true;
        }
    }

    public  double normalizeToTwoPi(double theta) {

        double normalized = theta % TWO_PI;
        if (normalized < 0) {
            normalized += TWO_PI;
        }

        return normalized;
    }
    public  double smallestAngleDifference() {
        pinpoint.update(GoBildaPinpointDriver.ReadData.ONLY_UPDATE_HEADING);
        // Normalize both angles to [0, 2π)
        double current = normalizeToTwoPi(pinpoint.getHeading(UnnormalizedAngleUnit.RADIANS));

        // Compute the difference and normalize to (-π, π]
        double diff = rot_target - current;
        if (diff > Math.PI) {
            diff -= TWO_PI;
        } else if (diff <= -Math.PI) {
            diff += TWO_PI;
        }
        return diff;

    }
    public void manualMove(double power){
        leftBack.setPower(power);
        leftFront.setPower(power);
        rightBack.setPower(power);
        rightFront.setPower(power);
    }
    public double getYaw(){
        pinpoint.update(GoBildaPinpointDriver.ReadData.ONLY_UPDATE_HEADING);
        return pinpoint.getHeading(UnnormalizedAngleUnit.RADIANS);
    }
    public void train_set_heading_pickup(){
        rot_target = basketYaw;
    }
    public void train_set_heading_human(){
        rot_target = submersibleYaw;
    }



}
