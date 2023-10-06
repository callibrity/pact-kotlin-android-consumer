package com.example.simpledemo

//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.PactTestExecutionContext
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
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


class UserQueryPactTest : ConsumerPactTest() {
    @get:Rule
    internal val rule: PactProviderRule
        get() = PactProviderRule("spring-provider-api", this)

    @Pact(consumer = "android-consumer-app", provider = "spring-provider-api")
    override fun createPact(builder: PactDslWithProvider): RequestResponsePact =
        builder
            .given("users exist")
            .uponReceiving("get list of users")
            .method("GET")
            .path("/v1/users")
            .matchHeader("Authorization", "Bearer [a-zA-Z0-9=\\+/]+", "Bearer AAABd9yHUjI=")
            .willRespondWith()
            .status(200)
            .body(PactDslJsonBody()
                .minArrayLike("users", 3, 3)
                .stringType("id", "ABC-125")
                .stringType("firstName", "second")
                .stringType("lastName", "secondLast")
                .stringType("address", "addr2")
            )
            .given("user with firstName=first exists")
            .uponReceiving("get list of users with firstName=first")
            .method("GET")
            .path("/v1/users")
            .matchQuery("firstName", "[a-zA-Z\\+/]+", "first")
//            .matchQuery("lastName", "[a-zA-Z\\+/]+", "last")
            .matchHeader("Authorization", "Bearer [a-zA-Z0-9=\\+/]+", "Bearer AAABd9yHUjI=")
            .willRespondWith()
            .status(200)
            .body(
                PactDslJsonBody()
                    .minArrayLike("users", 1, 1)
                    .stringType("id", "ABC-123")
                    .stringType("firstName", "first")
                    .stringType("lastName", "last")
                    .stringType("address", "addr")
            )
            .given("user with firstName=first and lastName=last exists")
            .uponReceiving("get list of users with firstName=first and lastName=last")
            .method("GET")
            .path("/v1/users")
            .matchQuery("firstName", "[a-zA-Z\\+/]+", "first")
            .matchQuery("lastName", "[a-zA-Z\\+/]+", "last")
            .matchHeader("Authorization", "Bearer [a-zA-Z0-9=\\+/]+", "Bearer AAABd9yHUjI=")
            .willRespondWith()
            .status(200)
            .body(
                PactDslJsonBody()
                    .minArrayLike("users", 1, 1)
                    .stringType("id", "ABC-123")
                    .stringType("firstName", "first")
                    .stringType("lastName", "last")
                    .stringType("address", "addr")
            )
            .toPact()

    override fun providerName(): String = "spring-provider-api"

    override fun consumerName(): String = "android-consumer-app"

    @PactVerification("spring-provider-api", fragment = "pactGetUserByFirstNameQuery")
    override fun runTest(mockServer: MockServer, context: PactTestExecutionContext) {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockServer.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ClientApi::class.java)
        println("test get user by query param")


        kotlinx.coroutines.test.runTest {
            println("runTest firstName as query parameter")
            var resultArray = ArrayList<UserResponse>()
            var userResponse = UserResponse("ABC-123","first", "last", "addr")
            resultArray.add(userResponse)
            var responseList: Response<UserResponseList> = api.getUsers("first","Bearer AAAAABCDE")

            assertEquals(200, responseList.code())
            assertEquals(resultArray, responseList.body()!!.users)
        }

        kotlinx.coroutines.test.runTest {
            println("runTest firstName and lastName as query parameter")
            var resultArray = ArrayList<UserResponse>()
            var userResponse = UserResponse("ABC-123","first", "last", "addr")
            resultArray.add(userResponse)
            var responseList: Response<UserResponseList> = api.getUsers("first","last","Bearer AAAAABCDE")

            assertEquals(200, responseList.code())
            assertEquals(resultArray, responseList.body()!!.users)
        }

        kotlinx.coroutines.test.runTest {
            println("runTest /users to get full list")
            var resultArray = ArrayList<UserResponse>()
            var userResponse = UserResponse("ABC-125","second", "secondLast", "addr2")
            resultArray.add(userResponse)
            resultArray.add(userResponse)
            resultArray.add(userResponse)

            var responseList: Response<UserResponseList> = api.getUsers("Bearer AAAAABCDE")
            assertEquals(200, responseList.code())
            assertEquals(resultArray, responseList.body()!!.users)

        }
    }



}

