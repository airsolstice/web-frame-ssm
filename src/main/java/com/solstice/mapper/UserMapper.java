package com.solstice.mapper;

import com.solstice.bean.User;

public interface UserMapper {
	public User findUserByEmail(String email);
	public User findUserByUserName(String userName);
	public void addUser(User user);
	public Integer findIdByCode(String activeCode);
	public void active(Integer id);
	public void updatePwd(User user);
}
