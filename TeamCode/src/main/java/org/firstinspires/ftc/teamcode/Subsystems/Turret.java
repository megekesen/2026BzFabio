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
import com.qualcomm.robotcore.util.Range;

@Configurable
public class Turret {
    public DcMotorEx shooterMotorRight;
    private DcMotorEx shooterMotorLeft;


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

    private double LLCurentPosition;
    private double distance;

    private PIDController turretController;
    public static double turretP;
    public static double turretI;
    public static double turretD;

    public double powServo;

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
    /**
     * Computes corrected turret aiming error when the Limelight
     * is offset sideways from the turret center.
     *
     * @param txDeg          Limelight horizontal angle (degrees)
     * @param distance       Distance from camera to target (same units as offset)
     * @param cameraOffsetX Side offset of camera from turret center (+right, -left)
     *
     * @return Corrected turret error angle in degrees (target-centered = 0)
     */
    public static double getCorrectedTurretError(
            double txDeg,
            double distance,
            double cameraOffsetX
    ) {
        // Protect against divide-by-zero or invalid distance
        if (distance <= 0.001) {
            return txDeg;
        }

        // Convert Limelight tx to radians
        double txRad = Math.toRadians(txDeg);

        // Target position in camera frame
        double xCam = distance * Math.tan(txRad);
        double yCam = distance;

        // Shift into turret frame
        double xTurret = xCam + cameraOffsetX;
        double yTurret = yCam;

        // Compute corrected angle
        double correctedRad = Math.atan2(xTurret, yTurret);

        return Math.toDegrees(correctedRad);
    }

    private void turretLLPID(){
        turretLLController.setPID(turretLLP, turretLLI, turretLLD);
        powServo = 0;
        powServo =turretLLController.calculate(getCorrectedTurretError(-LLCurentPosition, distance, 0.14 ), 0);

        powServo = turretServo.getPosition() + powServo;
        turretServo.setPosition(Range.clip(powServo, 0.0, 1.0));
    }
    public void setTurretServoPosition(double pos){
        turretServo.setPosition(pos);
    }


    public void setHood (double position){
        hoodServo.setPosition(position);
    }

    public void setLLCurrentPosition(double tx, double distance){
        LLCurentPosition = tx;
        this.distance = distance;
    }
    public void setTurretWithLimelight(double distance, double tx){
        //here do a bunch of math to figure our the roller speed and hood extension
        //if this does not work, picka spot of the field, find the optimal hood position
        // and roller speed, keep the turret from spinning
        // and just use that always

        setLLCurrentPosition(tx, distance);
        if (distance > 100){
            setShooterSpeed(1600);
            setHood(0);
        }else{
            setShooterSpeed( 1400);
            setHood(0);
        }
        speedRPMTarget = 1600;
    }

    public void updateLL(){
        shooterPID();
        turretLLPID();
    }
    public void update(){
        shooterPID();
    }


}
