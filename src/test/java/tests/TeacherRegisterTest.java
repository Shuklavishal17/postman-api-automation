package tests;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TeacherRegisterTest {

    @DataProvider(name = "teacherData")
    public Object[][] teacherData() throws IOException , CsvValidationException{
        List<String[]> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("src/test/resources/testdata/teacher_register_data.csv"))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                records.add(line);
            }
        }

        Object[][] data = new Object[records.size()][6];
        for (int i = 0; i < records.size(); i++) {
            String[] row = records.get(i);
            data[i][0] = row[0]; // name
            data[i][1] = row[1]; // email
            data[i][2] = row[2]; // password
            data[i][3] = row[3]; // phone
            data[i][4] = row[4]; // courseIds
            data[i][5] = Integer.parseInt(row[5]); // expectedStatus
        }
        return data;
    }

    @Test(dataProvider = "teacherData")
    public void testTeacherRegister(String name, String email, String password, String phone, String courseIds, int expectedStatus) {

        String requestBody = String.format(
                "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"phone\":\"%s\",\"courseIds\":\"%s\"}",
                name, email, password, phone, courseIds
        );

        RestAssured.baseURI = ConfigReader.get("baseUrl");

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/v1/teacher/register")
        .then()
            .statusCode(expectedStatus)
            .log().all();
    }
}
