package com.challenge.prewave.prewave_challenge.api.models

import jakarta.validation.constraints.NotNull

class NewEdge(
    @field:NotNull(message = "fromNode must be set")
    val fromNode: Long?,

    @field:NotNull(message = "toNode must be set")
    val toNode: Long?
)