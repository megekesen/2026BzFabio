package org.firstinspires.ftc.teamcode.Teleop2026;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Messanger;
import org.firstinspires.ftc.teamcode.Subsystems.Supersystems;

@Configurable
@TeleOp (name = "Teleop")
public class Teleop extends OpMode {
    Supersystems supersystems;
    GamepadEx pad1;
    ToggleButtonReader fineToggle;
    ElapsedTime timer;

    double scoreHeading = 0.0;
    double sideHeading = 0.0;

    public enum States {PREINTAKE, INTAKE,PRESHOOTING, SHOOTING, SIDE}
    public States currentState = States.INTAKE;


    @Override
    public void init() {
        supersystems = new Supersystems(hardwareMap, false);
        pad1 = new GamepadEx(gamepad1);
        fineToggle = new ToggleButtonReader(pad1, GamepadKeys.Button.LEFT_STICK_BUTTON);
        timer = new ElapsedTime();
        if (Messanger.allianceColor.equals("Blue")) {
            supersystems.ll.switchPipeline(Limelight.Pipelines.BLUE_TARGET);
        } else if (Messanger.allianceColor.equals("red")) {
            supersystems.ll.switchPipeline(Limelight.Pipelines.RED_TARGET);
        }
    }

    @Override
    public void loop() {
        supersystems.train.DriveCentric(pad1.getLeftX(), pad1.getLeftY(), pad1.getRightX(), supersystems.pin.getYaw(), fineToggle.getState());

        pad1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(() -> {currentState = States.PREINTAKE;});

        pad1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(() -> {currentState = States.PRESHOOTING;});

        pad1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(() -> {currentState = States.SIDE;});


        switch (currentState){
            case PREINTAKE:
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.ENCODER);
                supersystems.setRollers(true);
                supersystems.resetIntake();
                currentState = States.INTAKE;
                break;
            case INTAKE:
                supersystems.setRollers(true);
                supersystems.intakeWithDistance(timer);
                break;
            case PRESHOOTING:
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.LL);
                supersystems.ll.start();
                supersystems.train.trainSetHEading(scoreHeading);
                supersystems.resetScore();
                supersystems.updateLLForSHooting();
                supersystems.setTurretON();
                supersystems.updateTurretPIDs();
                pad1.getGamepadButton(GamepadKeys.Button.A)
                        .whenPressed(() -> {
                            if(supersystems.ll.llCanAim())
                                currentState = States.SHOOTING;
                        });
                break;
            case SHOOTING:
                supersystems. score(timer);
                supersystems.updateTurretPIDs();
                supersystems.updateLLForSHooting();
                break;
            case SIDE:
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.ENCODER);
                supersystems.train.trainSetHEading(sideHeading);
                supersystems.intakeWithDistanceFromShooter(timer);
                break;
        }

        if (currentState != States.PREINTAKE || currentState != States.INTAKE){
            supersystems.setRollers(false);
        }
        if (currentState != States.PRESHOOTING || currentState != States.SHOOTING){
            supersystems.ll.stop();
        }

        supersystems.updateTurretPIDs();
        fineToggle.readValue();
        pad1.readButtons();
    }
}
