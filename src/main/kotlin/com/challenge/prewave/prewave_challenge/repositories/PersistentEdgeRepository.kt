package com.challenge.prewave.prewave_challenge.repositories

import com.challenge.prewave.prewave_challenge.tables.Edge.Companion.EDGE
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PersistentEdgeRepository(private val dslContext: DSLContext) : EdgeRepository {
    override fun create(fromId: Int, toId: Int) {
        dslContext.insertInto(EDGE, EDGE.FROM_ID, EDGE.TO_ID).values(fromId, toId).execute()
    }

    override fun delete(fromId: Int, toId: Int): Boolean {
        val result = dslContext.deleteFrom(EDGE).where(EDGE.FROM_ID.equal(fromId)).and(EDGE.TO_ID.equal(toId)).execute()
        return result == 1
    }

    override fun findByFromIds(ids: Collection<Int>): Map<Int, List<Int>> {
        val results = dslContext.select(EDGE).from(EDGE).where(EDGE.FROM_ID.`in`(ids)).fetch()
        val connections = mutableMapOf<Int, MutableList<Int>>()
        results.forEach { result ->
            val fromId = result.value1().fromId!!
            val toId = result.value1().toId!!
            if (connections[fromId].isNullOrEmpty()) {
                connections[fromId] = mutableListOf()
            }
            connections[fromId]?.add(toId)
        }
        return connections
    }

}