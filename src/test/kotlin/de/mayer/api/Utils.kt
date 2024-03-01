package de.mayer.api

import de.mayer.persistence.AdventurePanacheRepo
import de.mayer.persistence.ChapterRepositoryWithPanache
import de.mayer.persistence.dto.AdventureDto
import de.mayer.persistence.dto.ChapterDto
import io.restassured.RestAssured.given
import io.restassured.common.mapper.TypeRef
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus

@ApplicationScoped
class Utils(val adventurePanacheRepo: AdventurePanacheRepo,
    val chapterRepositoryWithPanache: ChapterRepositoryWithPanache) {
    fun getAllAdventures(): List<String> {
        val response: List<String> =
            given()
                .`when`().get("/adventures")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().body().`as`(object : TypeRef<List<String>>() {})
        return response
    }

    @Transactional
    fun insertAdv(name: String) : AdventureDto{
        val adv = AdventureDto()
        adv.name = name
        adventurePanacheRepo.persist(adv)
        return adventurePanacheRepo.find("name", name).firstResult<AdventureDto>()
    }

    @Transactional
    fun insertChapter(chapterDto: ChapterDto) {
        chapterRepositoryWithPanache.persist(chapterDto)
    }

}