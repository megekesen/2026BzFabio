package org.firstinspires.ftc.teamcode.Autonomous2026;


import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Messenger;
import org.firstinspires.ftc.teamcode.Subsystems.Supersystems;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Auto blue Goal", group = "Autonomous")
@Configurable // Panels
public class AutonomousBlueGoal extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState = -1; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    Supersystems supersystems;
    private Timer pathTimer;
    private ElapsedTime superTimer;
    private boolean pathStarted = false;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(17.084257206208424, 110.53113271847256, Math.toRadians(10)));

        paths = new Paths(follower);
        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.debug("Messenger pattern ", Messenger.sequence );
        panelsTelemetry.update(telemetry);

        pathTimer = new Timer();
        superTimer = new ElapsedTime();

        Messenger.allianceColor = "BLUE";
        supersystems = new Supersystems(hardwareMap, true);
        supersystems.setLLforPatternRecognition();

    }
    @Override
    public void init_loop(){
        supersystems.updateLLForPattern();
        supersystems.detectAndSavePattern();
        panelsTelemetry.debug("Messenger pattern ", Messenger.sequence );
        panelsTelemetry.update(telemetry);
    }
    @Override
    public void start(){
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();// Update Pedro Pathing
        autonomousPathUpdate();
        supersystems.update();


        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.debug("Messenger pattern ", Messenger.sequence );
        panelsTelemetry.update(telemetry);
    }

    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(17.404, 110.691),
                                    new Pose(60.000, 85.401)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(10), Math.toRadians(135))
                    .build();

            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(60.000, 85.401),
                                    new Pose(43.000, 84.047)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                    .build();

            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(43.000, 84.047),
                                    new Pose(19.000, 84.047)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(19.000, 84.047),
                                    new Pose(60.000, 85.374)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .build();

            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(60.000, 85.374),
                                    new Pose(17.765, 74.122)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(90))
                    .build();
        }
    }


    public void autonomousPathUpdate() {
        switch (pathState){
            case 0:
                if(!pathStarted){
                    follower.followPath(paths.Path1);
                    pathStarted = true;
                }
                supersystems.rollers.intakeMotor.setPower(0.5);
                supersystems.updateLLForPattern();
                supersystems.detectAndSavePattern();
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.LL);

                supersystems.resetScore(superTimer);
                supersystems.updateLLForSHooting();
                supersystems.aimTurretWithLL();
                if(!follower.isBusy()){
                    setPathState(1);
                }
                break;
            case 1:
                supersystems.ll.start();
                supersystems. score(superTimer);
                supersystems.update();
                supersystems.updateLLForSHooting();
                if(pathTimer.getElapsedTimeSeconds() > 8){
                    setPathState(2);
                }
                break;
            case 2:
                supersystems.turret.setShooterSpeed(0);
                if(!pathStarted){
                    follower.followPath(paths.Path2);
                    pathStarted = true;
                }
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.STOP);
                supersystems.resetIntake();
                if(!follower.isBusy()){
                    setPathState(3);
                }
                break;
            case 3:
                if(!pathStarted){
                    follower.followPath(paths.Path3, 0.2, true);
                    pathStarted = true;
                }


                supersystems.setRollers(true);
                supersystems.intakeWithDistance(superTimer);
                if(!follower.isBusy() || pathTimer.getElapsedTimeSeconds()> 6){
                    setPathState(4);
                }
                break;
            case 4:
                if(!pathStarted){
                    follower.followPath(paths.Path4);
                    pathStarted = true;
                }
                supersystems.rollers.intakeMotor.setPower(0.5);
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.LL);
                supersystems.ll.start();
                supersystems.resetScore(superTimer);
                supersystems.updateLLForSHooting();
                supersystems.aimTurretWithLL();
                if(!follower.isBusy()){
                    setPathState(5);
                }
                break;
            case 5:
                supersystems. score(superTimer);
                supersystems.update();
                supersystems.updateLLForSHooting();
                if(pathTimer.getElapsedTimeSeconds() > 8){
                    setPathState(6);
                }
                break;
            case 6:
                if(!pathStarted){
                    follower.followPath(paths.Path5);
                    pathStarted = true;
                }
                supersystems.turret.setShooterSpeed(0);
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.STOP);
                supersystems.resetIntake();
                if(!follower.isBusy()){
                    setPathState(-1);
                }
                break;

        }
    }

    public void setPathState (int state){
        pathState = state;
        pathStarted = false;
        pathTimer.resetTimer();
    }
}