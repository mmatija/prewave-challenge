package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.errors.SourceAndDestinationNodesSameException
import com.challenge.prewave.prewave_challenge.api.errors.UnexpectedException
import com.challenge.prewave.prewave_challenge.models.Edge
import com.challenge.prewave.prewave_challenge.repositories.EdgeRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class TreeService(val edgeRepository: EdgeRepository) {

    fun connectNodes(fromNode: Int, toNode: Int): Edge {
        if (fromNode == toNode) {
            throw SourceAndDestinationNodesSameException("Destination node cannot be the same as source node")
        }
        try {
            edgeRepository.create(fromNode, toNode)
            return Edge(fromNode, toNode)
        } catch (ex: DuplicateKeyException) {
            throw EdgeAlreadyExistsException("Edge from $fromNode to $toNode already exists")
        } catch (ex: Exception) {
            throw UnexpectedException("An unexpected error happened")
        }
    }

    fun disconnectNodes(fromNode: Int, toNode: Int) {
        val result: Boolean
        try {
            result = edgeRepository.delete(fromNode, toNode)
        } catch (ex: Exception) {
            throw UnexpectedException("An unexpected error happened")
        }
        if (!result) {
            throw EdgeDoesNotExistException("Edge from node $fromNode to node $toNode does not exist")
        }
    }

    fun getTree(rootNodeId: Int): Map<Int, List<Int>> {
        try {
            val allConnections: MutableMap<Int, List<Int>> = mutableMapOf()
            var nodesToFetch = mutableSetOf(rootNodeId)
            while (nodesToFetch.isNotEmpty()) {
                val nonVisitedNodes = nodesToFetch.filter { n -> !allConnections.containsKey(n) }
                val connections = edgeRepository.findByFromIds(nonVisitedNodes)
                nodesToFetch = mutableSetOf()
                connections.forEach { (k, v) ->
                    allConnections[k] = v
                    nodesToFetch.addAll(v)
                }
            }
            return allConnections
        }  catch (ex: Exception) {
            throw UnexpectedException("An unexpected error happened")
        }
    }
}