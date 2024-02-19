package de.mayer.persistence

import de.mayer.persistence.dto.AdventureDto
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AdventurePanacheRepo: PanacheRepository<AdventureDto> {

}
