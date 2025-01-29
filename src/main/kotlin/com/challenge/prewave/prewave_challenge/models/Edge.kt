package com.challenge.prewave.prewave_challenge.models

import jakarta.validation.constraints.NotNull

class Edge(
    @field:NotNull(message = "fromNode must be set")
    val fromNode: Long?,

    @field:NotNull(message = "toNode must be set")
    val toNode: Long?
)