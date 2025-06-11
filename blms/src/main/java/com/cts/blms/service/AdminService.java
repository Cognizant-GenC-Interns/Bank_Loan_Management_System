package com.cts.blms.service;

import com.cts.blms.entity.Admin;

public interface AdminService {

	boolean validateAdmin(String email, String password);

	Admin findAdmin(String email);

}
