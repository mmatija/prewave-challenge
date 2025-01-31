package com.challenge.prewave.prewave_challenge.api.controllers

import com.challenge.prewave.prewave_challenge.services.TreeService
import com.challenge.prewave.prewave_challenge.api.models.TreeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TreeController {

    @Autowired
    private lateinit var treeService: TreeService

    @RequestMapping(value = ["/v1/tree"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteEdge(@RequestParam rootNode: Int): TreeDTO {
        val allConnections = treeService.getTree(rootNode)
        return TreeDTO(rootNode, allConnections)
    }
}