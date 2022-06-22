package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.TestUserServiceException;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

@Service("userService")
public class UserServiceImpl implements UserService {

	final Logger LOG = LogManager.getLogger(this.getClass());

	// 상수 도입: 30, 50
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50; // BASIC에서 SILVER로 가는 최소 로그인 수 
	public static final int MIN_RECOMMEND_FOR_GOLD = 30; // SILVER에서 GOLD로 가는 최소 추천 수
	
	@Autowired
	private UserDao userDao;
	
	// mail
	@Autowired
	@Qualifier("dummyMailSender")
	private MailSender mailSender;
	
	public UserServiceImpl() {
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	public int pwCheck(UserVO inVO) throws SQLException {
		return this.userDao.pwCheck(inVO);
	}

	@Override
	public int idCheck(UserVO inVO) throws SQLException {
		return this.userDao.idCheck(inVO);
	}

	@Override
	public List<UserVO> doRetrieve(DTO dto) throws SQLException {
		return this.userDao.doRetrieve(dto);
	}

	@Override
	public int doDelete(UserVO inVO) throws SQLException {
		return this.userDao.doDelete(inVO);
	}

	@Override
	public UserVO doSelectOne(UserVO inVO) throws SQLException {
		return this.userDao.doSelectOne(inVO);
	}

	@Override
	public int doInsert(UserVO inVO) throws SQLException {
		return this.userDao.doInsert(inVO);
	}

	@Override
	public int doUpdate(UserVO inVO) throws SQLException {
		return this.userDao.doUpdate(inVO);
	}

	@Override
	public int add(UserVO inVO) throws SQLException {
		if (inVO.getLevel() == null) inVO.setLevel(Level.BASIC);
		return this.userDao.doInsert(inVO);
	}

	@Override
	public void upgradeLevels(UserVO inVO) throws SQLException {
		try {
			List<UserVO> list = userDao.getAll(inVO);

			// 50회 이상 로그인 시: BASIC -> SILVER
			// 30번 이상 추천을 받을 시: SILVER -> GOLD
			// GOLD는 대상이 아님
			for(UserVO user : list) {
				if(canUpgradeLevel(user) == true) {
					upgradeLevel(user);
				}
			}
		} catch (TestUserServiceException e) {
			LOG.debug("============================");
			LOG.debug("=TestUserServiceException="+e.getMessage());
			LOG.debug("============================");
			throw e;
		}
	}
	
	/**
	 * User가 업그레이드 대상인지 확인
	 * @param user
	 * @return 대상이 맞을 시: true/ 대상이 아니면: false
	 */
	private boolean canUpgradeLevel(UserVO user) {
		Level currentLevel = user.getLevel();
		
		switch (currentLevel) {
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			
			default: throw new IllegalAccessError("Unknown Level: "+currentLevel);
		}
	}
	
	/**
	 * 레벨 업그레이드 작업
	 * @param user
	 * @throws SQLException 
	 */
	public void upgradeLevel(UserVO user) throws SQLException {
		// p09000 값이 검출되면 강제로 Rollback!
//		if("p09000".equals(user.getuId())) {
//			throw new TestUserServiceException("트랜잭션 테스트: "+user.getuId());
//		}
		user.upgradeLevel();
		this.userDao.doUpdate(user);
		// 등업되면 mail전송
		sendupgradeMail(user);
	}
	
	/**
	 * 등업되면 메일 전송
	 * BASIC -> SILVER: 2번째
	 * SILVER -> GOLD: 4번째
	 * @param user
	 */
	public void sendupgradeMail(UserVO user) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(user.getEmail()); // 받는 사람 이메일
		message.setFrom("xodnr571@naver.com"); // 보내는 사람 이메일
		message.setSubject("등업 안내"); 
		message.setText("사용자의 등급이 "+user.getLevel().name()+"로 업그레이드 되었습니다.");
		
		mailSender.send(message);
//		try {
//			MimeMessage mail = mailSender.createMimeMessage();
//			MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");
//			
//			LOG.debug("-----------------------------");
//			LOG.debug("-sendupgradeMail-" + user.getEmail());
//			LOG.debug("-----------------------------");
//			
//			mailHelper.setFrom("xodnr571@naver.com");
//			mailHelper.setTo(user.getEmail());
//			mailHelper.setSubject("등업 안내");
//			mailHelper.setText("사용자의 등급이 "+user.getLevel().name()+"로 업그레이드 되었습니다.");
//			
//			
//		} catch (MessagingException e) {
//			LOG.debug("=============================");
//			LOG.debug("=MessagingException=" + e.getMessage());
//			LOG.debug("=============================");
//		}
		
	}
}