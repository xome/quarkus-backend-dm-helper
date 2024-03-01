package de.mayer.persistence.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "adventure")
data class AdventureDto(

    @Id @GeneratedValue var id: Long? = null,
    var name: String? = null

)
