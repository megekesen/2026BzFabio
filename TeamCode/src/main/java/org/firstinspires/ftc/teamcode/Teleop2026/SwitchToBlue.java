package org.firstinspires.ftc.teamcode.Teleop2026;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Messenger;

@TeleOp(name = "Switch alliance color")
public class SwitchToBlue extends OpMode {
    private TelemetryManager telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

    @Override
    public void init() {
        telemetryM.debug("If you press start, the alliance color will invert, if it is blue it will become red, and if it is red it will become blue");
    }
    
    @Override
    public void init_loop(){
        telemetryM.debug("If you press start, the alliance color will invert, if it is blue it will become red, and if it is red it will become blue");


        telemetryM.debug("Alliance Color " + Messenger.allianceColor);
        telemetryM.debug("Pattern " + Messenger.sequence);
        telemetryM.update(telemetry);
    }
    @Override
    public void start(){
        if(Messenger.allianceColor == "Red"){
            Messenger.allianceColor = "Blue";
        } else if (Messenger.allianceColor == "Blue") {
            Messenger.allianceColor = "Red";
        }
    }

    @Override
    public void loop() {
        telemetryM.debug("Alliance Color " + Messenger.allianceColor);
        telemetryM.debug("Pattern " + Messenger.sequence);
        telemetryM.update(telemetry);
    }
}
