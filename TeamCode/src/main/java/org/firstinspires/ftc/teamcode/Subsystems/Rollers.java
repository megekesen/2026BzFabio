package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Configurable
public class Rollers {
    private CRServo intakeMotor;

    public Rollers(@NonNull HardwareMap hwMap){
        intakeMotor = hwMap.get(CRServo.class, "intakeServo");
    }

    public void enableIntake(){
        intakeMotor.setPower(1);
    }

    public void disableIntake(){
        intakeMotor.setPower(0.0);
    }
}