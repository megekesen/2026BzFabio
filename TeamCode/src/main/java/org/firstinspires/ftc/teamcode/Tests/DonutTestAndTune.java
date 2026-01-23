package org.firstinspires.ftc.teamcode.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
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
    public static Donut.PushUpPositions pushup = Donut.PushUpPositions.DOWN;
    private TelemetryManager telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

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
        telemetryM.debug("color sensor left " + donut.getHueFromSensor(donut.colorSensorLeft));
        telemetryM.debug("color sensor right "+ donut.getHueFromSensor(donut.colorSensorLeft));
        telemetryM.debug("front Distance Sensor "+ donut.getFrontDistance());
        telemetryM.debug("back Distance Sensor "+ donut.getBackDistance());
        telemetryM.debug("SPindex target "+ donut.spindexTarget);
        telemetryM.debug("Spindex current "+ donut.spindexMotor.getCurrentPosition());
        telemetryM.debug("COLOR SEEN" + donut.getColor());
        telemetryM.debug();

        if(setRollers){
            rollers.enableIntake();
        } else if (!setRollers) {
            rollers.disableIntake();

        }

        telemetryM.update(telemetry);
        donut.update();

    }
}
