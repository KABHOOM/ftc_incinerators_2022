// Import Required Files And Packages
package org.firstinspires.ftc.teamcode;

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

    public void startMove(int time, String direction, float power) {

        // General Movement Function

        // Check The Direction And Move Correctly
        status = "on";
        if (direction == "front") {
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);
        }

        else if (direction == "back") {
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
    public void startViper(int time, String place, float power) {

        // If Buttons Are Pushed, Activate Viper Slider

        // Move Viper To Ground
        if (place == "ground") {
            status = "on";
            viperSlider.setPower(-power);
            sleep(1000);
        }

        // Move Viper Up
        else if (place == "low") {
            status = "on";
            viperSlider.setPower(power);
            sleep(300);
        }

        else if (place == "middle") {
            status = "on";
            viperSlider.setPower(power);
            sleep(600);
        }

        else if (place == "high") {
            status = "on";
            viperSlider.setPower(power);
            sleep(1000);
        }

        // Keep Running For Time Required
        sleep(time);

        // Reset To Ground
        viperSlider.setPower(-power);
        sleep(1000);

        // Once Done, Turn Off
        viperSlider.setPower(0);
        status = "off";
    }

    // Open Claw Function
    public void openClaw() {
        status = "on";
        clawL.setPosition(0.5);
        clawR.setPosition(0.4);
        status = "off";
    }

    // Close Claw Function
    public void closeClaw() {
        status = "on";
        clawL.setPosition(0.4);
        clawR.setPosition(0.3);
        status = "off";
    }

    // Auto Place & Redact Cone Function
    // Currently Untested And Not Working - Will Tip Robot Over
    // Coded Just For High Junction For Now
    // DO NOT USE
    public void autoCone() {

        status = "on";

        // Make Sure The Claw Is Closed
        closeClaw();

        // Short Backward Movement Away From Junction
        frontLeft.setPower(-0.2);
        backLeft.setPower(-0.2);
        frontRight.setPower(-0.2);
        backRight.setPower(-0.2);
        sleep(50);

        // Turn All Motors Off
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);

        // Move Viper Slider Up To Reach Junction
        viperSlider.setPower(1);
        sleep(1000);

        // Turn Off Motor
        viperSlider.setPower(0);

        // Short Forward Movement To Get Cone Over Junction
        frontLeft.setPower(0.2);
        backLeft.setPower(0.2);
        frontRight.setPower(0.2);
        backRight.setPower(0.2);
        sleep(100);

        // Turn Off Motors
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);

        // Drop Cone By Opening Claw
        openClaw();
        sleep(100);

        // Close Claw To Prepare For Redact
        closeClaw();

        // Move Robot Back To Original Position
        frontLeft.setPower(-0.2);
        backLeft.setPower(-0.2);
        frontRight.setPower(-0.2);
        backRight.setPower(-0.2);
        sleep(100);

        // Turn Motors Off
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);

        // Move Viper Slider Down
        viperSlider.setPower(-1);
        sleep(1000);

        // Turn Motor Off
        viperSlider.setPower(0);

        status = "off";
    }
}