package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.models.Edge
import com.challenge.prewave.prewave_challenge.tables.references.EDGE
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class EdgeService(private val create: DSLContext) {



    fun createEdge(fromNode: Long, toNode: Long): Edge {
        try {
            create.insertInto(EDGE, EDGE.FROM_ID, EDGE.TO_ID).values(fromNode, toNode).execute()
            return Edge(fromNode = fromNode, toNode = toNode)
        } catch (ex: DuplicateKeyException) {
            throw EdgeAlreadyExistsException("Edge from $fromNode to $toNode already exists")
        }
    }
}