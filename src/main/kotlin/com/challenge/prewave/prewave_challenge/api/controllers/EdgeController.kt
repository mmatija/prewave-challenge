package com.challenge.prewave.prewave_challenge.api.controllers

import com.challenge.prewave.prewave_challenge.services.TreeService
import com.challenge.prewave.prewave_challenge.api.models.EdgeDTO
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class EdgeController {

    @Autowired
    private lateinit var treeService: TreeService

    @RequestMapping(value = ["/v1/edges"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createEdge(@Valid @RequestBody edgeDTO: EdgeDTO): EdgeDTO {
        treeService.connectNodes(edgeDTO.fromNode!!, edgeDTO.toNode!!)
        return edgeDTO
    }

    @RequestMapping(value = ["/v1/edges"], method = [RequestMethod.DELETE], produces = [MediaType.TEXT_PLAIN_VALUE])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteEdge(@RequestParam fromNode: Int, @RequestParam toNode: Int) {
        treeService.disconnectNodes(fromNode, toNode)
    }
}