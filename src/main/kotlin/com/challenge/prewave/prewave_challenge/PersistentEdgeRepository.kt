package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.models.Edge
import com.challenge.prewave.prewave_challenge.tables.Edge.Companion.EDGE
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PersistentEdgeRepository(private val dslContext: DSLContext) : EdgeRepository {
    override fun create(fromId: Int, toId: Int): Edge {
        dslContext.insertInto(EDGE, EDGE.FROM_ID, EDGE.TO_ID).values(fromId, toId).execute()
        return Edge(fromId, toId)
    }

    override fun delete(fromId: Int, toId: Int): Boolean {
        val result = dslContext.deleteFrom(EDGE).where(EDGE.FROM_ID.equal(fromId)).and(EDGE.TO_ID.equal(toId)).execute()
        return result == 1
    }

    override fun findByFromIds(ids: List<Int>): List<Edge> {
        val result = dslContext.select(EDGE).from(EDGE).where(EDGE.FROM_ID.`in`(ids)).fetch()
        return result.map { r -> Edge(r.value1().fromId!!, r.value1().toId!!) }
    }

}