package com.testeintegracao.testes.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice
class HandleValidationsExceptions {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<List<String>>{
        var erros: MutableList<String> = ex.bindingResult
            .allErrors.stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList())

        return ResponseEntity(erros, HttpStatus.BAD_REQUEST)
    }

}