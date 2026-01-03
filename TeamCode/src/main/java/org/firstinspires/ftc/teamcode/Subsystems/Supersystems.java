package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;

@Configurable
public class Supersystems {
    private Rollers rollers;
    private Donut donut;
    public DriveTrain train;
    private Turret turret;
    public Limelight ll;
    public PinPoint pin;
    private Donut.BallColor[] donutSlots = {Donut.BallColor.EMPTY, Donut.BallColor.EMPTY, Donut.BallColor.EMPTY};
    private int intakeState = 0;
    public static double timeToWaitForSwitchingSpindex = 1.0;
    public static double  timeToWaitForShooting = 1.0;
    public static double  timeToWaitForPushUp = 1.0;

    private int scorestate = 0;

    public static double turretPositionForIntake = 0.0;
    public static double hoodPositionForIntake = 0.0;
    public static int shooterSpeedForIntake = -0;


    public Supersystems(HardwareMap hwMap, boolean isThisAutonomous){
        rollers = new Rollers(hwMap);
        donut = new Donut(hwMap);
        turret = new Turret(hwMap);
        ll = new Limelight(hwMap);

        if (!isThisAutonomous){
            train = new DriveTrain(hwMap);
            pin = new PinPoint(hwMap);
        }
    }

    public void intake(boolean switchToNext, ElapsedTime timer){
        rollers.enableIntake();
        switch (intakeState){
            case 0:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_1);
                if(switchToNext) {
                    donutSlots[0] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_2);

                    timer.reset();
                    intakeState = 1;
                }
                break;
            case 1:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 2;
                }
                break;
            case 2:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_2);
                if(switchToNext) {
                    donutSlots[1] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_3);

                    timer.reset();
                    intakeState = 3;
                }
                break;
            case 3:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 4;
                }
                break;
            case 4:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_3);
                if(switchToNext) {
                    donutSlots[2] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_1);

                    timer.reset();
                    intakeState = 5;
                }
                break;
            case 5:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 0;
                }
        }
    }

    public void intakeWithDistance(ElapsedTime timer){
        boolean switchToNext = donut.getFrontDistance() < 10.0;
        rollers.enableIntake();
        switch (intakeState){
            case 0:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_1);
                if(switchToNext) {
                    donutSlots[0] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_2);
                    timer.reset();
                    intakeState = 1;
                }
                break;
            case 1:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 2;
                }
                break;
            case 2:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_2);
                if(switchToNext) {
                    donutSlots[1] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_3);
                    timer.reset();
                    intakeState = 3;
                }
                break;
            case 3:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 4;
                }
                break;
            case 4:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_3);
                if(switchToNext) {
                    donutSlots[2] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_1);
                    timer.reset();
                    intakeState = 5;
                }
                break;
            case 5:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 0;
                }
        }
    }

    public void intakeFromShooter(boolean switchToNext, ElapsedTime timer){
        turret.setTurretTarget(turretPositionForIntake);
        turret.setHood(hoodPositionForIntake);
        turret.setShooterSpeed(shooterSpeedForIntake);
        switch (intakeState){
            case 0:
                donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_1);
                if(switchToNext) {
                    donutSlots[0] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_2);

                    timer.reset();
                    intakeState = 1;
                }
                break;
            case 1:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 2;
                }
                break;
            case 2:
                donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_2);
                if(switchToNext) {
                    donutSlots[1] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_3);

                    timer.reset();
                    intakeState = 3;
                }
                break;
            case 3:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 4;
                }
                break;
            case 4:
                donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_3);
                if(switchToNext) {
                    donutSlots[2] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_1);

                    timer.reset();
                    intakeState = 5;
                }
                break;
            case 5:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 0;
                }
        }
    }

    public void intakeWithDistanceFromShooter(ElapsedTime timer){
        turret.setTurretTarget(turretPositionForIntake);
        turret.setHood(hoodPositionForIntake);
        turret.setShooterSpeed(shooterSpeedForIntake);
        boolean switchToNext = donut.getBackDistance() < 10.0;
        switch (intakeState){
            case 0:
                donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_1);
                if(switchToNext) {
                    donutSlots[0] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_2);
                    timer.reset();
                    intakeState = 1;
                }
                break;
            case 1:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 2;
                }
                break;
            case 2:
                donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_2);
                if(switchToNext) {
                    donutSlots[1] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_3);
                    timer.reset();
                    intakeState = 3;
                }
                break;
            case 3:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 4;
                }
                break;
            case 4:
                donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_3);
                if(switchToNext) {
                    donutSlots[2] = donut.getColor();
                    donut.setSpindexPosition(Donut.SpindexPositions.SHOTER_1);

                    timer.reset();
                    intakeState = 5;
                }
                break;
            case 5:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 0;
                }
        }
    }

    /**
     * use before starting the intake to reset the state to 0
     */
    public void resetIntake(){
        intakeState = 0;
        setSlotsToEmpty();
    }
    /**
     * use before starting the shooting to reset the state to 0
     */
    public void resetScore(){
        scorestate = 0;
    }
    public void setRollers (boolean state){
        if (state){
            rollers.enableIntake();
        } else if (!state) {
            rollers.disableIntake();
        }
    }
    public void score (ElapsedTime timer){
        Donut.SpindexPositions[] spindexPositions = new Donut.SpindexPositions[3];

        if(hasAllColorsNeeded()){
            if (Messanger.sequence.equals("GPP")){
                Donut.BallColor[] targetSequence = {Donut.BallColor.GREEN, Donut.BallColor.PURPLE, Donut.BallColor.PURPLE};
                shootWithSequence(timer, findSequence(targetSequence));
            } else if (Messanger.sequence.equals("PGP")) {
                Donut.BallColor[] targetSequence = {Donut.BallColor.PURPLE, Donut.BallColor.GREEN, Donut.BallColor.PURPLE};
                shootWithSequence(timer, findSequence(targetSequence));
            }else if (Messanger.sequence.equals("PPG")) {
                Donut.BallColor[] targetSequence = {Donut.BallColor.PURPLE, Donut.BallColor.PURPLE, Donut.BallColor.GREEN};
                shootWithSequence(timer, findSequence(targetSequence));
            }
        }else {
            spindexPositions[0] = Donut.SpindexPositions.SHOTER_1;
            spindexPositions[1] = Donut.SpindexPositions.SHOTER_2;
            spindexPositions[2] = Donut.SpindexPositions.SHOTER_3;
            shootWithSequence(timer, spindexPositions);
        }


    }
    private boolean hasAllColorsNeeded(){
        boolean valid = false;
        if(Arrays.stream(donutSlots).noneMatch(slot -> slot == Donut.BallColor.EMPTY)){
            int purple = 0;
            int green = 0;

            for (Donut.BallColor slot : donutSlots) {
                if (slot == Donut.BallColor.PURPLE) purple++;
                else if (slot == Donut.BallColor.GREEN) green++;
            }

            valid = purple >= 2 && green >= 1;
        }
        return valid;

    }

    /**
     *
     * @param targetSequence a 3 element array detailin the target sequence
     * @return a 3 element array detailing how the donut slots will be shot based on the taraget sequence
     */
    private Donut.SpindexPositions[] findSequence(Donut.BallColor[] targetSequence){
        Donut.BallColor[] donutslots = donutSlots;
        Donut.SpindexPositions[] shootSequence = new Donut.SpindexPositions[3];

        for (int i = 0; i < targetSequence.length; i++) {
            for (int j = 0; j < donutslots.length; j++) {
                if (targetSequence[i].equals(donutslots[j])) {
                    shootSequence[i] = Donut.SpindexPositions.values()[j + 3];
                    donutslots[j] = null;
                    break;
                }
            };
        }

        return shootSequence;
    }
    private void shootWithSequence(ElapsedTime timer, Donut.SpindexPositions[] spindexPositions){

        Donut.SpindexPositions pos1 = spindexPositions[0];
        Donut.SpindexPositions pos2 = spindexPositions[1];
        Donut.SpindexPositions pos3 = spindexPositions[2];

        switch (scorestate){
            case 0:
                donut.setSpindexPosition(pos1);
                scorestate = 1;
                timer.reset();
                break;
            case 1:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 2;
                }
                break;
            case 2:
                if (shoot()){
                    scorestate = 3;
                    timer.reset();
                }
                break;
            case 3:
                if(timer.seconds() > timeToWaitForShooting){
                    intakeState = 4;
                }
                break;
            case 4 :
                donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
                scorestate = 5;
                timer.reset();
                break;
            case 5 :
                if (timer.seconds() > timeToWaitForPushUp){
                    scorestate = 6;
                }
                break;
            case 6:
                donut.setSpindexPosition(pos2);
                scorestate = 7;
                timer.reset();
                break;
            case 7:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 8;
                }
                break;
            case 8:
                if (shoot()){
                    scorestate = 9;
                    timer.reset();
                }
                break;
            case 9:
                if(timer.seconds() > timeToWaitForShooting){
                    intakeState = 10;
                }
                break;
            case 10 :
                donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
                scorestate = 11;
                timer.reset();
                break;
            case 11 :
                if (timer.seconds() > timeToWaitForPushUp){
                    scorestate = 12;
                }
                break;
            case 12:
                donut.setSpindexPosition(pos3);
                scorestate = 13;
                timer.reset();
                break;
            case 13:
                if(timer.seconds() > timeToWaitForSwitchingSpindex){
                    intakeState = 14;
                }
                break;
            case 14:
                if (shoot()){
                    scorestate = 15;
                    timer.reset();
                }
                break;
            case 15:
                if(timer.seconds() > timeToWaitForShooting){
                    intakeState = 16;
                }
                break;
            case 16 :
                donut.setPushUpServoPosition(Donut.PushUpPositions.DOWN);
                scorestate = 17;
                timer.reset();
                break;
            case 17 :
                if (timer.seconds() > timeToWaitForPushUp){
                    scorestate = 0;
                }
                break;
        }
    }
    private boolean shoot(){
        if(turret.isReadyToShoot()){
            donut.setPushUpServoPosition(Donut.PushUpPositions.UP);
            return true;
        }
        return false;
    }

    public void setTurretON(){
        turret.setTurretWithLimelight(ll.getDistance(), ll.getTx());
    }


    private void setSlotsToEmpty(){
        donutSlots[0] = Donut.BallColor.EMPTY;
        donutSlots[2] = Donut.BallColor.EMPTY;
        donutSlots[3] = Donut.BallColor.EMPTY;
    }



    public void updateTurretPIDs(){
        turret.update();
    }
    public void updateLLForSHooting(){
        ll.updateAimLL();
    }
    public void updateLLForPattern(){
        ll.updatePatternLL();
    }
}
