package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.EdgeService
import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.models.Edge
import com.challenge.prewave.prewave_challenge.tables.references.EDGE
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class EdgeServiceTest: BaseTest() {

    @Autowired
    lateinit var edgeService: EdgeService

    @Test
    fun `createEdge method stores the edge information in the database`() {
        edgeService.createEdge(1, 2)
        val result = dslContext.fetch(EDGE)
        assertEquals( 1, result.size)
        val createdEdge = result.first()
        assertEquals( 1, createdEdge.fromId)
        assertEquals( 2, createdEdge.toId)
    }

    @Test
    fun `createEdge method returns created edge`() {
        val result = edgeService.createEdge(1, 2)
        val expectedResult = Edge(1, 2)
        assertEquals(result, expectedResult)
    }

    @Test
    fun `createEdge method raises an error when edge already exists`() {
        edgeService.createEdge(1, 2)
        assertThrows<EdgeAlreadyExistsException> { edgeService.createEdge(1, 2) }
    }

    @Test
    fun `deleteEdge removes the edge from the database`() {
        edgeService.createEdge(1, 2)
        edgeService.deleteEdge(1,2)
        val result = dslContext.fetch(EDGE)
        assertEquals( 0, result.size)
    }

    @Test
    fun `deleteEdge raises an exception if edge does not exist`() {
        assertThrows<EdgeDoesNotExistException> { edgeService.deleteEdge(1,2) }
    }

}
