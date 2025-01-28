package com.challenge.prewave.prewave_challenge.api

import com.challenge.prewave.prewave_challenge.models.Edge
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class EdgeController {

    @RequestMapping(value = ["/v1/edges"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createEdge(@Valid @RequestBody edge: Edge): Edge {
        return edge
    }
}