
import java.util.Date;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.pojo.TbUser;
import com.taotao.service.UserService;

public class UserTest {
	
	
	@Test
	public void test(){
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
		UserService userServiceImpl = (UserService) ac.getBean("userService");
		TbUser tbUser = new TbUser();
		tbUser.setUsername("taotaotest005");
		tbUser.setPassword("1233453");
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		userServiceImpl.insertUser(tbUser);
		ac.close();
	}
}
