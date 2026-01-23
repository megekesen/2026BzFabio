package org.firstinspires.ftc.teamcode.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Donut;
import org.firstinspires.ftc.teamcode.Subsystems.Rollers;

@Configurable
@TeleOp(name = "Donut Test and Tune", group = "Test")
public class DonutTestAndTune extends OpMode {
    Donut donut;
    Rollers rollers;
    public static Donut.SpindexPositions position = Donut.SpindexPositions.INTAKE_1;
    public static Donut.PushUpPositions pushup = Donut.PushUpPositions.UP;
    public static boolean setRollers = false;
    @Override
    public void init() {
        donut = new Donut(hardwareMap);
        rollers = new Rollers(hardwareMap);
    }

    @Override
    public void loop() {
        donut.setSpindexPosition(position);
        donut.setPushUpServoPosition(pushup);
        telemetry.addData("color sensor left", donut.getHueFromSensor(donut.colorSensorLeft));
        telemetry.addData("color sensor right", donut.getHueFromSensor(donut.colorSensorLeft));
        telemetry.addData("front Distance Sensor", donut.getFrontDistance());
        telemetry.addData("back Distance Sensor", donut.getBackDistance());

        if(setRollers){
            rollers.enableIntake();
        } else if (!setRollers) {
            rollers.disableIntake();

        }

        telemetry.update();
    }
}
