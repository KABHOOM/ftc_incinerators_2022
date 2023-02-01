// Import Required Files
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

// Send Code And Operating Mode To Game Board
@TeleOp(name = "Robot Code", group = "Incinerators Robot")
public class ftc_incinerators_teleop extends LinearOpMode {

    // Define All Motors
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backLeft = null;
    DcMotor backRight = null;
    DcMotor viperSlider = null;
    Servo clawL = null;
    Servo clawR = null;

    // Status For AutoCone
    String status;

    // SpeedFactor
    double moveSpeedFactor = 0.75;
    double armSpeedFactor = 0.6;

    @Override
    public void runOpMode() {

        // Set Configuration Function
        set_configuration();

        // Only Start Code And Movement When The Start Button Is Pressed
        waitForStart();

        while (opModeIsActive()) {

            // Activate Movement With SpeedFactor
            startMove();

            // Activate Precise Movement
            startPreciseMovement();

            // Activate Viper Slider With SpeedFactor
            startViper();

            // Activate Claw
            openClaw();
            closeClaw();

            // Auto Cone Placer
            autoCone();

            // Display the current values
            telemetry.addData("Right Claw", "%5.2f", clawR.getPosition());
            telemetry.addData("Left Claw", "%5.2f", clawL.getPosition());
            telemetry.addData("Right Trigger", "%5.2f", gamepad2.right_trigger);
            telemetry.addData("Left Trigger", "%5.2f", gamepad2.left_trigger);
            telemetry.addData("Left Bumper", gamepad2.left_bumper);
            telemetry.addData("Right Bumper", gamepad2.right_bumper);
            telemetry.addData("Status", status);
            telemetry.update();
        }
    }

    // Set Up The Hardware Maps And The Direction Of Motors
    public void set_configuration(){
        // Hardware Map All Motors
        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");
        viperSlider = hardwareMap.dcMotor.get("Viper_Slider");
        clawL = hardwareMap.servo.get("ClawL");
        clawR = hardwareMap.servo.get("ClawR");

        // Reverse Direction Of Side Motors44
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        clawR.setDirection(Servo.Direction.REVERSE);
    }

    // Movement Function
    public void startMove() {

        // Set Conditions For Activation
        if (gamepad1.right_stick_y != 0.0 || gamepad1.right_stick_x != 0.0 || gamepad1.left_stick_x != 0.0 || gamepad1.left_stick_y != 0.0) {
            // If Joy Sticks Are Active, Move Robot
            status = "on";
            // Move Robot Based On Position Of Joystick Calculations
            frontLeft.setPower((-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x) * moveSpeedFactor);
            backLeft.setPower((-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x) * moveSpeedFactor);
            frontRight.setPower((-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x) * moveSpeedFactor);
            backRight.setPower((-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x) * moveSpeedFactor);
        }

        // Or Else Set All To Power 0 To Stop Movement
        else {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
        status = "off";
    }

    // Precise Movement Function
    public void startPreciseMovement() {

        // Make Movement In 4 Directions Slow And Precise With D-Pad controls

        // Move Forward With Dpad Up Key
        if (gamepad1.dpad_up) {
            status = "on";
            frontLeft.setPower(0.2 * moveSpeedFactor);
            backLeft.setPower(0.2 * moveSpeedFactor);
            frontRight.setPower(0.2 * moveSpeedFactor);
            backRight.setPower(0.2 * moveSpeedFactor);
        }

        // Move Backward With Dpad Down Key
        else if (gamepad1.dpad_down) {
            status = "on";
            frontLeft.setPower(-0.2 * moveSpeedFactor);
            backLeft.setPower(-0.2 * moveSpeedFactor);
            frontRight.setPower(-0.2 * moveSpeedFactor);
            backRight.setPower(-0.2 * moveSpeedFactor);
        }

        // Move Left With Dpad Left Key
        else if (gamepad1.dpad_left) {
            status = "on";
            frontLeft.setPower(-0.2 * moveSpeedFactor);
            backLeft.setPower(0.2 * moveSpeedFactor);
            frontRight.setPower(0.2 * moveSpeedFactor);
            backRight.setPower(-0.2 * moveSpeedFactor);
        }

        // Move Right With Dpad Right Key
        else if (gamepad1.dpad_right) {
            status = "on";
            frontLeft.setPower(0.2 * moveSpeedFactor);
            backLeft.setPower(-0.2 * moveSpeedFactor);
            frontRight.setPower(-0.2 * moveSpeedFactor);
            backRight.setPower(0.2 * moveSpeedFactor);
        }

        // Once Done, Stop Movement
        else {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
            status = "off";
        }
    }

    // Viper Slide Function
    public void startViper() {
        int cur_pos;

        // If Buttons Are Pushed, Activate Viper Slider

        // Move Viper Down With Right Bumper Key
        if (gamepad2.right_bumper) {
            status = "on";
            viperSlider.setPower(-armSpeedFactor);

        }

        // Move Viper Down With Right Bumper Key
        else if (gamepad2.left_bumper) {
            status = "on";
            viperSlider.setPower(armSpeedFactor);
        }

        // Once Done, Turn Off
        else {
            viperSlider.setPower(0);
            status = "off";
        }

    }

    // Open Claw Function
    public void openClaw() {

        // Conditions For Claw Opening With Right Trigger Key
        if (gamepad2.right_trigger > 0) {
            status = "on";
            clawL.setPosition(0.5);
            clawR.setPosition(0.4);
            status = "off";
        }
    }

    // Close Claw Function
    public void closeClaw() {

        // Conditions For Claw Closing With Left Trigger Key
        if (gamepad2.left_trigger > 0) {
            status = "on";
            clawL.setPosition(0.4);
            clawR.setPosition(0.3);
            status = "off";
        }
    }

    // Auto Place & Redact Cone Function
    public void autoCone() {

        // Check If The Button Is Pressed And the Status Of Other Functions If Off
        if (gamepad2.a == true && status == "off") {
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
}