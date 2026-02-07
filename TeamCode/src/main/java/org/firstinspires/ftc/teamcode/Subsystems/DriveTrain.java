package org.firstinspires.ftc.teamcode.Subsystems;


import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Configurable
public class DriveTrain {
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightBack;

    private static final double TWO_PI = 2 * Math.PI;
    private static double rot_target = 0;
    private static double rotationalPower = 0;
    private boolean run_rotational = true;
    private double old_rot_target = 0;

    private static PIDFController rotational_controller;
    public static double rot_kP = 0.7;
    public static double rot_kI = 0.0;
    public static double rot_kD = 0.0;

    //change these for presets
    public static double goalHeading = 5.5;
    public static double humanHEading= 1.48;


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
    }



    private void Drive(double lStickX, double lStickY, double rStickX, double heading){

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double y = lStickY; // Remember, this is reversed!
        double x = lStickX; // this is strafing
        double rx = rStickX;
        update_run_rotational();
        if (run_rotational){
            rotationalPower = Rotational_PID(heading);
        }

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = ((y + x + rx) / denominator) - rotationalPower;
        double leftRearPower = ((y - x + rx) / denominator) - rotationalPower;
        double rightFrontPower = ((y - x - rx) / denominator) + rotationalPower;
        double rightRearPower = ((y + x - rx) / denominator) + rotationalPower;

        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftBack.setPower(leftRearPower);
        rightBack.setPower(rightRearPower);
    }
    public void DriveCentric(double lStickX, double lStickY, double rStickX, double robotAngle, boolean fineControls){
        double  fine = 1;
        if (fineControls){
            fine = 0.5;
        }
        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double y = lStickY * fine; // Remember, this is reversed!
        double x = lStickX * fine; // this is strafing
        double rx = rStickX * fine;


        double theta = Math.atan2(y, x);
        double r = Math.hypot(y, x);

        theta = AngleUnit.normalizeRadians(theta - robotAngle);

        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        Drive(newRight, newForward, rx, robotAngle);

    }

    private double Rotational_PID( double currentHeading ){
        double pow = 0;
        pow = rotational_controller.calculate(currentHeading, currentHeading + smallestAngleDifference(currentHeading));

        if (rotational_controller.atSetPoint()){
            pow = 0;
            run_rotational = false;
        };

        return pow;
    }
    private void update_run_rotational(){
        if (old_rot_target != rot_target){
            old_rot_target = rot_target;
            run_rotational = true;
        }
    }

    private  double normalizeToTwoPi(double theta) {

        double normalized = theta % TWO_PI;
        if (normalized < 0) {
            normalized += TWO_PI;
        }

        return normalized;
    }
    private  double smallestAngleDifference(double heading) {
        // Normalize both angles to [0, 2π)
        double current = normalizeToTwoPi(heading);

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
    public void train_set_heading_goal(){
        rot_target = goalHeading;
    }
    public void train_set_heading_human(){
        rot_target = humanHEading;
    }
    public void trainSetHEading(double heading){
        rot_target = heading;
    }



}
