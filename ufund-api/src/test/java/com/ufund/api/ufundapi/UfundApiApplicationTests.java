/*
package com.ufund.api.ufundapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class UfundApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void getAllNeedsEmpty(){
		NeedController need = new NeedController();
		assert((need.getAllNeeds().getBody().isEmpty()) && (need.getAllNeeds().getStatusCode()==HttpStatus.OK));
	}

	@Test
	void getAllNeedsSingular(){
		NeedController need = new NeedController();
		need.createNeed(new Need(0,"TEST", 0.0, 0, "NULL"));
		assert((!need.getAllNeeds().getBody().isEmpty()) && (need.getAllNeeds().getStatusCode()==HttpStatus.OK));
	}

	@Test
	void getAllNeedsVerifyData(){
		NeedController need = new NeedController();
		Need test = new Need(0,"TEST", 0.0, 0, "NULL");
		need.createNeed(test);
		assert((need.getAllNeeds().getBody().contains(test)) && (need.getAllNeeds().getStatusCode()==HttpStatus.OK));
	}

}
*/