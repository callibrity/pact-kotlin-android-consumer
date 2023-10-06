//package com.example.simpledemo
//
//import au.com.dius.pact.consumer.dsl.PactDslWithProvider
//import au.com.dius.pact.core.model.annotations.Pact
//import au.com.dius.pact.core.model.annotations.PactFolder
//import org.junit.runner.RunWith
//
////@PactFolder("target/pacts")
//@RunWith(JUnitParamsRunner.class)
//class ContractTest {
//    @Pact(provider = "user-provider-service", consumer = "user-consume-service")
//    fun userPact(builder: PactDslWithProvider): RequestResponsePact {
//        val responseBody = LambdaDsl.newJsonBody { user ->
//            user.stringType("name", "someName")
//            user.stringType("age", "20")
//            user.stringType("lastName", "someLastName")
//        }
//
//        return builder
//            .given("a user is present")
//            .uponReceiving("a request to get user")
//            .pathFromProviderState("/user", "/user")
//            .method("GET")
//            .willRespondWith()
//            .body(responseBody.build())
//            .toPact()
//    }
//
//    @Test
//    fun `should return user`(mockServer: MockServer) {
//        val url = mockServer.getUrl() + "/user"
//
////        val user = UserClient().getUser(url)
//
////        assertTrue(user.hasProperty("age"))
////        assertTrue(user.hasProperty("name"))
////        assertTrue(user.hasProperty("lastName"))
//    }
//}