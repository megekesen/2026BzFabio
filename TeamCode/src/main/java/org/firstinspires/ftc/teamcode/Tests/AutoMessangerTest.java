package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Messanger;

@Autonomous(name = "Messanger test", group = "Tests")

public class AutoMessangerTest extends OpMode{

    @Override
    public void init() {
    }

    @Override
    public void loop() {
        Messanger.allianceColor = "saved from autonomous";
        Messanger.sequence = "saved from autonomous";

    }
}
