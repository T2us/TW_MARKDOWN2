package com.pcwk.ehr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pcwk.ehr.cmn.SearchVO;
import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;
@RunWith(SpringJUnit4ClassRunner.class) // Junit의 기능을 Spring으로 확대시킴.
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"}) // ContextConfiguration을 통해 applicationContext.xml을 로딩하여 공유
public class JunitUserDaoTest {
	final Logger LOG = LogManager.getLogger(this.getClass());
	
	@Autowired // Context Framework는 변수 타입과 일치하는 Context 내의 Bean을 찾고, 변수에 주입(할당)
	ApplicationContext context;
	
	@Autowired
	UserDao dao;
	UserVO user01;
	UserVO user02;
	UserVO user03;
	
	SearchVO searchVO;
	
	@Before
	public void setUp() throws Exception{
		LOG.debug("============================");
		LOG.debug("=0. setUp()=");
		LOG.debug("============================");
		
		searchVO = new SearchVO(10, 1, "", "");
		
		user01 = new UserVO("p09", "김태욱09", "4321", Level.BASIC, 1, 0, "zzxsx484@gmail.com", "날짜_사용_안_함");
		user02 = new UserVO("p090", "김태욱090", "4321", Level.SILVER, 50, 2, "zzxsx484@gmail.com", "날짜_사용_안_함");
		user03 = new UserVO("p0900", "김태욱0900", "4321", Level.GOLD, 100, 31, "zzxsx484@gmail.com", "날짜_사용_안_함");	
		
		LOG.debug("context: "+context);
		LOG.debug("dao: "+dao);
		
		assertNotNull(context);
		assertNotNull(dao);
	}
	
	@Test
	public void idCheck() throws SQLException {
		/* idCheck 테스트 시나리오
		 * 1. 기존 데이터 삭제
		 * 2. 한 건 입력
		 * 3. idCheck()
		 */
		
		// [1] 기존 데이터 삭제
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		assertEquals(0, dao.getCount(user01));
		
		// [2] 신규 등록: user01
		dao.doInsert(user01);
		assertEquals(1, dao.getCount(user01));
		
		// [3] idCheck
		int flag = dao.idCheck(user01);
		assertEquals(1, flag);
		assertEquals(0, dao.idCheck(user02));
		
	}
	
	@Test
	@Ignore
	public void doRetrieve() throws SQLException{
//		searchVO.setSearchDiv("30");
//		searchVO.setSearchWord("tubus");
		List<UserVO> list = dao.doRetrieve(searchVO);
		for(UserVO vo: list) LOG.debug("vo= "+vo);
	}
	
	@Test
	@Ignore
	public void doDelete() throws SQLException{
		/* doUpdate() 테스트 시나리오
		 * 1. 전체 삭제 
		 * 2. 신규 등록: user01
		 * 3. 한 건 조회: user01
		 * 4. 데이터 삭제
		 */
		LOG.debug("============================");
		LOG.debug("=4. doDelete()=");
		LOG.debug("============================");
		
		// [1] 전체 삭제
//		dao.deleteAll();
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		assertEquals(0, dao.getCount(user01));
		
		// [2] 신규 등록: user01
		dao.doInsert(user01);
		assertEquals(1, dao.getCount(user01));
		
		// [3] 한 건 조회 및 비교
		UserVO vsVO = dao.doSelectOne(user01);
		isSameUser(vsVO, user01);
		
		// [4] 데이터 삭제
		dao.doDelete(user01);
		assertEquals(0, dao.getCount(user01));
	}
	
	@Test
	@Ignore
	public void doUpdate() throws SQLException{
		/* doUpdate() 테스트 시나리오
		 * 1. 전체 삭제 => 내 데이터만 삭제
		 * 2. 신규 등록: user01
		 * 3. 한 건 조회: user01
		 * 4. 조회된 데이터 수정: user01_U
		 * 5. 수정 쿼리 수행
		 * 6. 한 건 조회 비교
		 */
		LOG.debug("============================");
		LOG.debug("=3. doUpdate()=");
		LOG.debug("============================");
		
		// [1] 전체 삭제
//		dao.deleteAll();
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		assertEquals(0, dao.getCount(user01));
		
		// [2] 신규 등록: user01
		dao.doInsert(user01);
		assertEquals(1, dao.getCount(user01));
		
		// [3] 한 건 조회: user01
		UserVO vsVO = dao.doSelectOne(user01);
		isSameUser(vsVO, user01);
		
		// [4] 조회된 데이터 수정: user01_U
		String upStr = "_U";
		int upInt    = 10;
		
		user01.setName(user01.getName()+upStr);
		user01.setPasswd(user01.getPasswd()+upStr);
		user01.setName(user01.getName()+upStr);
		user01.setEmail(user01.getEmail()+upStr);
		
		user01.setLogin(user01.getLogin()+upInt);
		user01.setRecommend(user01.getRecommend()+upInt);
		user01.setLevel(Level.SILVER);
		
		// [5] 수정 쿼리 수정
		dao.doUpdate(user01);
		
		// [6] 한 건 조회 비교
		vsVO = dao.doSelectOne(user01);
		isSameUser(vsVO, user01);
	}
	
