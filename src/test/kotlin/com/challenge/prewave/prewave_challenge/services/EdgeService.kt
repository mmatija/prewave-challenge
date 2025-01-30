package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.EdgeService
import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.models.Edge
import com.challenge.prewave.prewave_challenge.tables.Edge.Companion.EDGE
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class EdgeServiceTest: BaseTest() {

    @Autowired
    lateinit var edgeService: EdgeService

    @Test
    fun `calling createEdge method stores the edge information in the database`() {
        edgeService.createEdge(1, 2)
        val result = dslContext.fetch(EDGE)
        assertEquals(result.size, 1)
        val createdEdge = result.first()
        assertEquals(createdEdge.fromId, 1L)
        assertEquals(createdEdge.toId, 2L)
    }

    @Test
    fun `returns created edge`() {
        val result = edgeService.createEdge(1, 2)
        val expectedResult = Edge(1, 2)
        assertTrue { result == expectedResult }
    }

    @Test
    fun `raises an error when edge already exists`() {
        edgeService.createEdge(1, 2)
        assertThrows<EdgeAlreadyExistsException> { edgeService.createEdge(1, 2) }
    }
}
