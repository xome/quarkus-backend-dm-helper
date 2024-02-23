package de.mayer.api

import io.restassured.RestAssured.given
import io.restassured.common.mapper.TypeRef
import org.apache.http.HttpStatus

class Utils {
    companion object {
        fun getAllAdventures(): List<String> {
            val response: List<String> =
                given()
                    .`when`().get("/adventures")
                    .then().statusCode(HttpStatus.SC_OK)
                    .extract().body().`as`(object : TypeRef<List<String>>() {})
            return response
        }
    }
}