package com.taotao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper tbUserMapper;

	@Override
	public void insertUser(TbUser user) {
		tbUserMapper.insert(user);
	}

}
