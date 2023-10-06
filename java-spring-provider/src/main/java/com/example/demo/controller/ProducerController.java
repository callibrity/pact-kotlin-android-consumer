package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.BlogInfo;
import com.example.demo.domain.User;
import com.example.demo.domain.UserServiceResponse;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("v1")
public class ProducerController {
	
	@Autowired
	private UserRepository userRepository;

	  @GetMapping("/users")
	  public UserServiceResponse all(@RequestParam(required = false) String firstName) {
		  System.out.println("getting all users...");
		  UserServiceResponse resp = new UserServiceResponse();
		  resp.setUsers(userRepository.findAll());
	    return resp;
	  }
	  
	  @GetMapping("/users/{id}")
	  @ResponseBody
	  public User getUser (@PathVariable() String id) {
		  System.out.println("getting user: " + id);
		  UUID uuid = UUID.fromString(id);
		  User result = null;
		  
		  if(!userRepository.findById(uuid).isEmpty() ) {
			  result = userRepository.findById(uuid).get();
			  System.out.println(result.toString());
		  }
		  
		  
	    return result;
	  }
	  
	  @GetMapping("/blogs/{id}")
	  @ResponseBody
	  public BlogInfo getBlog (@PathVariable() String id) {
		  System.out.println("getting blog: " + id);
//		  UUID uuid = UUID.fromString(id);
		  BlogInfo result = new BlogInfo("wonderful day", "today is a really nice day");
		  System.out.println(result.toString());

	    return result;
	  }
	  
	  @PostMapping("/users")
	  public User addUser (@RequestBody User request) {
		  System.out.println("adding user: " + request.toString());
		  System.out.println(request.getFirstName() + " " + request.getLastName()+ " " + request.getAddress());
		  
		  return userRepository.save(request);
		  
	  }
	  
	  @DeleteMapping("/users/{id}")
	  public void deleteUser(@PathVariable() String id) {
		  UUID uuid = UUID.fromString(id);
		  userRepository.deleteById(uuid);
	  }
	  
	  @GetMapping("/blogs")
	  public List<BlogInfo> allBlogs() {
		  System.out.println("getting all blogs...");
		  List<BlogInfo> blogs = new ArrayList<BlogInfo>();
		  blogs.add(new BlogInfo("wonderful day","today is a great day..."));
		  blogs.add(new BlogInfo("meh day", "today is a meh day..."));
	    return blogs;
	  }
}
