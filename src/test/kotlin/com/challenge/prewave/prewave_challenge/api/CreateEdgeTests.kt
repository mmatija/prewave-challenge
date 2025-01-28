package com.challenge.prewave.prewave_challenge.api

import com.challenge.prewave.prewave_challenge.models.Edge
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CreateEdgeTests(@Autowired val restTemplate: TestRestTemplate) {


    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Returns status code 200`() {
        val body = edgeAsJson(fromNode = "1", toNode = "2")
        sendPostRequest(body).andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `Returns created edge`() {
        val body = edgeAsJson(fromNode =  "1",  toNode = "2")
        sendPostRequest(body).andExpect {
            content { json(body) }
        }
    }

    @Test
    fun `Returns bad request when fromNode value is missing`() {
        val body = """{"fromNode": "1"}"""
        sendPostRequest(body).andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Returns error message when fromNode is not set`() {
        val body = """{"toNode": "2"}"""
        val expectedErrorMessage = """{"fromNode": "fromNode must be set"}"""
        sendPostRequest(body).andExpect {
            content { json(expectedErrorMessage) }
        }
    }

    @Test
    fun `Returns error message when toNode is not set`() {
        val body = """{"fromNode": "1"}"""
        val expectedErrorMessage = """{"toNode": "toNode must be set"}"""
        sendPostRequest(body).andExpect {
            content { json(expectedErrorMessage) }
        }
    }

    fun sendPostRequest(jsonBody: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/edges") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonBody
        }
    }

    fun edgeAsJson(fromNode: String, toNode: String): String {
        return """{"fromNode": "${fromNode}", "toNode": "${toNode}"}"""
    }



}