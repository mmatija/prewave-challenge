package com.challenge.prewave.prewave_challenge.api.controllers

import com.challenge.prewave.prewave_challenge.TreeService
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
        val allConnections: MutableMap<Int, List<Int>> = mutableMapOf()
        var nodesToFetch = mutableSetOf(rootNode)
        while (nodesToFetch.isNotEmpty()) {
            val connections = treeService.getConnectedNodes(nodesToFetch.filter { n -> !allConnections.containsKey(n) }.toList())
            nodesToFetch = mutableSetOf()
            connections.forEach { (k, v) ->
                allConnections[k] = v
                nodesToFetch.addAll(v)
            }
        }

        return TreeDTO(rootNode, allConnections)
    }
}