package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.gamepad.InputHandler;
import org.firstinspires.ftc.teamcode.opencv.DrawRectanglePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Disabled
@Config
@TeleOp
public class MecanumAutoLimeLight extends LinearOpMode {
    public static int c270_width  = 320;
    public static int c270_height = 240;
    public static double pos1 = 0.0;
    public static double pos2 = 0.5;
    public static double pos3 = 1.0;

    public String[] cameraNames = {"WebcamC270", "Webcam1080", "limelight"};

    InputHandler inputHandler;
    OpenCvWebcam webcam;
    DrawRectanglePipeline pipeline;
    String camera = null;
    Servo cvServo;
    boolean inputComplete= false;
    int camera_width, camera_height;

    private Limelight3A limelight;

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() {

        camera = cameraNames[2];
        camera_width = c270_width;
        camera_height = c270_height;

        pipeline = new DrawRectanglePipeline(telemetry, camera);

        cvServo = hardwareMap.get(Servo.class, "servo");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        int cameraMonitorViewId = hardwareMap.appContext
                .getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

//        webcam = OpenCvCameraFactory
//                .getInstance()
//                .createWebcam(limelight), cameraMonitorViewId);

        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(camera_width, camera_height, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera Error", errorCode);
                telemetry.update();
            }
        });

        telemetry.addData(camera, " will be used");
        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        webcam.stopStreaming();

        if (isStopRequested()) return;

        switch (pipeline.selectedRect) {
            case 1:
                telemetry.addData("Set Servo to: ", String.format("%.2f",pos1));
                cvServo.setPosition(pos1);
                break;
            case 2:
                telemetry.addData("Set Servo to: ", String.format("%.2f",pos2));
                cvServo.setPosition(pos2);
                break;
            case 3:
                telemetry.addData("Set Servo to: ", String.format("%.2f",pos3));
                cvServo.setPosition(pos3);
                break;
        }

        telemetry.addData("Selected Rectangle: ",pipeline.selectedRect);
        telemetry.update();
        sleep(1000);
    }
}
