package org.firstinspires.ftc.teamcode.Autonomous2026;


import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.teamcode.Subsystems.Messenger;
import org.firstinspires.ftc.teamcode.Subsystems.Supersystems;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Auto Red Goal", group = "Autonomous")
@Configurable // Panels
public class AutonomousRedGoal extends OpMode {
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
        follower.setStartingPose(new Pose(125.04280155642026, 118.83268482490271, Math.toRadians(126)));

        paths = new Paths(follower);
        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.debug("Messenger pattern ", Messenger.sequence );
        panelsTelemetry.update(telemetry);

        pathTimer = new Timer();
        superTimer = new ElapsedTime();

        Messenger.allianceColor = "RED";
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
        public PathChain Path6;
        public PathChain Path7;
        public PathChain Path8;
        public PathChain Path9;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(125.043, 118.833),
                                    new Pose(84.093, 85.401)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(126), Math.toRadians(45))
                    .build();

            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(84.093, 85.401),
                                    new Pose(101.167, 84.047)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(0))
                    .build();

            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(101.167, 84.047),
                                    new Pose(125.774, 84.047)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .build();

            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(125.774, 84.047),
                                    new Pose(83.977, 85.374)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                    .build();

            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(83.977, 85.374),
                                    new Pose(101.416, 59.767)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(0))
                    .build();

            Path6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(101.416, 59.767),
                                    new Pose(135.759, 59.436)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .build();

            Path7 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(135.759, 59.436),
                                    new Pose(99.539, 55.603),
                                    new Pose(115.255, 73.160),
                                    new Pose(127.996, 70.152)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .build();

            Path8 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(127.996, 70.152),
                                    new Pose(84.148, 85.432)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                    .build();
            Path9 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(84.148, 85.432),
                                    new Pose(93.147, 76.295)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(45))
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
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.LL);
                supersystems.ll.start();
                supersystems.resetScore();
                supersystems.updateLLForSHooting();
                supersystems.aimTurretWithLL();
                if(!follower.isBusy()){
                    setPathState(1);
                }
                break;
            case 1:
                supersystems. score(superTimer);
                supersystems.updateTurretPIDs();
                supersystems.updateLLForSHooting();
                if(pathTimer.getElapsedTimeSeconds() > 10){
                    setPathState(2);
                }
                break;
            case 2:
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
                if(!follower.isBusy()){
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
                supersystems.resetScore();
                supersystems.updateLLForSHooting();
                supersystems.aimTurretWithLL();
                if(!follower.isBusy()){
                    setPathState(5);
                }
                break;
            case 5:
                supersystems. score(superTimer);
                supersystems.updateTurretPIDs();
                supersystems.updateLLForSHooting();
                if(pathTimer.getElapsedTimeSeconds() > 10){
                    setPathState(6);
                }
                break;
            case 6:
                if(!pathStarted){
                    follower.followPath(paths.Path5);
                    pathStarted = true;
                }
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.STOP);
                supersystems.resetIntake();
                if(!follower.isBusy()){
                    setPathState(7);
                }
                break;
            case 7:
                if(!pathStarted){
                    follower.followPath(paths.Path6, 0.2, true);
                    pathStarted = true;
                }

                supersystems.setRollers(true);
                supersystems.intakeWithDistance(superTimer);
                if(!follower.isBusy()){
                    setPathState(8);
                }
                break;
            case 8:
                if(!pathStarted){
                    follower.followPath(paths.Path7);
                    pathStarted = true;
                }
                if(!follower.isBusy()){
                    setPathState(9);
                }
                break;
            case 9:
                if(!pathStarted){
                    follower.followPath(paths.Path8);
                    pathStarted = true;
                }
                supersystems.rollers.intakeMotor.setPower(0.5);
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.LL);
                supersystems.ll.start();
                supersystems.resetScore();
                supersystems.updateLLForSHooting();
                supersystems.aimTurretWithLL();
                if(!follower.isBusy()){
                    setPathState(10);
                }
                break;
            case 10:
                supersystems. score(superTimer);
                supersystems.updateTurretPIDs();
                supersystems.updateLLForSHooting();
                if(pathTimer.getElapsedTimeSeconds() > 10){
                    setPathState(11);
                }
                break;
            case 11:
                supersystems.setTurretUpdateMode(Supersystems.TURRET_UPDATE_MODE.STOP);
                if(!pathStarted){
                    follower.followPath(paths.Path9);
                    pathStarted = true;
                }
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