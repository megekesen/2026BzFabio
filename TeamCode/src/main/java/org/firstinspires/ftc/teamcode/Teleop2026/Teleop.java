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

    public enum States {PREINTAKE, INTAKE,PRESHOOTING, SHOOTING, SIDE, NOTHING, UNJAM}
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
            supersystems.train.goalHeading = 0.0;
            supersystems.train.humanHEading = 0.0 ;
        } else if (Messenger.allianceColor.equals("Red")) {
            supersystems.ll.switchPipeline(Limelight.Pipelines.RED_TARGET);
        }
        supersystems.ll.start();

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
        if(gamepad1.startWasReleased()){
            currentState = States.UNJAM;
            timer.reset();
        }

        supersystems.train.DriveCentric(pad1.getLeftX(), pad1.getLeftY(), pad1.getRightX(), supersystems.pin.getYaw(), fineToggle.getState());




        switch (currentState){
            case PREINTAKE:
                supersystems.turret.setShooterSpeed(0);
                supersystems.donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
                supersystems.setRollers(true);
                supersystems.resetIntake();
                currentState = States.INTAKE;
                break;
            case INTAKE:
                supersystems.donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
                supersystems.setRollers(true);
                supersystems.intakeWithDistance(timer);

                break;
            case PRESHOOTING:
                supersystems.rollers.intakeMotor.setPower(0.5);
                supersystems.train.train_set_heading_goal();
                supersystems.resetScore(timer);
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
                break;
            case SIDE:
                supersystems.turret.setShooterSpeed(-400);
                supersystems.train.train_set_heading_human();
                supersystems.intakeFromShooter(gamepad1.aWasReleased(), timer);
                break;

            case UNJAM:
                supersystems.donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
                supersystems.rollers.setIntakeInverse();
                supersystems.donut.spindexMotor.setPower(0);

                break;

            case NOTHING:
                supersystems.setRollers(false);
                supersystems.turret.setShooterSpeed(0);
                break;
        }




        telemetryM.debug("case  " + currentState);
        telemetryM.debug("intake state " + supersystems.intakeState);
        telemetryM.debug("shooting state " + supersystems.scorestate);
        telemetryM.debug("Shooter Speed "+ supersystems.turret.shooterMotorRight.getVelocity());
        telemetryM.debug("LL Distance "+supersystems.ll.getDistance() );
        telemetryM.debug("LL TX "+supersystems.ll.getTx() );

        telemetryM.debug("Donuts slots " + supersystems.peekAtSpindex());
        telemetryM.debug("Turn to next " + supersystems.switchToNext);

        telemetryM.update(telemetry);


        supersystems.update();

    }
}
