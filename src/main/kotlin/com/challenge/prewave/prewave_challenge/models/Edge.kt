package com.challenge.prewave.prewave_challenge.models

import jakarta.validation.constraints.NotBlank

class Edge(
    @field:NotBlank(message = "fromNode must be set")
    val fromNode: String?,

    @field:NotBlank(message = "toNode must be set")
    val toNode: String?
)