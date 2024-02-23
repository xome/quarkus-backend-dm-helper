package de.mayer.api

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.AdventureRepository
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure
import jakarta.transaction.Transactional
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response

@Path("/adventure/{adventureName}")
class AdventureApi(val adventureRepository: AdventureRepository) {

    @PUT
    fun putAdventure(adventureName: String): Response {
        if (adventureRepository.findByName(adventureName).isPresent)
            return Response.status(Response.Status.BAD_REQUEST).build()

        adventureRepository.save(Adventure(adventureName, emptyList()))
        return Response.ok().build()
    }

    @PATCH
    fun patchAdventure(
        adventureName: String,
        newAdventureName: String
    ): Response {
        if (adventureRepository.findByName(adventureName).isEmpty)
            return Response.status(Response.Status.NOT_FOUND).build()

        if (adventureRepository.findByName(newAdventureName).isPresent)
            return Response.status(Response.Status.BAD_REQUEST).build()

        adventureRepository.changeName(Adventure(adventureName, emptyList()), newAdventureName)
        return Response.ok().build()
    }

    @DELETE
    @Transactional
    fun deleteAdventure(adventureName: String): Response {
        if (adventureRepository.findByName(adventureName).isEmpty)
            return Response.status(Response.Status.NOT_FOUND).build()

        adventureRepository.delete(Adventure(adventureName, emptyList()))
        return Response.ok().build()
    }


}