package com.challenge.prewave.prewave_challenge.api.models

data class TreeDTO(val rootNode: Int, val connections: Map<Int, List<Int>>)

