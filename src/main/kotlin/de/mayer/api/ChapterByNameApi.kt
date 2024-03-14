package de.mayer.api

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.ChapterAlreadyExistsException
import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.ChapterNotFoundException
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
        try {
            chapterRepository.update(adventureName, chapterName, chapter)
        } catch (_: ChapterAlreadyExistsException){
            return Response.status(Response.Status.BAD_REQUEST).build()
        } catch (_: ChapterNotFoundException){
            return Response.status(Response.Status.NOT_FOUND).build()
        }
        return Response.ok().build()
    }

    @DELETE
    fun deleteChapter(adventureName: String,
                      chapterName: String): Response {
        try {
            chapterRepository.delete(adventureName, chapterName)
        } catch (_: ChapterNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
        return Response.ok().build()
    }

}