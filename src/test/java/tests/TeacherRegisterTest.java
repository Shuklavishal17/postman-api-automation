package tests;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.aventstack.extentreports.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;
import utils.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class TeacherRegisterTest {

    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setup() {
        extent = ExtentManager.getInstance();
        RestAssured.baseURI = ConfigReader.get("baseUrl");
    }

    @DataProvider(name = "teacherData")
    public Object[][] teacherData() throws IOException, CsvValidationException {

        List<Object[]> data = new ArrayList<>();
        CSVReader reader = new CSVReader(
                new FileReader("src/test/resources/testdata/teacher_register_data.csv")
        );

        reader.readNext();
        String[] row;

        while ((row = reader.readNext()) != null) {
            data.add(new Object[]{
                    row[0], 
                    row[1], 
                    row[2],
                    row[3], 
                    row[4],
                    Integer.parseInt(row[5])
            });
        }

        reader.close();
        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "teacherData")
    public void testTeacherRegister(String name,
                                    String email,
                                    String password,
                                    String phone,
                                    String courseIds,
                                    int expectedStatus) {

        test = extent.createTest("Register Test - " + email);

        List<String> courseIdList =
                (courseIds != null && !courseIds.isEmpty())
                        ? Arrays.asList(courseIds.split("\\|"))
                        : new ArrayList<>();

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        body.put("phone", phone);
        body.put("courseIds", courseIdList);

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/api/v1/teacher/register");

        int actualStatus = response.getStatusCode();

        String description = "";
        try {
            description = response.jsonPath().getString("message");
        } catch (Exception e) {
            description = "No message returned";
        }

        String result = (actualStatus == expectedStatus) ? "PASS" : "FAIL";

        CSVReportUtil.writeResult(
                email, expectedStatus,
                actualStatus, result, description
        );

        if (result.equals("PASS")) {
            test.pass("Test Passed");
        } else {
            test.fail("Test Failed");
        }

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schema/teacher_register_schema.json"))
                .statusCode(expectedStatus);
    }

    @AfterSuite
    public void tearDown() {
        extent.flush();
        CSVReportUtil.saveReport();
    }
}
