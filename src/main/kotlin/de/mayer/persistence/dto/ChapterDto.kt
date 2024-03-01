package de.mayer.persistence.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "chapter")
data class ChapterDto(

    @Id @GeneratedValue var id: Long? = null,
    var name: String? = null,
    var adventure: Long? = null,
    var subheader: String? = null,
    var approximateDurationInMinutes: Int? = null,

    )
