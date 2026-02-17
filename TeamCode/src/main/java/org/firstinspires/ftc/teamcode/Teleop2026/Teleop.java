package org.firstinspires.ftc.teamcode.Teleop2026;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Donut;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Messenger;
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

    public enum States {PREINTAKE, INTAKE,PRESHOOTING, SHOOTING, SIDE, NOTHING}
    public States currentState = States.NOTHING;
    private TelemetryManager telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();


    @Override
    public void init() {
        supersystems = new Supersystems(hardwareMap, false);
        pad1 = new GamepadEx(gamepad1);
        fineToggle = new ToggleButtonReader(pad1, GamepadKeys.Button.LEFT_STICK_BUTTON);
        timer = new ElapsedTime();
        if (Messenger.allianceColor.equals("Blue")) {
            supersystems.ll.switchPipeline(Limelight.Pipelines.BLUE_TARGET);
        } else if (Messenger.allianceColor.equals("red")) {
            supersystems.ll.switchPipeline(Limelight.Pipelines.RED_TARGET);
        }

        if(gamepad1.dpadDownWasReleased()){
            currentState = States.PREINTAKE;
        }

        if(gamepad1.dpadUpWasReleased()){
            currentState = States.PRESHOOTING;
        }

        if(gamepad1.dpadRightWasPressed()){
            currentState = States.SIDE;
        }

        if(gamepad1.dpadLeftWasReleased()){
            currentState = States.NOTHING;
        }
        supersystems.donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
    }

    @Override
    public void loop() {
        fineToggle.readValue();
        pad1.readButtons();

        if(gamepad1.dpadDownWasReleased()){
            currentState = States.PREINTAKE;
        }

        if(gamepad1.dpadUpWasReleased()){
            currentState = States.PRESHOOTING;
        }

        if(gamepad1.dpadRightWasPressed()){
            currentState = States.SIDE;
        }

        if(gamepad1.dpadLeftWasReleased()){
            currentState = States.NOTHING;
        }

        supersystems.train.DriveCentric(pad1.getLeftX(), pad1.getLeftY(), pad1.getRightX(), supersystems.pin.getYaw(), fineToggle.getState());




        switch (currentState){
            case PREINTAKE:
                supersystems.setRollers(true);
                supersystems.resetIntake();
                currentState = States.INTAKE;
                break;
            case INTAKE:
                supersystems.setRollers(true);
                supersystems.intakeWithDistance(timer);
                break;
            case PRESHOOTING:
                supersystems.rollers.intakeMotor.setPower(0.5);
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.LL);
                supersystems.ll.start();
                supersystems.train.trainSetHEading(scoreHeading);
                supersystems.resetScore();
                supersystems.updateLLForSHooting();
                supersystems.aimTurretWithLL();
               if(gamepad1.aWasReleased()){
                   currentState = States.SHOOTING;
                            if(supersystems.ll.llCanAim())
                                currentState = States.SHOOTING;
                        }
                break;
            case SHOOTING:
                supersystems. score(timer);
                supersystems.updateTurretPIDs();
                supersystems.updateLLForSHooting();
                break;
            case SIDE:
                supersystems.train.trainSetHEading(sideHeading);
                supersystems.intakeFromShooter(gamepad1.aWasReleased(), timer);
                break;

            case NOTHING:
                break;
        }

        if (currentState == States.NOTHING){
            supersystems.setRollers(false);
        }
        if (currentState != States.PRESHOOTING && currentState != States.SHOOTING && currentState!= States.SIDE){
            supersystems.ll.stop();
            supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.STOP);
            supersystems.donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
        }



        telemetryM.debug("case  " + currentState);
        telemetryM.debug("intake state " + supersystems.intakeState);
        telemetryM.debug("shooting state " + supersystems.scorestate);
        telemetryM.debug(("distance in the front " + supersystems.donut.getFrontDistance()));
        telemetryM.debug(("distance in the back " + supersystems.donut.getBackDistance()));

        telemetryM.debug("Donuts slots " + supersystems.peekAtSpindex());
        telemetryM.debug("Turn to next " + supersystems.switchToNext);

        telemetryM.update(telemetry);


        supersystems.update();

    }
}
