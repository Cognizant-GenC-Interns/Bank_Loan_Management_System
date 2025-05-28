package com.cts.blms.service;

import com.cts.blms.model.Admin;

public interface AdminService {

	boolean validateAdmin(String email, String password);

	Admin findAdmin(String email);
	
	

}
