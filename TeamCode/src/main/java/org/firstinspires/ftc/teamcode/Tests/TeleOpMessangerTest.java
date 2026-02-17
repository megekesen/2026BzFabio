package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Messenger;

@TeleOp(name = "Messanger Test", group = "Test")
public class TeleOpMessangerTest extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("ALliance color ", Messenger.allianceColor);
        telemetry.addData("Sequence", Messenger.sequence);
        telemetry.update();
    }
}
