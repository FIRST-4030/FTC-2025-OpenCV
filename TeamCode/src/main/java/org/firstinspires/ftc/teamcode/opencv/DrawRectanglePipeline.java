package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DrawRectanglePipeline extends OpenCvPipeline {
    static class Params {
        int rect1X, rect1Y, rect1W, rect1H;
        int rect2X, rect2Y, rect2W, rect2H;
        int rect3X, rect3Y, rect3W, rect3H;
    }

    Params PARAMS = new Params();

    Scalar nonSelectedColor = new Scalar(0, 255, 0);
    Scalar selectedColor = new Scalar(0, 0, 255);
    Rect rect1, rect2, rect3;
    public int selectedRect = -1;
    int frame = 0;
    Telemetry telemetry;

    Mat hsvMat = new Mat();

    public DrawRectanglePipeline( Telemetry _telemetry, String cameraName) {
        this.telemetry = _telemetry;

        setParams(cameraName);

        rect1 = new Rect(PARAMS.rect1X, PARAMS.rect1Y, PARAMS.rect1W, PARAMS.rect1H);
        rect2 = new Rect(PARAMS.rect2X, PARAMS.rect2Y, PARAMS.rect2W, PARAMS.rect2H);
        rect3 = new Rect(PARAMS.rect3X, PARAMS.rect3Y, PARAMS.rect3W, PARAMS.rect3H);
    }

    @Override
    public Mat processFrame(Mat input) {   // This method is called repeatedly
//        telemetry.addData("Processing Frame", frame++);
//        telemetry.update();
        selectedRect = findRectangle(input);
        drawRectangles( input );
        return input;
    }

    int findRectangle(Mat input) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        double satRect1 = getAvgSaturation(hsvMat, rect1);
        double satRect2 = getAvgSaturation(hsvMat, rect2);
        double satRect3 = getAvgSaturation(hsvMat, rect3);

        if ((satRect1>satRect2) && (satRect1>satRect3)) {
            return 1;
        }
        else if ((satRect2>satRect1) && (satRect2>satRect3)) {
            return 2;
        }
        return 3;
    }

    protected double getAvgSaturation(Mat input, Rect rect) {
        Mat submat = input.submat(rect);
        Scalar color = Core.mean(submat);
        return color.val[1];
    }

    public void drawRectangles(Mat input) {
        Imgproc.rectangle(input, rect1, nonSelectedColor);
        Imgproc.rectangle(input, rect2, nonSelectedColor);
        Imgproc.rectangle(input, rect3, nonSelectedColor);

        switch (selectedRect) {
            case 1:
                Imgproc.rectangle(input, rect1, selectedColor);
                break;
            case 2:
                Imgproc.rectangle(input, rect2, selectedColor);
                break;
            case 3:
                Imgproc.rectangle(input, rect3, selectedColor);
                break;
        }
    }

    private void setParams(String _cameraName) {
        if (_cameraName.equals("WebcamC270")) {
            PARAMS.rect1X = 50;
            PARAMS.rect1Y = 42;
            PARAMS.rect1W = 40;
            PARAMS.rect1H = 40;

            PARAMS.rect2X = 125;
            PARAMS.rect2Y = 42;
            PARAMS.rect2W = 40;
            PARAMS.rect2H = 40;

            PARAMS.rect3X = 200;
            PARAMS.rect3Y = 42;
            PARAMS.rect3W = 40;
            PARAMS.rect3H = 40;
        } else {
            PARAMS.rect1X = 275; // 300
            PARAMS.rect1Y = 45;
            PARAMS.rect1W = 50;
            PARAMS.rect1H = 50;

            PARAMS.rect2X = 360;  //400
            PARAMS.rect2Y = 45;
            PARAMS.rect2W = 50;
            PARAMS.rect2H = 50;

            PARAMS.rect3X = 445; // 500
            PARAMS.rect3Y = 45;
            PARAMS.rect3W = 50;
            PARAMS.rect3H = 50;
        }
    }
}