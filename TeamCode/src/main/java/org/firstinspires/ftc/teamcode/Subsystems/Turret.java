package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Turret {
    public DcMotorEx shooterMotorRight;
    private DcMotorEx shooterMotorLeft;


    private DcMotorEx turretMotor;
    private Servo turretServo;

    private Servo hoodServo;



    private PIDFController speedShooterController;
    public static double shoterP = 0.0005;
    public static double shooterI;
    public static double shooterD;
    public static double shooterF = 0.00057;

    int speedRPMTarget;



    private PIDController turretLLController;

    public static double turretLLP;
    public static double turretLLI;
    public static double turretLLD;

    private double turretLLTarget;

    private PIDController turretController;
    public static double turretP;
    public static double turretI;
    public static double turretD;

    private double turretTarget;

    public Turret(HardwareMap hwMap){
        shooterMotorRight = hwMap.get(DcMotorEx.class, "shooterMotorRight");
        shooterMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMotorLeft = hwMap.get(DcMotorEx.class, "shooterMotorLeft");
        shooterMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        turretServo = hwMap.get(Servo.class, "turretServo");
        hoodServo = hwMap.get(Servo.class, "hoodServo");
        speedShooterController = new PIDFController(shoterP, shooterI, shooterD, shooterF);
        turretLLController = new PIDController(turretLLP, turretLLI, turretLLD);
        turretController = new PIDController(turretP, turretI, turretD);
    }

    private void shooterPID(){
        speedShooterController.setPIDF(shoterP, shooterI, shooterD, shooterF);
        double pow = 0;
        double current_pos = shooterMotorLeft.getVelocity(); //in ticks per second
        pow = speedShooterController.calculate(current_pos, speedRPMTarget);

        shooterMotorLeft.setPower(pow);
        shooterMotorRight.setPower(pow);
    }

    public void setShooterSpeed(int target){
        speedRPMTarget = target;
    }

    private void turretLLPID(){
//        double pow = 0;
//        double current_pos = turretMotor.getCurrentPosition(); //in ticks per second
//        pow = turretLLController.calculate(current_pos, turretLLTarget);
//
//        turretMotor.setPower(pow);
    }

    public void setTurretLLTarget(double target){
        turretLLTarget = target;
    }

    private void turretPID(){
//        double pow = 0;
//        double current_pos = turretMotor.getCurrentPosition(); //in ticks per second
//        pow = turretController.calculate(current_pos, turretTarget);
//
//        turretMotor.setPower(pow);
        turretServo.setPosition(turretTarget);
        }

    public void setTurretTarget(double target){
        turretTarget = target;
    }

    public void setHood (double position){
        hoodServo.setPosition(position);
    }

    public void setTurretWithLimelight(double distance, double tx){
        //here do a bunch of math to figure our the roller speed and hood extension
        //if this does not work, picka spot of the field, find the optimal hood position
        // and roller speed, keep the turret from spinning
        // and just use that always

        //turretLLTarget = tx;
        turretTarget = 0.5;
        speedRPMTarget = 1600;
    }
    public boolean isReadyToShoot(){
        return speedShooterController.atSetPoint();
    }

    public void updateLL(){
        shooterPID();
        turretLLPID();
    }
    public void update(){
        shooterPID();
        turretPID();
    }


}
