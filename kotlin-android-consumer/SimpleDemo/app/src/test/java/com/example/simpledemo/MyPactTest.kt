package com.example.simpledemo

//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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


/* MyPactTest.kt */
class MyPactTest : ConsumerPactTest() {
    @get:Rule
    internal val rule: PactProviderRule
        get() = PactProviderRule("spring-provider-api", this)

    @Pact(consumer = "android-consumer-app", provider = "spring-provider-api")
    override fun createPact(builder: PactDslWithProvider): RequestResponsePact =
        builder
            .given("user with ID 8de28ede-a489-4fff-b190-e59a7ce395b8 exists", "id", "8de28ede-a489-4fff-b190-e59a7ce395b8")
            .uponReceiving("get user with ID 8de28ede-a489-4fff-b190-e59a7ce395b8")
            .method("GET")
            .path("/v1/users/8de28ede-a489-4fff-b190-e59a7ce395b8")
            .matchHeader("Authorization", "Bearer [a-zA-Z0-9=\\+/]+", "Bearer AAABd9yHUjI=")
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

    @PactVerification("spring-provider-api", fragment = "pactGetUserById")
    override fun runTest(mockServer: MockServer, context: PactTestExecutionContext) {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockServer.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ClientApi::class.java)

        kotlinx.coroutines.test.runTest {

            println("runTest GET UserById")
            var userResponse = UserResponse("8de28ede-a489-4fff-b190-e59a7ce395b8","first1", "last1", "addr1")
            var responseList: Response<UserResponse> = api.getUser("8de28ede-a489-4fff-b190-e59a7ce395b8","Bearer AAAAABCDE")

            assertEquals(200, responseList.code())
            assertEquals(userResponse.id, responseList.body()!!.id)
            assertEquals(userResponse.firstName, responseList.body()!!.firstName)

        }
    }
}

