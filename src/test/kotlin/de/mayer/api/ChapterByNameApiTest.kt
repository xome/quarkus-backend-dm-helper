package de.mayer.api

import de.mayer.persistence.AdventurePanacheRepo
import de.mayer.persistence.dto.ChapterDto
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@QuarkusTest
class ChapterByNameApiTest {

    @Inject
    lateinit var adventurePanacheRepo: AdventurePanacheRepo


    @Inject
    lateinit var utils: Utils

    val url = "/chapter/{adventureName}/{chapterName}"

    @BeforeEach
    @Transactional
    fun initData() {
        adventurePanacheRepo.deleteAll()
    }

    @DisplayName(
        """
        Given there is a Chapter by the name Testchapter,
        When it is patched and deleted,
        Then it is first renamed and then cannot be read again
    """
    )
    @Test
    fun readUpdateDelete() {
        val advDto = utils.insertAdv("Adventure")
        val oldChapterName = "Testchapter"

        val chapterDto = ChapterDto(name = oldChapterName, adventure = advDto.id)
        utils.insertChapter(chapterDto)

        val givenJson = getChapter(advDto.name!!, chapterDto.name!!)

        val expectedJson = jsonChapterOnlyWithName(chapterDto, false)

        assertThat(givenJson, `is`(expectedJson))

        chapterDto.name = "New Chapter"
        var newChapterJson = jsonChapterOnlyWithName(chapterDto, true)

        given()
            .with()
            .pathParams("adventureName", advDto.name, "chapterName", oldChapterName)
            .contentType(ContentType.JSON)
            .body(newChapterJson)
            .`when`()
            .patch(url)
            .then()
            .statusCode(HttpStatus.SC_OK)

        newChapterJson = jsonChapterOnlyWithName(chapterDto, false)
        val jsonAfterPatch = getChapter(advDto.name!!, chapterDto.name!!)

        assertThat(jsonAfterPatch, `is`(newChapterJson))

        given()
            .with()
            .pathParams("adventureName", advDto.name, "chapterName", chapterDto.name)
            .`when`()
            .delete(url)
            .then()
            .statusCode(HttpStatus.SC_OK)

        given()
            .with()
            .pathParams("adventureName", advDto.name, "chapterName", chapterDto.name)
            .`when`()
            .get(url)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)

    }
    @DisplayName("""
        Given there already exists a Chapter by the name "Testchapter"
        When another Chapter is patched to the new name "Testchapter",
        Then Status 400 is returned
    """)
    @Test
    fun patchNewNameAlreadyExists() {
        val adventure = utils.insertAdv("Testadventure")

        val chapterOne = ChapterDto()
        chapterOne.adventure = adventure.id
        chapterOne.name = "Testchapter"
        utils.insertChapter(chapterOne)

        val chapterTwo = ChapterDto()
        chapterTwo.name = "New Chapter"
        chapterTwo.adventure = adventure.id
        utils.insertChapter(chapterTwo)

        given()
            .with()
            .pathParams("adventureName", adventure.name, "chapterName", chapterTwo.name)
            .contentType(ContentType.JSON)
            .body(jsonChapterOnlyWithName(chapterOne, true))
            .`when`()
            .patch(url)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)

    }

    @DisplayName("""
        Given there is no Chapter by the name "Testchapter",
        When it is patched with a new name,
        Then status NOT_FOUND is returned
    """)
    @Test
    fun patchNonExistentChapter(){

        val adventure = utils.insertAdv("Testadventure")
        val patchChapter = ChapterDto()
        patchChapter.name = "Another Testchapter"

        given()
            .with()
            .pathParams("adventureName", adventure.name, "chapterName", "Testchapter")
            .contentType(ContentType.JSON)
            .body(jsonChapterOnlyWithName(patchChapter, true))
            .`when`()
            .patch(url)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @DisplayName("""
        Given there is no Chapter by the name "Testchapter",
        When it is to be deleted,
        Then status NOT_FOUND is returned
    """)
    @Test
    fun deleteNonExistentChapter(){
        val adventure = utils.insertAdv("Testadventure")

        given()
            .with()
            .pathParams("adventureName", adventure.name, "chapterName", "Testchapter")
            .`when`()
            .delete(url)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)

    }

    private fun jsonChapterOnlyWithName(chapterDto: ChapterDto, recordsNull: Boolean): String {
        val records = if (recordsNull) "null" else "[]"

        return """
                {"name":"${chapterDto.name}","subheader":null,"approximateDurationInMinutes":null,"records":${records}}
            """.trimIndent()
    }

    private fun getChapter(
        adventureName: String,
        chapterName: String
    ): String? = given()
        .with()
        .pathParams("adventureName", adventureName, "chapterName", chapterName)
        .`when`()
        .get(url)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().asString()


}