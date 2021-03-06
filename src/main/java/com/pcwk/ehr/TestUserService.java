package com.pcwk.ehr;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserServiceImpl;

public class TestUserService extends UserServiceImpl {
	final Logger LOG = LogManager.getLogger(getClass());
	
	private String uId; // 사용자 ID

	public TestUserService() {}
	
	public TestUserService(String uId) {
		super();
		this.uId = uId;
		
		LOG.debug("==========================================");
		LOG.debug("this.uId = "+ this.uId);
		LOG.debug("==========================================");
	}

	/* 테스트 시나리오
	 * 5명의 사용자, 등업 대상자는 2번째 4번째에 있음.
	 * 4번째 사용자에서 강제로 예외 발생
	 */
	@Override
	public void upgradeLevel(UserVO user) throws SQLException {	
		
		if(this.uId.equals(user.getuId())) {
			LOG.debug("===================================");
			LOG.debug("=upgradeLevel=");
			LOG.debug("===================================");
			
			throw new TestUserServiceException("TestUserServiceException: "+uId);
		}
		super.upgradeLevel(user);
	}
}