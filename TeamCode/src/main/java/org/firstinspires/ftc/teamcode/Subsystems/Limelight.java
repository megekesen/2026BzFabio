package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Configurable
public class Limelight {
    private Limelight3A limelight;

    public enum Pipelines{PATTERN_RECOGNITION (0), BLUE_TARGET(1), RED_TARGET(2);
        public int number;
        Pipelines(int number){
            this.number = number;
        }

    }

    double scale = 7799.701;
    // FOLLOW THIS TUTORIAL TO FIND THE SCALE
    // https://youtu.be/Ap1lBywv00M?si=vfVvohYDsQcFGoUW

    private double distance = 0.0;
    private double tx = 0.0;
    private double ta = 0.0;
    public Limelight(@NonNull HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    public void start(){
        limelight.start();
    }

    public void switchPipeline (Pipelines pipeline){
        limelight.pipelineSwitch(pipeline.number);
    }

    public void stop(){
        limelight.stop();
    }

    /**
     * for teleop to get the distance and calculate subsequently the hood extension and shooter speeed;
     * Updates the distance and TX values. you can get them w the respective getter functions
     */
    public void updateAimLL (){
        LLResult llResult = limelight.getLatestResult();
        if (llResult!= null && llResult.isValid()) {
            distance = getDistanceFromTag(llResult.getTa());
            tx = llResult.getTx();
            ta = llResult.getTa();
        }
    }

    public boolean llCanAim(){
        LLResult llResult = limelight.getLatestResult();
        return llResult!= null && llResult.isValid();
    }

    /**
     * useed during autonomous init sequence to sense the pattern
     * @return the id of the pattern tag recognized. if -1 no tag recognized
     */
    public int updatePatternLL(){
        LLResult llResult = limelight.getLatestResult();
        if (llResult != null && !llResult.getFiducialResults().isEmpty()) {
            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            return fiducialResults.get(0).getFiducialId();
        } else {
            return -1;
        }
    }

    private double getDistanceFromTag(double ta){
        double distance = 183.6147*(Math.pow(ta, -0.5453));
        return 183.6147*(Math.pow(ta, -0.5453));
    }


    public double getDistance(){return distance;}
    public double getTx(){
        return tx;
    }

    public double getTa() {return ta;}
}