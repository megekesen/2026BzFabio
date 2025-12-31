package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.PinPoint;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

@TeleOp(name = "Turret Test and Tune", group = "Test")

public class TurretTestAndTune extends OpMode {
    Turret turret;
    Limelight lime;
    PinPoint pin;

    public static int turretTarget;

    public static int shooterSpeedTarget;

    public static double hoodPosition;

    public static boolean useLimeLIght = false;


    @Override
    public void init() {
        turret = new Turret(hardwareMap);
        lime = new Limelight(hardwareMap);
        pin = new PinPoint(hardwareMap);
    }

    @Override
    public void loop() {
        turret.setShooterTarget(shooterSpeedTarget);
        turret.setHood(hoodPosition);
        turret.setTurretTarget(turretTarget);

        if (useLimeLIght){
            lime.updateAimLL(pin.getYaw());
            turret.setTurretWithLimelight(lime.getDistance(), lime.getTx());
        }

        turret.update();
    }
}
