package com.solstice.service;

import com.solstice.bean.User;
import com.solstice.exception.UserException;

public interface UserService {
	public void findUserByUserName(String userName) throws UserException;
	public void findUserByEmail(String email) throws UserException;
	public void addUser(User user)throws UserException;
	public Integer findIdByCode(String activeCode)throws UserException;
	public void active(Integer id)throws UserException;
	public User login(User user)throws UserException;
	public void updatePwd(String email,String pwd)throws UserException;
}