package com.example.simpledemo

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.PactTestExecutionContext
import au.com.dius.pact.consumer.dsl.LambdaDsl
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit.ConsumerPactTest
import au.com.dius.pact.consumer.junit.PactProviderRule
import au.com.dius.pact.consumer.junit.PactVerification
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import groovy.util.GroovyTestCase.assertEquals
import org.junit.Rule
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConsumerPactTest : ConsumerPactTest() {
    @get:Rule
    internal val rule: PactProviderRule
        get() = PactProviderRule("spring-provider-api", this)

    @Pact(consumer = "android-consumer-app", provider = "spring-provider-api")
    override fun createPact(builder: PactDslWithProvider): RequestResponsePact =
        builder
            .given("user does not exist")
            .uponReceiving("post user with request body")
            .method("POST")
            .path("/v1/users")
            .matchHeader("Authorization", "Bearer [a-zA-Z0-9=\\+/]+", "Bearer AAABd9yHUjI=")
            .body(LambdaDsl.newJsonBody {
                it.stringType("firstName", "first1")
                it.stringType("lastName", "last1")
                it.stringType("address", "addr1")
            }.build())
            .willRespondWith()
            .status(200)
            .body(
                LambdaDsl.newJsonBody {
                    it.stringType("id", "8de28ede-a489-4fff-b190-e59a7ce395b8")
                    it.stringType("firstName", "first1")
                    it.stringType("lastName", "last1")
                    it.stringType("address", "addr1")
                }.build()
            )
            .toPact()

    override fun providerName(): String = "spring-provider-api"

    override fun consumerName(): String = "android-consumer-app"

    @PactVerification("spring-provider-api", fragment = "pactPostUser")
    override fun runTest(mockServer: MockServer, context: PactTestExecutionContext) {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockServer.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ClientApi::class.java)

        kotlinx.coroutines.test.runTest {
            println("runTest POST /users to get full list")
            var userResponse = UserResponse("8de28ede-a489-4fff-b190-e59a7ce395b8","first1", "last1", "addr1")

            var response: Response<UserResponse> = api.addUser(UserRequest("first1","last1","addr1"),"Bearer AAAAABCDE")
            assertEquals(200, response.code())
            assertEquals(userResponse, response.body())

        }
    }
}

