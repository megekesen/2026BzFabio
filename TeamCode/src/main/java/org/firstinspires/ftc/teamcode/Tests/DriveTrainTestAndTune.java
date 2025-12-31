package org.firstinspires.ftc.teamcode.Tests;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.PinPoint;

@TeleOp(name = "Limelight Test", group = "Tests")
public class DriveTrainTestAndTune extends OpMode {

    DriveTrain drive;
    PinPoint pin;
    GamepadEx pad1;
    ToggleButtonReader fineToggle;
    public static double heading;
    @Override
    public void init() {
        drive = new DriveTrain(hardwareMap);
        pad1= new GamepadEx(gamepad1);
        fineToggle = new ToggleButtonReader(pad1, GamepadKeys.Button.LEFT_STICK_BUTTON);

        pin = new PinPoint(hardwareMap);
    }

    @Override
    public void loop() {
        fineToggle.readValue();
        drive.DriveCentric(pad1.getLeftX(), pad1.getLeftY(), pad1.getRightX(), pin.getYaw(), fineToggle.getState());
        drive.trainSetHEading(heading);

        telemetry.addData("Current Heading" , pin.getYaw());
        telemetry.addData("Target Heading ", heading);

    }
}
