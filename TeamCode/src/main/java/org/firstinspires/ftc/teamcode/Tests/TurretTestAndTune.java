package org.firstinspires.ftc.teamcode.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Donut;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.PinPoint;
import org.firstinspires.ftc.teamcode.Subsystems.Rollers;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
@Configurable
@TeleOp(name = "Turret Test and Tune", group = "Test")

public class TurretTestAndTune extends OpMode {
    Turret turret;
    Limelight lime;
    PinPoint pin;
    Donut donut;
    Rollers rollers;

    public static double turretTarget = 0.5;

    public static double shooterSpeedTarget;

    public static double hoodPosition = 0.575;

    public static boolean useLimeLIght = false;
    private TelemetryManager telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

    public static Donut.SpindexPositions position = Donut.SpindexPositions.INTAKE_1;
    public static Donut.PushUpPositions pushup = Donut.PushUpPositions.DOWN;


    public static boolean setRollers = false;
    @Override
    public void init() {
        turret = new Turret(hardwareMap);
        lime = new Limelight(hardwareMap);
        pin = new PinPoint(hardwareMap);
        donut = new Donut(hardwareMap);
        rollers = new Rollers(hardwareMap);
        lime.switchPipeline(Limelight.Pipelines.BLUE_TARGET);
        lime.start();
    }

    @Override
    public void loop() {
        turret.setShooterSpeed((int) shooterSpeedTarget);
        turret.setHood(hoodPosition);
        turret.setLLCurrentPosition(lime.getTx(), lime.getDistance());

        donut.setSpindexPosition(position);
        donut.setPushUpServoPosition(pushup);



        if(setRollers){
            rollers.enableIntake();
        } else {
            rollers.disableIntake();
        }

        telemetryM.update(telemetry);
        donut.update();

        if (useLimeLIght){
            turret.setLLCurrentPosition(lime.getTx(), lime.getDistance());
        }


        telemetryM.debug(" Current sped " + turret.shooterMotorRight.getVelocity());
        telemetryM.debug("Target speed "+ shooterSpeedTarget);
        telemetryM.debug("Current error "+ (shooterSpeedTarget - turret.shooterMotorRight.getVelocity()));
        telemetryM.debug("TX "+ lime.getTx());
        telemetryM.debug("Servo Power" + turret.powServo);

        if (useLimeLIght){
            turret.updateLL();
        }else {
            turret.update();
        }
        turret.updateLL();
        telemetryM.update(telemetry);
    }
}
