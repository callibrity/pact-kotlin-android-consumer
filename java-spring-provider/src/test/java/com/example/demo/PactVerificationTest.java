package com.example.demo;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.apache.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("spring-provider-api")
@PactBroker
public class PactVerificationTest {
  @LocalServerPort
  private int port;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup(PactVerificationContext context) {
    context.setTarget(new HttpTestTarget("localhost", port));
  }

  @TestTemplate
  @ExtendWith(PactVerificationSpringProvider.class)
  void pactVerificationTestTemplate(PactVerificationContext context, HttpRequest request) {
    // WARNING: Do not modify anything else on the request, because you could invalidate the contract
    if (request.containsHeader("Authorization")) {
      request.setHeader("Authorization", "Bearer " + generateToken());
    }
    context.verifyInteraction();
  }

  private static String generateToken() {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(System.currentTimeMillis());
    return Base64.getEncoder().encodeToString(buffer.array());
  }


  @State(value = "user does not exist", action = StateChangeAction.SETUP)
  void addUser() {
	  UUID uuid = UUID.fromString("8de28ede-a489-4fff-b190-e59a7ce395b9");
	  Optional<User> user = userRepository.findById(uuid);
	  if(user.isPresent()) {
		  userRepository.delete(user.get());
	  }

  }
  @State(value = "user with ID 8de28ede-a489-4fff-b190-e59a7ce395b8 exists", action = StateChangeAction.SETUP)
  void getUser(Map<String, Object> params) {
    String userId = params.get("id").toString();
    UUID uuid = UUID.fromString("8de28ede-a489-4fff-b190-e59a7ce395b8");
    Optional<User> user = userRepository.findById(uuid);
    System.out.println("adding user...: 8de28ede-a489-4fff-b190-e59a7ce395b8 = "+ userId);
    if (!user.isPresent()) {
    	User u = new User(uuid, "first1", "last1", "addr1");
    	userRepository.save(u);
    }
    

  }

  @State(value = "users exist", action = StateChangeAction.SETUP)
  void usersExists() {
//	  userRepository.deleteAll();
	  userRepository.saveAll(Arrays.asList(
			  new User("second", "secondLast", "addr2"),
			  new User("second", "secondLast", "addr2"),
			  new User("second", "secondLast", "addr2")));
  }
  
  @State(value = "user with firstName=first exists", action = StateChangeAction.SETUP)
  void usersWithParamExists() {
	  userRepository.saveAll(Arrays.asList(
			  new User("first", "first", "addr")));
  }
  
  @State(value = "user with firstName=first and lastName=last exists", action = StateChangeAction.SETUP)
  void usersWithParam2Exists() {
	  userRepository.saveAll(Arrays.asList(
			  new User("first", "first", "addr")));
  }
}
