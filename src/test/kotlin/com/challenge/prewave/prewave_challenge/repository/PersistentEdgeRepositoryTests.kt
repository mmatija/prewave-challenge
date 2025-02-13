package com.challenge.prewave.prewave_challenge.repository

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.repositories.PersistentEdgeRepository
import com.challenge.prewave.prewave_challenge.tables.Edge.Companion.EDGE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
class PersistentEdgeRepositoryTests: BaseTest() {

    @Autowired
    lateinit var edgeRepositroy: PersistentEdgeRepository

    @Test
    fun `create method stores the edge in the database`() {
        edgeRepositroy.create(1, 2)
        val result = dslContext.fetch(EDGE)
        assertEquals( 1, result.size)
        val createdEdge = result.first()
        assertEquals( 1, createdEdge.fromId)
        assertEquals( 2, createdEdge.toId)
    }

    @Test
    fun `createEdge method raises an error when edge already exists`() {
        edgeRepositroy.create(1, 2)
        assertThrows<DuplicateKeyException> { edgeRepositroy.create(1, 2) }
    }

    @Test
    fun `deleteEdge removes the edge from the database`() {
        edgeRepositroy.create(1, 2)
        edgeRepositroy.delete(1,2)
        val result = dslContext.fetch(EDGE)
        assertEquals( 0, result.size)
    }

    @Test
    fun `deleteEdge returns true if the row was deleted`() {
        edgeRepositroy.create(1, 2)
        assertTrue { edgeRepositroy.delete(1,2) }
    }

    @Test
    fun `deleteEdge returns false if the row does not exist`() {
        assertFalse { edgeRepositroy.delete(1,2) }
    }

    @Test
    fun `findByFromIds returns all edges where fromId column matches provided ids`() {
        edgeRepositroy.create(1, 2)
        edgeRepositroy.create(1, 3)
        edgeRepositroy.create(2, 4)
        edgeRepositroy.create(3, 4)
        val result = edgeRepositroy.findByFromIds(ids = listOf(1, 2, 5))
        val expectedResult = mapOf(1 to listOf(2, 3), 2 to listOf(4))
        assertEquals(expectedResult, result)
    }

    @Test
    fun `findByFromIds returns empty map if there are no matching results`() {
        val result = edgeRepositroy.findByFromIds(ids = listOf(1))
        assertEquals(emptyMap<Int, List<Int>>(), result)
    }
}

