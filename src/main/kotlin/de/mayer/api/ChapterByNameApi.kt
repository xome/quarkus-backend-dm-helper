package de.mayer.api

import de.mayer.penandpaperdmhelperjcore.adventure.model.Chapter
import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.ChapterRepository
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response

@Path("/chapter/{adventureName}/{chapterName}")
class ChapterByNameApi(val chapterRepository: ChapterRepository) {

    @GET
    fun getChapter(adventureName: String,
                   chapterName: String): Response {

        val optChapter = chapterRepository.read(adventureName, chapterName)

        if (optChapter.isEmpty){
            return Response.status(Response.Status.NOT_FOUND).build()
        }

        return Response.ok(optChapter.get()).build()
    }

    @PATCH
    fun patchChapter(adventureName: String,
                     chapterName: String,
                     chapter: Chapter): Response {
        chapterRepository.update(adventureName, chapterName, chapter)
        return Response.ok().build()
    }

    @DELETE
    fun deleteChapter(adventureName: String,
                      chapterName: String): Response {
        chapterRepository.delete(adventureName, chapterName)
        return Response.ok().build()
    }

}