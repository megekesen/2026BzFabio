package org.firstinspires.ftc.teamcode.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.PinPoint;
@Configurable
@TeleOp(name = "Limelight Test", group = "Tests")
public class LimelightTest extends OpMode {
    Limelight ll;
    String green ="\uD83D\uDFE9";
    String purple = "\uD83D\uDFEA";

    public static Limelight.Pipelines pipeline;
    @Override
    public void init() {
        ll = new Limelight(hardwareMap);

    }
    @Override
    public void start(){
        ll.start();
        ll.switchPipeline(pipeline);
    }

    @Override

    public void loop() {
        ll.updateAimLL();
        double distance = ll.getDistance();
        int iDNumber = ll.updatePatternLL();
        String pattern = "";
        if (iDNumber == 21){
            pattern = green + purple + purple;
        } else if (iDNumber == 22) {
            pattern = purple + green + purple;
        } else if (iDNumber == 24) {
            pattern = purple + purple + green;
        }

        telemetry.addData("Distance " , distance);
        telemetry.addData("ID NUmber ", iDNumber);
        telemetry.addData("Pattern ", pattern);
    }

    @Override
    public void stop(){
        ll.stop();
    }
}
