package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Messanger;

@TeleOp(name = "Messanger Test", group = "Test")
public class TeleOpMessangerTest extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("ALliance color ", Messanger.allianceColor);
        telemetry.addData("Sequence", Messanger.sequence);
        telemetry.update();
    }
}
