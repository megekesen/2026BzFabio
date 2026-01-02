package org.firstinspires.ftc.teamcode.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Donut;

@Configurable
@TeleOp(name = "Donut Test and Tune", group = "Test")
public class DonutTestAndTune extends OpMode {
    Donut donut;
    Donut.SpindexPositions position = Donut.SpindexPositions.INTAKE_1;
    Donut.PushUpPositions pushup = Donut.PushUpPositions.UP;
    @Override
    public void init() {
        donut = new Donut(hardwareMap);
    }

    @Override
    public void loop() {
        donut.setSpindexPosition(position);
        donut.setPushUpServoPosition(pushup);
    }
}