	@Test
	@Ignore
	public void getAll() throws SQLException {
		/* getAll() 테스트 시나리오
		 * 1. 전체 삭제 
		 * 2. 데이터 3건 각각 입력
		 * 3. 전체 건수 조회 = 3건
		 */
		LOG.debug("============================");
		LOG.debug("=2. addAndGet()=");
		LOG.debug("============================");
		
		// [1] 전체 삭제
//		dao.deleteAll();
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		assertEquals(0, dao.getCount(user01));
		// [2] 데이터 3건 각각 입력
		//     [2-1] 데이터 1건 입력 후 1건이 입력됐는지 assertEquals로 검사
		dao.doInsert(user01);
		assertEquals(1, dao.getCount(user01));

		//     [2-2] 데이터 총 2건 입력 후 총 2건이 입력됐는지 assertEquals로 검사
		dao.doInsert(user02);
		assertEquals(2, dao.getCount(user01));
		
		//     [2-2] 데이터 총 3건 입력 후 총 3건이 입력됐는지 assertEquals로 검사
		dao.doInsert(user03);
		assertEquals(3, dao.getCount(user01));
		
		// [3] 전체 건수 조회
		//     - getAll의 반환값은 list으로 반환되고, 3건이 제대로 들어갔음을 확인했으니 메소드가 제대로 실행되는지 확인하려면 list.size와 예측값 3이 동일한 값이어야 한다.
		List<UserVO> list = dao.getAll(user01);
		assertEquals(3, list.size());
		
	}
	
	@Test
	@Ignore
	public void addAndGet() {
		LOG.debug("============================");
		LOG.debug("=1. addAndGet()=");
		LOG.debug("============================");
		
		try {
			// 전체 삭제
//			dao.deleteAll();
			dao.doDelete(user01);
			dao.doDelete(user02);
			dao.doDelete(user03);
			assertEquals(0, dao.getCount(user01));
			
			// 1건 추가
			dao.doInsert(user01);
			assertEquals(1,  dao.getCount(user01));
			
			// 1건 추가
			dao.doInsert(user02);
			assertEquals(2,  dao.getCount(user01));
			
			// 단건 조회
			UserVO vsUser01 = dao.doSelectOne(user01);
			isSameUser(vsUser01, user01);
		} catch (SQLException e) {
			LOG.debug("--------------------------------");
			LOG.debug("-SQLException- " + e.getMessage());
			LOG.debug("--------------------------------");
		}	
	}
	
	private void isSameUser(UserVO vsVO, UserVO orgVO) {
		assertEquals(vsVO.getuId(), orgVO.getuId());
		assertEquals(vsVO.getName(), orgVO.getName());
		assertEquals(vsVO.getPasswd(), orgVO.getPasswd());
		
		assertEquals(vsVO.getLevel(), orgVO.getLevel());
		assertEquals(vsVO.getLogin(), orgVO.getLogin());
		assertEquals(vsVO.getRecommend(), orgVO.getRecommend());
		assertEquals(vsVO.getEmail(), orgVO.getEmail());
//		assertEquals(vsVO.getRegDt(), orgVO.getRegDt());
	}
	
//	@Test(expected = NullPointerException.class)
//	@Ignore
//	public void getFailure() throws SQLException{
//		LOG.debug("============================");
//		LOG.debug("=2. getFailure()=");
//		LOG.debug("============================");
//		
//		dao.deleteAll();
//		
//		dao.get(user01);
//	}
	


	@After
	public void tearDown() throws Exception{
		LOG.debug("============================");
		LOG.debug("=9. tearDown()=");
		LOG.debug("============================");
	}
}