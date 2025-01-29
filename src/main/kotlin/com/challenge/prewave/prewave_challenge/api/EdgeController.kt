package com.challenge.prewave.prewave_challenge.api

import com.challenge.prewave.prewave_challenge.EdgeService
import com.challenge.prewave.prewave_challenge.api.models.Edge
import com.challenge.prewave.prewave_challenge.api.models.NewEdge
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class EdgeController {

    @Autowired
    private lateinit var edgeService: EdgeService

    @RequestMapping(value = ["/v1/edges"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createEdge(@Valid @RequestBody newEdge: NewEdge): Edge {
        return edgeService.createEdge(newEdge.fromNode!!, newEdge.toNode!!)
    }
}