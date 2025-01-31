package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.errors.SourceAndDestinationNodesSameException
import com.challenge.prewave.prewave_challenge.models.Edge
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class TreeService(val edgeRepository: PersistentEdgeRepository) {

    fun connectNodes(fromNode: Int, toNode: Int): Edge {
        if (fromNode == toNode) {
            throw SourceAndDestinationNodesSameException("Destination node cannot be the same as source node")
        }
        try {
            return edgeRepository.create(fromNode, toNode)
        } catch (ex: DuplicateKeyException) {
            throw EdgeAlreadyExistsException("Edge from $fromNode to $toNode already exists")
        }
    }

    fun disconnectNodes(fromNode: Int, toNode: Int) {
        val result = edgeRepository.delete(fromNode, toNode)
        if (!result) {
            throw EdgeDoesNotExistException("Edge from node $fromNode to node $toNode does not exist")
        }
    }

    fun getTree(rootNodeId: Int): Map<Int, List<Int>> {
        val allConnections: MutableMap<Int, List<Int>> = mutableMapOf()
        var nodesToFetch = mutableSetOf(rootNodeId)
        while (nodesToFetch.isNotEmpty()) {
            val connections = edgeRepository.findByFromIds(nodesToFetch.filter { n -> !allConnections.containsKey(n) }.toList())
            nodesToFetch = mutableSetOf()
            connections.forEach { (k, v) ->
                allConnections[k] = v
                nodesToFetch.addAll(v)
            }
        }
        return allConnections
    }
}