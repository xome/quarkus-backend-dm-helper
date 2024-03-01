package de.mayer.api

import de.mayer.persistence.AdventurePanacheRepo
import de.mayer.persistence.dto.AdventureDto
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@QuarkusTest
class AdventuresApiTest {

    @Inject
    lateinit var repo: AdventurePanacheRepo

    @Inject
    lateinit var utils: Utils

    @BeforeEach
    @Transactional
    fun initData() {
        repo.deleteAll()
    }

    @DisplayName(
        """
        Given there are Adventures with the names "A" and "B",
        When Api Get is called,
        Then they both are returned
    """
    )
    @Test
    fun getAllAdventures() {

        val adventure2 = AdventureDto()
        adventure2.name = "B"
        insertAdventure(adventure2)

        val adventure1 = AdventureDto()
        adventure1.name = "A"
        insertAdventure(adventure1)

        val retrievedBody = utils.getAllAdventures()

        assertThat(retrievedBody, `is`(listOf("A", "B")))

    }

    @Transactional
    fun insertAdventure(adventure: AdventureDto) {
        repo.persistAndFlush(adventure)
    }

}