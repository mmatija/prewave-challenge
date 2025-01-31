package com.challenge.prewave.prewave_challenge.api.controllers

import com.challenge.prewave.prewave_challenge.EdgeService
import com.challenge.prewave.prewave_challenge.api.models.Tree
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
    private lateinit var edgeService: EdgeService

    @RequestMapping(value = ["/v1/tree"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteEdge(@RequestParam rootNode: Int): Tree {
        val allConnections: MutableMap<Int, List<Int>> = mutableMapOf()
        var nodesToFetch = mutableSetOf(rootNode)
        while (nodesToFetch.isNotEmpty()) {
            val connections = edgeService.getConnectedNodes(nodesToFetch.toList())
            nodesToFetch = mutableSetOf()
            connections.forEach { (k, v) ->
                allConnections[k] = v
                nodesToFetch.addAll(v)
            }
        }

        return Tree(rootNode, allConnections)
    }
}