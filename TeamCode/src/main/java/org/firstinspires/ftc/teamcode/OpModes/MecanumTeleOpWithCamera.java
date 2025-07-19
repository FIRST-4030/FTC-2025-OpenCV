package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.BuildConfig;
import org.firstinspires.ftc.teamcode.gamepad.InputAutoMapper;
import org.firstinspires.ftc.teamcode.gamepad.InputHandler;
import org.firstinspires.ftc.teamcode.opencv.DrawRectanglePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Config
@TeleOp
public class MecanumTeleOpWithCamera extends LinearOpMode {
    public static int c270_width  = 320;
    public static int c270_height = 240;

    public String[] cameraNames = {"WebcamC270", "Webcam1080"};

    InputHandler inputHandler;
    OpenCvWebcam webcam;
    DrawRectanglePipeline pipeline;
    String camera = null;
    Servo cvServo;
    int camera_width, camera_height;
    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        inputHandler = InputAutoMapper.normal.autoMap(this);

        camera = cameraNames[0];
        camera_width = c270_width;
        camera_height = c270_height;

        pipeline = new DrawRectanglePipeline(telemetry, camera);

        cvServo = hardwareMap.get(Servo.class, "servo");

        int cameraMonitorViewId = hardwareMap.appContext
                .getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory
                .getInstance()
                .createWebcam(hardwareMap.get(WebcamName.class, camera), cameraMonitorViewId);

        webcam.setPipeline(pipeline);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.startCameraStream(webcam, 30); // 30 FPS stream

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

        telemetry.addData("Compiled on:", BuildConfig.COMPILATION_DATE);
        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Streaming", "Camera is running");
            telemetry.update();
        }
    }
}
