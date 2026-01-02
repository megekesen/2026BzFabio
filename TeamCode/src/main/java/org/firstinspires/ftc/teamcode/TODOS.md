# Drive TrainTesting
   - check with the controller if the drivetrain works as intended by using the DriveTrainTestAndTune
   - if you want, tune the rotational pid to have positional presets(test extensively to make sure they work)
   - Remember to set the pid values in the actual class and not just in the panels interface
   - if you don't want to use them just ser the rot variable in the Drive function to 0
# Donut Calibration
   - use the DonutTestAndTune to make sure all of your servos work, and find all the intake and the shootout values 
   - (If you can't change the enums from panel just do it manually from code, or make a simple function)
   - remember to save them to the actual Donut class once you find them 
# Limelight testing
   - i made 3 pipelines:
     - 0 : sequence beacon recognition. use the function updatePatternLL to ger the id number
     - 1 : Blue Target Recognition, use the updateAimLL(pinpoint.getYaw()) to get distance and tx
     - 2 : Red Target Recognition, use the updateAimLL(pinpoint.getYaw()) to get distance and tx
   - in order to use these correctly, first of all mount the limelight and make sure that the view is good
   - if the image flickers, ajdust the exposure ( i advice to do this whenever you switch locations just to kae sure)
   - follow the video to find your distance equation and test it to make sure it is corect
# Turret Calibration
   - first calibrate the two PIDs. 
   - If you ended up putting some extra weights on the spinner as a fly wheel, look up the video on how to tune it w feed forward from coach pratt
   - (you will need to turn the PIDController into a PIDFController, should be straight forward enough.)
   - if you want, code the set with limelight function. this should make a bunch of math to go from the distance to the target to the shooter speed and hood position, otherwise just pick one spot and always use that
   - what you should try and get to work is auto aiming as i think it is the easiest. By using the getTX function, use the tx value to set a target position for the turret, so that when the tx is 0, the turret does not move, and as you move the robot relative to the apriltag (blue or red goal) the turret should rotate accordingly
