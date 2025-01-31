package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.errors.SourceAndDestinationNodesSameException
import com.challenge.prewave.prewave_challenge.models.Edge
import com.challenge.prewave.prewave_challenge.tables.references.EDGE
import org.jooq.DSLContext
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class EdgeService(private val dslContext: DSLContext) {

    fun createEdge(fromNode: Int, toNode: Int): Edge {
        if (fromNode == toNode) {
            throw SourceAndDestinationNodesSameException("Destination node cannot be the same as source node")
        }
        try {
            dslContext.insertInto(EDGE, EDGE.FROM_ID, EDGE.TO_ID).values(fromNode, toNode).execute()
            return Edge(fromNode = fromNode, toNode = toNode)
        } catch (ex: DuplicateKeyException) {
            throw EdgeAlreadyExistsException("Edge from $fromNode to $toNode already exists")
        }
    }

    fun deleteEdge(fromNode: Int, toNode: Int) {
        val result = dslContext.deleteFrom(EDGE).where(EDGE.FROM_ID.equal(fromNode)).and(EDGE.TO_ID.equal(toNode)).execute()
        if (result == 0) {
            throw EdgeDoesNotExistException("Edge from node $fromNode to node $toNode does not exist")
        }
    }

    fun getConnectedNodes(rootNodes: List<Int>): Map<Int, List<Int>> {
        val result = dslContext.select(EDGE).from(EDGE).where(EDGE.FROM_ID.`in`(rootNodes)).fetch()
        val connectedNodes = rootNodes.associateWith { emptyList<Int>().toMutableList() }.toMutableMap()
        result.forEach { r -> connectedNodes[r.value1().fromId]?.add(r.value1().toId!!) }
        return connectedNodes
    }
}