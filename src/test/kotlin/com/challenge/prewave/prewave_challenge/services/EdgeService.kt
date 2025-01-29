package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.EdgeService
import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.models.Edge
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.junit.jupiter.api.AfterEach
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
        val result = dslContext.select().from("edge").fetch()
        assertEquals(result.size, 1)
        assertEquals(result.first().getValue(field("from_id")), 1L)
        assertEquals(result.first().getValue(field("to_id")), 2L)
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
