package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.errors.SourceAndDestinationNodesSameException
import com.challenge.prewave.prewave_challenge.models.Edge
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class EdgeService(val edgeRepository: PersistentEdgeRepository) {

    fun createEdge(fromNode: Int, toNode: Int): Edge {
        if (fromNode == toNode) {
            throw SourceAndDestinationNodesSameException("Destination node cannot be the same as source node")
        }
        try {
            return edgeRepository.create(fromNode, toNode)
        } catch (ex: DuplicateKeyException) {
            throw EdgeAlreadyExistsException("Edge from $fromNode to $toNode already exists")
        }
    }

    fun deleteEdge(fromNode: Int, toNode: Int) {
        val result = edgeRepository.delete(fromNode, toNode)
        if (!result) {
            throw EdgeDoesNotExistException("Edge from node $fromNode to node $toNode does not exist")
        }
    }

    fun getConnectedNodes(rootNodes: List<Int>): Map<Int, List<Int>> {
        val result = edgeRepository.findByFromIds(rootNodes)
        val connectedNodes = rootNodes.associateWith { emptyList<Int>().toMutableList() }.toMutableMap()
        result.forEach { r -> connectedNodes[r.fromNode]?.add(r.toNode) }
        return connectedNodes
    }
}