package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Configurable
public class Rollers {
    private DcMotorEx intakeMotor;
    public static double intakePower = 0.5;

    public Rollers(@NonNull HardwareMap hwMap){
        intakeMotor = hwMap.get(DcMotorEx.class, "intakeMotor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void enableIntake(){
        intakeMotor.setPower(intakePower);
    }

    public void disableIntake(){
        intakeMotor.setPower(0.0);
    }
}