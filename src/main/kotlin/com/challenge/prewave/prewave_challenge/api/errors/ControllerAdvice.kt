package com.challenge.prewave.prewave_challenge.api.errors

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, List<String>>> {
        val errors: MutableList<String> = mutableListOf()
        ex.bindingResult.allErrors.forEach { error ->
            val errorMessage = error.defaultMessage
            errors.addLast(errorMessage)
        }
        val responseBody = mapOf("errors" to errors)
        return ResponseEntity(responseBody, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingParameterException(ex: MissingServletRequestParameterException): ResponseEntity<Map<String, List<String>>> {
        val errors = listOf("${ex.parameterName} must be set")
        val responseBody = mapOf("errors" to errors)
        return ResponseEntity(responseBody, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EdgeAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleEdgeAlreadyExistsException(ex: EdgeAlreadyExistsException): ResponseEntity<Map<String, List<String>>> {
        val errors = listOf(ex.message!!)
        val responseBody = mapOf("errors" to errors)
        return ResponseEntity(responseBody, HttpStatus.UNPROCESSABLE_ENTITY)
    }


    @ExceptionHandler(EdgeDoesNotExistException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleEdgeDoesNotExistException(ex: EdgeDoesNotExistException): ResponseEntity<Map<String, List<String>>> {
        val errors = listOf(ex.message!!)
        val responseBody = mapOf("errors" to errors)
        return ResponseEntity(responseBody, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(SourceAndDestinationNodesSameException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleSourceAndDestinationNodeSameException(ex: SourceAndDestinationNodesSameException): ResponseEntity<Map<String, List<String>>> {
        val errors = listOf(ex.message!!)
        val responseBody = mapOf("errors" to errors)
        return ResponseEntity(responseBody, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}