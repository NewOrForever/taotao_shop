package com.taotao.sso.service;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	TaoTaoResult checkRegisterData(String data, int type);
	TaoTaoResult register(TbUser user);
	TaoTaoResult userLogin(String username, String password);
	TaoTaoResult getUserByToken(String token);
	TaoTaoResult userLoginOut(String token);
	
}
