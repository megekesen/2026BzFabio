package org.firstinspires.ftc.teamcode.Teleop2026;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystems.PinPoint;

public class ResetPinpoint extends OpMode {
    PinPoint pin;
    TelemetryManager telemetryM;
    @Override
    public void init() {
       pin = new PinPoint(hardwareMap);
    }
    @Override
    public void init_loop(){
        telemetryM.debug("If you press start, pinpoint will reset. set the robot in desired starting heading before pressing start");
        telemetryM.debug("Robot Yaw " + pin.getYaw() );
        telemetryM.update(telemetry);
    }

    @Override
    public void start(){
        pin.resetPinPoint();
    }
    @Override
    public void loop() {
        telemetryM.debug("Robot Yaw " + pin.getYaw() );
        telemetryM.update(telemetry);
    }
}
