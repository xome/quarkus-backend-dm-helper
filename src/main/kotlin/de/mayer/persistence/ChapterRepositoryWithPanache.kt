package de.mayer.persistence

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.ChapterRepository
import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.RecordRepository
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure
import de.mayer.penandpaperdmhelperjcore.adventure.model.Chapter
import de.mayer.persistence.dto.AdventureDto
import de.mayer.persistence.dto.ChapterDto
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.util.*

@ApplicationScoped
class ChapterRepositoryWithPanache(
    val adventurePanacheRepo: AdventurePanacheRepo,
    val recordRepository: RecordRepository
) : PanacheRepository<ChapterDto>, ChapterRepository {
    override fun create(adventureName: String, chapter: Chapter) {
        TODO("Not yet implemented")
    }

    override fun read(adventureName: String, chapterName: String): Optional<Chapter> {
        val optAdventureDto = adventurePanacheRepo.find("name", adventureName)
            .firstResultOptional<AdventureDto>()

        if (optAdventureDto.isEmpty)
            return Optional.empty()

        val optChapter = find("adventure = ?1 and name = ?2", optAdventureDto.get().id, chapterName)
            .firstResultOptional<ChapterDto>()

        if (optChapter.isEmpty)
            return Optional.empty()

        val chapterDto = optChapter.get()

        return Optional.of(
            Chapter(
                chapterDto.name,
                chapterDto.subheader,
                chapterDto.approximateDurationInMinutes,
                recordRepository.read(adventureName, chapterName)
            )
        )

    }

    @Transactional
    override fun update(adventureName: String, chapterName: String, chapter: Chapter) {
        val adventureDto = adventurePanacheRepo.find("name", adventureName)
            .firstResultOptional<AdventureDto>()

        val chapterDto = find("adventure = ?1 and name = ?2", adventureDto.get().id, chapterName)
            .firstResult<ChapterDto>()


        if (chapter.name != null && !chapter.name.equals(chapterDto.name))
            chapterDto.name = chapter.name
        if (chapter.subheader != null && !chapter.subheader.equals(chapterDto.subheader))
            chapterDto.subheader = chapter.subheader
        if (chapter.approximateDurationInMinutes != null &&
            !chapter.approximateDurationInMinutes.equals(chapterDto.approximateDurationInMinutes)
        )
            chapterDto.approximateDurationInMinutes = chapter.approximateDurationInMinutes

        if (chapter.records != null){
            recordRepository.delete(adventureName, chapterName)
            recordRepository.create(Adventure(adventureName, emptyList()), chapter, chapter.records)
        }

    }

    @Transactional
    override fun delete(adventureName: String, chapterName: String) {
        val adventureDto = adventurePanacheRepo.find("name", adventureName).firstResultOptional<AdventureDto>()
        delete("adventure = ?1 and name = ?2", adventureDto.get().id, chapterName)
    }
}