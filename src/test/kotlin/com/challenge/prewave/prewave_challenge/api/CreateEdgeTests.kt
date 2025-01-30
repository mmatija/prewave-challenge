package com.challenge.prewave.prewave_challenge.api

import com.challenge.prewave.prewave_challenge.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CreateEdgeTests(@Autowired val mockMvc: MockMvc) : BaseTest() {

    @Test
    fun `Returns status code 200`() {
        val body = edgeAsJson(fromNode = 1, toNode = 2)
        sendPostRequest(body).andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `Returns created edge`() {
        val body = edgeAsJson(fromNode =  1,  toNode = 2)
        sendPostRequest(body).andExpect {
            content { json(body) }
        }
    }

    @Test
    fun `Returns bad request when fromNode value is missing`() {
        val body = """{"fromNode": 1}"""
        sendPostRequest(body).andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Returns error message when fromNode is not set`() {
        val body = """{"toNode": 2}"""
        val expectedErrorMessage = """{"errors": ["fromNode must be set"]}"""
        sendPostRequest(body).andExpect {
            content { json(expectedErrorMessage) }
        }
    }

    @Test
    fun `Returns error message when toNode is not set`() {
        val body = """{"fromNode": 1}"""
        val expectedErrorMessage = """{"errors": ["toNode must be set"]}"""
        sendPostRequest(body).andExpect {
            content { json(expectedErrorMessage) }
        }
    }

    @Test
    fun `Returns both error messages when neither fromNode nor toNode are set`() {
        val body = "{}"
        val expectedErrorMessage = """{"errors": ["fromNode must be set", "toNode must be set"]}"""
        sendPostRequest(body).andExpect {
            content { json(expectedErrorMessage) }
        }
    }

    @Test
    fun `Returns status code 422 when edge already exists`() {
        val body = edgeAsJson(fromNode = 1, toNode = 2)
        sendPostRequest(body)
        sendPostRequest(body).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `Returns error message when edge already exists`() {
        val body = edgeAsJson(fromNode = 1, toNode = 2)
        val expectedErrorMessage = """{"errors": ["Edge from 1 to 2 already exists"]}"""
        sendPostRequest(body)
        sendPostRequest(body).andExpect {
            content { json(expectedErrorMessage) }
        }
    }

    @Test
    fun `Returns status code 422 when source and destination node are the same`() {
        val body = edgeAsJson(fromNode = 1, toNode = 1)
        sendPostRequest(body).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    fun sendPostRequest(jsonBody: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/edges") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonBody
        }
    }

    fun edgeAsJson(fromNode: Int, toNode: Int): String {
        return """{"fromNode": ${fromNode}, "toNode": ${toNode}}"""
    }
}