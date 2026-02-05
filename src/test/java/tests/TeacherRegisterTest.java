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
import java.util.*;

import static io.restassured.RestAssured.given;

public class TeacherRegisterTest {

    @DataProvider(name = "teacherData")
    public Object[][] teacherData() throws IOException, CsvValidationException {

        List<Object[]> data = new ArrayList<>();

        CSVReader reader = new CSVReader(
                new FileReader("src/test/resources/testdata/teacher_register_data.csv")
        );

        reader.readNext(); // skip header
        String[] row;

        while ((row = reader.readNext()) != null) {
            data.add(new Object[]{
                    row[0], // name
                    row[1], // email
                    row[2], // password
                    row[3], // phone
                    row[4], // courseIds
                    Integer.parseInt(row[5]) // expectedStatus
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

        RestAssured.baseURI = ConfigReader.get("baseUrl");

        // 🔹 Convert courseIds to List
        List<String> courseIdList = new ArrayList<>();
        if (courseIds != null && !courseIds.isEmpty()) {
            courseIdList = Arrays.asList(courseIds.split("\\|"));
        }

        // 🔹 Request body using Map
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        body.put("phone", phone);
        body.put("courseIds", courseIdList);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().body()
        .when()
                .post("/api/v1/teacher/register")
        .then()
                .log().all()
                .statusCode(expectedStatus);
    }
}
