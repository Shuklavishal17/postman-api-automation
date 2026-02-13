package tests;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class TeacherloginTest {
	@Test
	public void testLogin(){
		RestAssured.baseURI = "https://lmspro.techsaga.live";
		
		String requestBody = """
			{
				"email": "nitin@yopmail.com",
				"password": "Nitin@123"
			}
		""";
		given()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.log().body()
		
		.when()
			.post("/api/v1/teacher/login")
		
			.then()
				.log().all()
				.statusCode(200);
	}

	
}
