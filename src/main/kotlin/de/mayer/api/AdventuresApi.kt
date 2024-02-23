package de.mayer.api

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.AdventureRepository
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/adventures")
class AdventuresApi(val adventureRepository: AdventureRepository) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAdventures(): Response{
        var ret = mutableListOf<String>()
        ret = adventureRepository.findAll().mapTo(ret){ adventure -> adventure.name }
        return Response.ok(ret).build()
    }

}