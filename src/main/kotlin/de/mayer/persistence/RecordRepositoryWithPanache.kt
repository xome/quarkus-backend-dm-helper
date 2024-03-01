package de.mayer.persistence

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.RecordRepository
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure
import de.mayer.penandpaperdmhelperjcore.adventure.model.Chapter
import de.mayer.penandpaperdmhelperjcore.adventure.model.RecordInAChapter
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

@ApplicationScoped
class RecordRepositoryWithPanache(
): RecordRepository {
    override fun create(adventure: Adventure, chapter: Chapter, record: RecordInAChapter, index: Int) {
        TODO("Not yet implemented")
    }

    override fun create(adventure: String, chapterName: String, index: Int, record: RecordInAChapter) {
        TODO("Not yet implemented")
    }

    override fun create(adventure: Adventure, chapter: Chapter, records: MutableList<RecordInAChapter>) {
        TODO("Not yet implemented")
    }

    override fun read(adventureName: String, chapterName: String): LinkedList<RecordInAChapter> {
        return LinkedList()
    }

    override fun read(adventure: String, chapter: String, index: Int): Optional<RecordInAChapter> {
        TODO("Not yet implemented")
    }

    override fun update(adventure: String, chapterName: String, index: Int, record: RecordInAChapter) {
        TODO("Not yet implemented")
    }

    override fun delete(adventure: String, chapter: String) {
        TODO("Not yet implemented")
    }

    override fun delete(adventure: String, chapter: String, index: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteChapterLinksReferencing(adventureName: String, chapterName: String) {
        TODO("Not yet implemented")
    }
}