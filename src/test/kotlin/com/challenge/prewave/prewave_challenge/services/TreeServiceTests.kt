package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.TreeService
import com.challenge.prewave.prewave_challenge.PersistentEdgeRepository
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
class TreeServiceTest(@Autowired edgeRepository: PersistentEdgeRepository): BaseTest() {

    val treeService = TreeService(edgeRepository = edgeRepository)

    @Test
    fun `connectNodes method returns edge between two given nodes`() {
        val result = treeService.connectNodes(1, 2)
        val expectedResult = Edge(1, 2)
        assertEquals(result, expectedResult)
    }

    @Test
    fun `connectNodes raises an error when trying to connectNodes from node to itself`() {
        assertThrows<SourceAndDestinationNodesSameException> { treeService.connectNodes(1, 1) }
    }

    @Test
    fun `connectNodes raises an error if connection between two nodes already exists`() {
        treeService.connectNodes(1, 2)
        assertThrows<EdgeAlreadyExistsException> { treeService.connectNodes(1, 2) }
    }

    @Test
    fun `disconnectNodes removes the given edge`() {
        treeService.connectNodes(1, 2)
        treeService.disconnectNodes(1,2)
        val connectedNodes = treeService.getConnectedNodes(listOf(1))
        assertEquals(emptyMap(), connectedNodes)
    }

    @Test
    fun `disconnectNodes raises an exception if edge does not exist`() {
        assertThrows<EdgeDoesNotExistException> { treeService.disconnectNodes(1,2) }
    }

    @Test
    fun `getConnectedNodes returns all connections of depth 1 for given nodes`() {
        treeService.connectNodes(1, 2)
        treeService.connectNodes(1, 3)
        treeService.connectNodes(2, 4)
        treeService.connectNodes(2, 5)
        treeService.connectNodes(3, 6)
        val node1 = 1
        val node2 = 2
        val node4 = 4
        val connectedNodes = treeService.getConnectedNodes(rootNodes = listOf(node1, node2, node4));
        val expectedResult = mapOf(node1 to listOf(2, 3), node2 to listOf(4, 5))
        assertEquals(expectedResult, connectedNodes)
    }
}
