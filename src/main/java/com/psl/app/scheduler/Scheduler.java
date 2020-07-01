package com.psl.app.scheduler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.psl.app.model.Constants;
import com.psl.app.repo.BlackListedTokensRepository;

@Component
public class Scheduler {
	
	@Autowired
	BlackListedTokensRepository repo;

	
	 @Scheduled(fixedDelay = Constants.ACCESS_TOKEN_VALIDITY_SECONDS)
	   public void cronJobSch() {
	      Date now = new Date();
	      repo.deleteExpiredBlackListedTokens(now);

	      
	 }

}
