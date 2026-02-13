package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {

        if (extent == null) {

            ExtentSparkReporter spark =
                    new ExtentSparkReporter("reports/TeacherRegisterReport.html");

            spark.config().setReportName("LMS API Automation Report");
            spark.config().setDocumentTitle("Teacher Register Execution");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }

        return extent;
    }
}
