package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;

@Configurable
public class Supersystems {
    private Rollers rollers;
    private Donut donut;
    private DriveTrain train;
    private Turret turret;
    public Limelight ll;
    private PinPoint pin;
     private Donut.BallColor[] donutSlots = {Donut.BallColor.EMPTY, Donut.BallColor.EMPTY, Donut.BallColor.EMPTY};
    private int intakeState = 0;

    private int scorestate = 0;


    private Supersystems(HardwareMap hwMap){
        rollers = new Rollers(hwMap);
        donut = new Donut(hwMap);
        train = new DriveTrain(hwMap);
        turret = new Turret(hwMap);
        pin = new PinPoint(hwMap);
        ll = new Limelight(hwMap);

    }

    public void intake(boolean switchToNext, ElapsedTime timer){
        rollers.enableIntake();
        switch (intakeState){
            case 0:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_1);
                if(switchToNext)
                    donutSlots[0] = donut.getColor();
                    timer.reset();
                    intakeState = 1;
                break;
            case 1:
                if(timer.seconds() > 1){
                    intakeState = 2;
                }
                break;
            case 2:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_2);
                if(switchToNext)
                    donutSlots[1] = donut.getColor();
                    timer.reset();
                    intakeState = 3;
                break;
            case 3:
                if(timer.seconds() > 1){
                    intakeState = 4;
                }
                break;
            case 4:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_3);
                if(switchToNext)
                    donutSlots[2] = donut.getColor();
                    timer.reset();
                    intakeState = 5;
                break;
            case 5:
                if(timer.seconds() > 1){
                    intakeState = 0;
                }
        }
    }

    public void intakeWithDistance(ElapsedTime timer){
        boolean switchToNext = donut.getDistance() < 10.0;
        rollers.enableIntake();
        switch (intakeState){
            case 0:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_1);
                if(switchToNext)
                    donutSlots[0] = donut.getColor();
                timer.reset();
                intakeState = 1;
                break;
            case 1:
                if(timer.seconds() > 1){
                    intakeState = 2;
                }
                break;
            case 2:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_2);
                if(switchToNext)
                    donutSlots[1] = donut.getColor();
                timer.reset();
                intakeState = 3;
                break;
            case 3:
                if(timer.seconds() > 1){
                    intakeState = 4;
                }
                break;
            case 4:
                donut.setSpindexPosition(Donut.SpindexPositions.INTAKE_3);
                if(switchToNext)
                    donutSlots[2] = donut.getColor();
                timer.reset();
                intakeState = 5;
                break;
            case 5:
                if(timer.seconds() > 1){
                    intakeState = 0;
                }
        }
    }
    public void setRollers (boolean state){
        if (state){
            rollers.enableIntake();
        } else if (!state) {
            rollers.disableIntake();
        }
    }
    public void score (){
        if(hasAllColorsNeeded()){
            if (Messanger.sequence.equals("GPP")){
                shootGPP();
            } else if (Messanger.sequence.equals("PGP")) {
                shootPGP();
            }else if (Messanger.sequence.equals("PPG")) {
                shootPPG();
            }
        }else {
            shootAll();
        }


    }
    public boolean hasAllColorsNeeded(){
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
    private void shootGPP(){
        //to be tested
        int greenleSlot = -1;
        int purpleSlot1 = -1;
        int purpleSlot2 = -1;

        for (int i = 0; i < donutSlots.length; i++){
            if (donutSlots[i] == Donut.BallColor.GREEN){
                greenleSlot = i;
            } else if (donutSlots[i] == Donut.BallColor.PURPLE){
                if(purpleSlot1 == -1){
                    purpleSlot1 = i;
                }else {
                    purpleSlot2 = i;
                }
            }
        }

    }
    private void shootPGP(){

    }
    private void shootPPG(){

    }
    private void shootAll(){

    }
    private void shoot(){
        while (!turret.isReadyToShoot()){
            turret.setTurretWithLimelight(ll.getDistance(), ll.getTx());
        }
        donut.setPushUpServoPosition(Donut.PushUpPositions.UP);
    }

}
