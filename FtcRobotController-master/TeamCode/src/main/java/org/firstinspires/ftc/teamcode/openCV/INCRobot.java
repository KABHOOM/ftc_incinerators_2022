// Import Required Files And Packages
package org.firstinspires.ftc.teamcode.openCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class INCRobot extends LinearOpMode {

    // Define All Motors
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backLeft = null;
    DcMotor backRight = null;
    DcMotor viperSlider = null;
    Servo clawL = null;
    Servo clawR = null;
    private int viperPos = 0;

    // Status For AutoCone - Will Show Whether Any Functions Are On And Will Let AutoCone Run Or Not
    String status;

    @Override
    public void runOpMode() {
        // Remain Empty For The Main Program
    }

    // Set Up The Hardware Maps And The Direction Of Motors
    public void set_configuration() {

        // Hardware Map All Motors
        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");
        viperSlider = hardwareMap.dcMotor.get("Viper_Slider");
        clawL = hardwareMap.servo.get("ClawL");
        clawR = hardwareMap.servo.get("ClawR");
        viperSlider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Reverse Direction Of Side Motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        clawR.setDirection(Servo.Direction.REVERSE);
    }

    // Movement Function
    // Inputs:
    // Time - How Long The Robot Should Move (Milliseconds As An Integer)
    // Direction - What Direction The Robot Should Move In (Front, Left, Back, Right, As A Lowercase String)
    // Power - How Much Power The Robot Should Move With (Float From 0 to 1)

    public void moveRobot(String direction,int time, float power) {

        // General Movement Function

        // Check The Direction And Move Correctly
        status = "on";
        if (direction == "forward") {
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);
        }

        else if (direction == "backward") {
            frontLeft.setPower(-power);
            frontRight.setPower(-power);
            backLeft.setPower(-power);
            backRight.setPower(-power);
        }

        else if (direction == "left") {
            frontLeft.setPower(-power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(-power);
        }

        else if (direction == "right") {
            frontLeft.setPower(power);
            frontRight.setPower(-power);
            backLeft.setPower(-power);
            backRight.setPower(power);
        }

        // Keep Running For The Time Required
        sleep(time);

        // Stop When Done
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        status = "off";
    }

    // Viper Slide Function
    // Inputs
    // Time - How Long The Viper Should Stay Up (Milliseconds As An Integer)
    // Place - Which Junction The Viper Should Move To (Ground, Low, Middle, And High As A Lowercase String)
    // Power - Speed The Viper Should Move With (Float From 0-1)
    public void moveViperSlideTo(String place) {
        int cur_pos;
        float power = 0.5F;
        // If Buttons Are Pushed, Activate Viper Slider

        closeClaw();
        // Move Viper To Ground
        if (place == "ground") {
            status = "on";
            viperSlider.setPower(-power);
            sleep(1000);
        }

        // Move Viper Up
        else if (place == "low") {
            viperSlider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //viperSlider.setPower(0);
            viperSlider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            //viperSlider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        else if (place == "middle") {
            status = "on";
            viperSlider.setPower(power);
            sleep(1500);
        }

        else if (place == "high") {
            status = "on";

            viperSlider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            cur_pos = viperSlider.getCurrentPosition();
            viperSlider.setTargetPosition(3000);
            viperSlider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            viperSlider.setPower(power);
            while(viperSlider.isBusy()){
                cur_pos = viperSlider.getCurrentPosition();
                telemetry.addLine(String.format("CUR_POS: %d",cur_pos));
                telemetry.update();
            }

        }
        sleep(500);


    }

    // Open Claw Function
    public void openClaw() {
        status = "on";
        clawL.setPosition(0.4);
        clawR.setPosition(0.3);
        status = "off";
    }

    // Close Claw Function
    public void closeClaw() {
        status = "on";
        clawL.setPosition(0.5);
        clawR.setPosition(0.4);
        status = "off";
    }


}