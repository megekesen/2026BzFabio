package org.firstinspires.ftc.teamcode.Supersystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Donut;
import org.firstinspires.ftc.teamcode.Subsystems.Rollers;

public class Intake {
    private Rollers rollers;
    private Donut donut;
    private enum SlotState{GREEN, PURPLE, EMPTY, UNKNOWN}
    private SlotState[] donutSlots = {SlotState.EMPTY, SlotState.EMPTY, SlotState.EMPTY};

    private Intake (HardwareMap hwMap){
        rollers = new Rollers(hwMap);
        donut = new Donut(hwMap);
    }



}
