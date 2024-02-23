package de.mayer.persistence

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.AdventureRepository
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure
import de.mayer.persistence.dto.AdventureDto
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.util.*

@ApplicationScoped
class AdventureRepositoryWithPanache(val adventurePanacheRepo: AdventurePanacheRepo) : AdventureRepository {
    override fun findByName(adventureName: String): Optional<Adventure> {
        val dtoOpt = adventurePanacheRepo.find("name", adventureName)
            .singleResultOptional<AdventureDto>()

        if (dtoOpt.isEmpty) return Optional.empty()

        return Optional.of(Adventure(dtoOpt.get().name, emptyList()))

    }

    @Transactional
    override fun save(adventure: Adventure) {
        val adventureDto = AdventureDto()
        adventureDto.name = adventure.name
        adventurePanacheRepo.persist(adventureDto)
    }

    @Transactional
    override fun changeName(adventure: Adventure, newName: String) {
        val adventureDto = adventurePanacheRepo.find("name", adventure.name).firstResult<AdventureDto>()
        adventureDto.name = newName
        adventurePanacheRepo.persist(adventureDto)
    }

    @Transactional
    override fun delete(adventure: Adventure) {
        adventurePanacheRepo.delete("name", adventure.name)
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