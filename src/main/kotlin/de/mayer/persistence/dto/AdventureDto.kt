package de.mayer.persistence.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "adventure")
class AdventureDto() {

    @get: GeneratedValue
    @get: Id
    var id: Long? = null

    var name: String? = null

}
