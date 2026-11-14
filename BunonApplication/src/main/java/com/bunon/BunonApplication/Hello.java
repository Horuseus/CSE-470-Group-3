package com.bunon.BunonApplication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

	public String greet() {
		return "Welcome to Bunon, the largest online platform for embracing and connecting with the Bengali culture";
	}

}
