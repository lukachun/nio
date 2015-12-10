package com.chungang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class Client {

	@Autowired
//	@Qualifier("handlerA")
	IHandler iHandler;
	
	IHandler getHander() {
		return iHandler;
	}
}
