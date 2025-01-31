package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.EdgeService
import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.errors.SourceAndDestinationNodesSameException
import com.challenge.prewave.prewave_challenge.models.Edge
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
    fun `createEdge raises an error when trying to create an edge from node to itself`() {
        assertThrows<SourceAndDestinationNodesSameException> { edgeService.createEdge(1, 1) }
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

    @Test
    fun `getConnectedNodes returns all connections of depth 1 for given nodes`() {
        edgeService.createEdge(1, 2)
        edgeService.createEdge(1, 3)
        edgeService.createEdge(2, 4)
        edgeService.createEdge(2, 5)
        edgeService.createEdge(3, 6)
        val node1 = 1
        val node2 = 2
        val node4 = 4
        val connectedNodes = edgeService.getConnectedNodes(rootNodes = listOf(node1, node2, node4));
        val expectedResult = mapOf(node1 to listOf(2, 3), node2 to listOf(4, 5), node4 to listOf())
        assertEquals(expectedResult, connectedNodes)
    }
}
