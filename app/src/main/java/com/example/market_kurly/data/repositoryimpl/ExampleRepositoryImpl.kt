package com.example.market_kurly.domain.repositoryimpl

import com.example.market_kurly.data.service.ExampleService
import com.example.market_kurly.domain.model.SignUpModel
import com.example.market_kurly.domain.model.toSignUpModel
import com.example.market_kurly.domain.repository.ExampleRepository

class ExampleRepositoryImpl(
    private val exampleService: ExampleService,
) : ExampleRepository {
    override suspend fun signUp(request: SignUpModel): Result<String> = runCatching {
        val request = toSignUpModel(request)
        val response = exampleService.signUp(request)
        response.message
    }
}
