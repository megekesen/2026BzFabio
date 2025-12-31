package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Turret {
    DcMotorEx shooterMotor;
    DcMotorEx turretMotor;
    Servo hoodServo;



    PIDController speedShooterController;
    public static double shoterP;
    public static double shooterI;
    public static double shooterD;

    int speedRPMTarget;



    PIDController turretController;
    public static double turretP;
    public static double turretI;
    public static double turretD;

    int turretTarget;

    public Turret(HardwareMap hwMap){
        shooterMotor = hwMap.get(DcMotorEx.class, "shooterMotor");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        turretMotor = hwMap.get(DcMotorEx.class, "turretMotor");
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        hoodServo = hwMap.get(Servo.class, "hoodServo");
        speedShooterController = new PIDController(shoterP, shooterI, shooterD);
        turretController = new PIDController(turretP, turretI, turretD);
    }

    private void shooterPID(){
        double pow = 0;
        double current_pos = shooterMotor.getVelocity(); //in ticks per second
        pow = speedShooterController.calculate(current_pos, speedRPMTarget);

        shooterMotor.setPower(pow);
    }

    public void setShooterTarget (int target){
        speedRPMTarget = target;
    }

    private void turretPID(){
        double pow = 0;
        double current_pos = turretMotor.getVelocity(); //in ticks per second
        pow = speedShooterController.calculate(current_pos, turretTarget);

        turretMotor.setPower(pow);
    }

    public void setTurretTarget (int target){
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
    }
    public boolean isReadyToShoot(){
        return speedShooterController.atSetPoint();
    }

    public void update(){
        shooterPID();
        turretPID();
    }


}
