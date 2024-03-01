package de.mayer.api

import de.mayer.persistence.AdventurePanacheRepo
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@QuarkusTest
class AdventureApiTest {

    @Inject
    lateinit var adventurePanacheRepo: AdventurePanacheRepo

    @Inject
    lateinit var utils: Utils

    val url = "/adventure/{adventureName}"

    @BeforeEach
    @Transactional
    fun initData() {
        adventurePanacheRepo.deleteAll()
    }

    @DisplayName(
        """
        Given there exists no Adventure with the name "Testadventure",
        When it is created,
        Then it can be read
            And it can be updated
            And it can be deleted
    """
    )
    @Test
    fun createReadUpdateDelete() {
        val adventureName = "Testadventure"
        put(adventureName)
            .then().statusCode(HttpStatus.SC_OK)

        assertThat(utils.getAllAdventures(), `is`(listOf(adventureName)))

        val newAdventureName = "Updated Adventure"

        patch(adventureName, newAdventureName)
            .then().statusCode(HttpStatus.SC_OK)

        assertThat(utils.getAllAdventures(), `is`(listOf(newAdventureName)))

        delete(newAdventureName)
            .then().statusCode(HttpStatus.SC_OK)

        assertThat(utils.getAllAdventures(), `is`(emptyList()))
    }

    @DisplayName(
        """
        Given there already is an Adventure by the name "Testadventure",
        When a new Adventure with the name "Testadventure" is to be created,
        Then StatusCode 400 is returned
    """
    )
    @Test
    fun adventureByTheNameAlreadyExists() {
        val name = "Testadventure"
        utils.insertAdv(name)

        put(name)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)

    }

    @DisplayName(
        """
        Given there is no Adventure by the name "Testadventure",
        When a Adventure by the name "Testadventure" should be renamed,
        Then StausCode 404 is returned
    """
    )
    @Test
    fun renameNonExistentAdventure() {
        patch("does not exist", "does not matter")
            .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @DisplayName(
        """
        Given there already exists an Adventure by the name "Testadventure",
        When another Adventure should be renamned to "Testadventure",
        Then StatusCode 400 is returned
    """
    )
    @Test
    fun renameToAlreadyExistingAdventure() {
        val adventureNameToBeRenamed = "Rename me!"
        val newAdventureName = "Testadventure"
        utils.insertAdv(adventureNameToBeRenamed)
        utils.insertAdv(newAdventureName)

        patch(adventureNameToBeRenamed,  newAdventureName)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)

    }

    @DisplayName(
        """
        Given there is Adventure by the name "Testadventure",
        When the Adventure should be deleted,
        Then status 404 is returned
    """
    )
    @Test
    fun deleteNonExistentAdventure() {
        delete("Testadventure")
            .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }


    private fun put(adventureName: String): Response =
        given().with().pathParams("adventureName", adventureName)
            .`when`().put(url)

    private fun delete(newAdventureName: String): Response =
        given().with().pathParams("adventureName", newAdventureName)
            .`when`().delete(url)

    private fun patch(adventureName: String, newAdventureName: String): Response =
        given()
            .with()
            .contentType(ContentType.JSON)
            .pathParams("adventureName", adventureName)
            .body(newAdventureName)
            .`when`().patch(url)


}