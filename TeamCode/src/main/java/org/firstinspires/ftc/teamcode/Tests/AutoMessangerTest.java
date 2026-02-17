package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystems.Messenger;

@Autonomous(name = "Messanger test", group = "Tests")

public class AutoMessangerTest extends OpMode{

    @Override
    public void init() {
    }

    @Override
    public void loop() {
        Messenger.allianceColor = "saved from autonomous";
        Messenger.sequence = "saved from autonomous";

    }
}
