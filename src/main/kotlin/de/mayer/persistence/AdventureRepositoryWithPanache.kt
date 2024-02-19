package de.mayer.persistence

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.AdventureRepository
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure
import de.mayer.persistence.dto.AdventureDto
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

@ApplicationScoped
class AdventureRepositoryWithPanache(val adventurePanacheRepo: AdventurePanacheRepo): AdventureRepository {
    override fun findByName(p0: String?): Optional<Adventure> {
        TODO("Not yet implemented")
    }

    override fun save(p0: Adventure?) {
        TODO("Not yet implemented")
    }

    override fun changeName(p0: Adventure?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Adventure?) {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableList<Adventure> {
        var ret = mutableListOf<Adventure>()
        ret = adventurePanacheRepo
                .findAll(Sort.ascending("name"))
                .list<AdventureDto>()
                .mapTo(ret) { dto -> Adventure(dto.name, emptyList()) }
        return ret
    }
}