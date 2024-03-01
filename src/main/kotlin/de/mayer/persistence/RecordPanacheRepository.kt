package de.mayer.persistence

import de.mayer.persistence.dto.RecordDto
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecordPanacheRepository: PanacheRepository<RecordDto> {
}
