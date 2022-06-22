package com.pcwk.ehr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.cmn.SearchVO;
import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"}) // ContextConfiguration을 통해 applicationContext.xml을 로딩하여 공유
public class JunitUserControllerTest {
	final Logger LOG = LogManager.getLogger(this.getClass());
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	@Autowired
	UserDao dao;
	UserVO user01;
	UserVO user02;
	UserVO user03;
	
	SearchVO searchVO;
	
	// 브라우저 대역(Mock)
	MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		LOG.debug("============================");
		LOG.debug("=0. setUp()=");
		LOG.debug("============================");
		
		searchVO = new SearchVO(10, 1, "", "");
		
		user01 = new UserVO("p09", "김태욱09", "4321", Level.BASIC, 1, 0, "zzxsx484@gmail.com", "날짜_사용_안_함");
		user02 = new UserVO("p090", "김태욱090", "4321", Level.SILVER, 50, 2, "zzxsx484@gmail.com", "날짜_사용_안_함");
		user03 = new UserVO("p0900", "김태욱0900", "4321", Level.GOLD, 100, 31, "zzxsx484@gmail.com", "날짜_사용_안_함");
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		LOG.debug("webApplicationContext: "+webApplicationContext);
		LOG.debug("mockMvc: "+mockMvc);
		
		assertNotNull(mockMvc);
		assertNotNull(webApplicationContext);
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
	
	@Test 
	public void idCheck() throws Exception {
		/* idCheck() 테스트 시나리오
		 * 1. 기존 데이터 삭제
		 * 2. 한 건 입력
		 * 3. idCheck()
		 */
		
		// [1] 기존 데이터 삭제
		doDelete(user01);
		doDelete(user02);
		doDelete(user03);
		assertEquals(0, dao.getCount(user01));
				
		// [2] 신규 데이터 등록
		add(user01);
		assertEquals(1, dao.getCount(user01));
		
		// [3] idCheck()
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doRetrieve.do
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/idCheck.do")
				.param("uId", user01.getuId());
		
		// 대역 객체를 통해 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();

		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		MessageVO messageVO = new Gson().fromJson(result, MessageVO.class);
		
		LOG.debug("============================");
		LOG.debug("=messageVO="+messageVO);
		LOG.debug("============================");	
		
		assertEquals("1", messageVO.getMsgId());
	}
	
	@Test 
	public void pwCheck() throws Exception {
		/* pwCheck() 테스트 시나리오 => userID가 같으면서 passwd가 같으면 1 출력
		 * 1. 기존 데이터 삭제
		 * 2. 한 건 입력
		 * 3. pwCheck()
		 */
		
		// [1] 기존 데이터 삭제
		doDelete(user01);
		doDelete(user02);
		doDelete(user03);
		assertEquals(0, dao.getCount(user01));
				
		// [2] 신규 데이터 등록
		add(user01);
		assertEquals(1, dao.getCount(user01));
		
		// [3] pwCheck()
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doRetrieve.do
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/pwCheck.do")
				.param("uId", user01.getuId())
				.param("passwd", user01.getPasswd());
		
		// 대역 객체를 통해 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();

		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		MessageVO messageVO = new Gson().fromJson(result, MessageVO.class);
		
		LOG.debug("============================");
		LOG.debug("=messageVO="+messageVO);
		LOG.debug("============================");	
		
		assertEquals("1", messageVO.getMsgId());
	}
	
	@Test
	@Ignore
	public void doRetrieve() throws Exception{
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doRetrieve.do
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/doRetrieve.do")
				.param("pageSize", String.valueOf(searchVO.getPageSize()))
				.param("pageNum", String.valueOf(searchVO.getPageNum()))
				.param("searchDiv", searchVO.getSearchDiv())
				.param("searchWord", searchVO.getSearchWord());
				
		// 대역 객체를 통해 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();

		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		// jsonString to VO
		Gson gson = new Gson();
		// gson List<UserVO> jsonString -> List<UserVO>로 변경
		List<UserVO> list = gson.fromJson(result, new TypeToken<List<UserVO>>() {}.getType());
		
		LOG.debug("============================");
		for(UserVO outVO : list) LOG.debug("=outVO="+outVO);
		LOG.debug("============================");	
	}
	
	@Test
	@Ignore
	public void doUpdate() throws Exception{
		/* addAndGet() 테스트 시나리오
		 * 1. 기존 데이터 삭제
		 * 2. 신규 데이터 등록
		 * 3. 단건 데이터 수정
		 * 4. 등록 데이터와 비교
		 */
		// [1] 기존 데이터 삭제
		doDelete(user01);
		doDelete(user02);
		doDelete(user03);
		assertEquals(0, dao.getCount(user01));
		
		// [2] 신규 데이터 등록
		add(user01);
		assertEquals(1, dao.getCount(user01));
		add(user02);
		assertEquals(2, dao.getCount(user01));
		add(user03);
		assertEquals(3, dao.getCount(user01));
		
		// [3] 단건 데이터 수정
		String modifyStr = "_U";
		
		UserVO outVO01 = doSelectOne(user01);
		isSameUser(outVO01, user01);
		
		user01.setName(user01.getName()+modifyStr);
		user01.setPasswd(user01.getPasswd()+modifyStr);
		user01.setLevel(Level.GOLD);
		user01.setLogin(user01.getLogin()+10);
		user01.setRecommend(user01.getRecommend()+10);
		user01.setEmail(user01.getEmail()+modifyStr);
		
		// [4] 등록 데이터와 비교
		
		// ----- 메소드 구현부 ------------------------------------------
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doUpdate.do
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/doUpdate.do")
				.param("uId", user01.getuId())
				.param("name", user01.getName())
				.param("passwd", user01.getPasswd())
				.param("intLevel", user01.getIntLevel()+"")//int 값을 string으로 만드는 가장 쉬운 방법
				.param("login", user01.getLogin()+"")
				.param("recommend", user01.getRecommend()+"")
				.param("email", user01.getEmail())
				.param("regDt", user01.getRegDt());
		
		// 대역 객체를 통해 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();

		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		// jsonString to VO
		Gson gson = new Gson();
		MessageVO messageVO = gson.fromJson(result, MessageVO.class);
		
		LOG.debug("============================");
		LOG.debug("=messageVO="+messageVO);
		LOG.debug("============================");	
		// ------- 메소드 구현부 -----------------------------------
		assertEquals("1", messageVO.getMsgId());
		
		UserVO vsVO01 = doSelectOne(user01);
		isSameUser(vsVO01, user01);
	}
	
	@Test
	@Ignore
	public void addAndGet() throws Exception{
		/* addAndGet() 테스트 시나리오
		 * 1. 기존 데이터 삭제
		 * 2. 신규 데이터 등록
		 * 3. 단건 데이터 조회
		 * 4. 등록 데이터와 비교
		 */
		// [1] 기존 데이터 삭제
		doDelete(user01);
		doDelete(user02);
		doDelete(user03);
		assertEquals(0, dao.getCount(user01));
		
		// [2] 신규 데이터 등록
		add(user01);
		assertEquals(1, dao.getCount(user01));
		add(user02);
		assertEquals(2, dao.getCount(user01));
		add(user03);
		assertEquals(3, dao.getCount(user01));
		
		// [3] 단건 데이터 조회, [4] 등록 데이터와 비교
		UserVO outVO01 = doSelectOne(user01);
		isSameUser(outVO01, user01);
		
		UserVO outVO02 = doSelectOne(user02);
		isSameUser(outVO02, user02);
		
		UserVO outVO03 = doSelectOne(user03);
		isSameUser(outVO03, user03);
	}
	
//	@Test
//	public void add() throws Exception{
	public void add(UserVO user) throws Exception{
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/add.do
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/add.do")
				.param("uId", user.getuId())
				.param("name", user.getName())
				.param("passwd", user.getPasswd())
				.param("intLevel", user.getIntLevel()+"")//int 값을 string으로 만드는 가장 쉬운 방법
				.param("login", user.getLogin()+"")
				.param("recommend", user.getRecommend()+"")
				.param("email", user.getEmail())
				.param("regDt", user.getRegDt());
		
		// 대역 객체를 통해 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();

		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		// jsonString to VO
		Gson gson = new Gson();
		MessageVO messageVO = gson.fromJson(result, MessageVO.class);
		
		LOG.debug("============================");
		LOG.debug("=messageVO="+messageVO);
		LOG.debug("============================");	
	}
	
//	@Test
//	public void doDelete() throws Exception{
	public void doDelete(UserVO user) throws Exception{
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doDelete.do?uId=p31 <== GET방식 일때만 가능
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/doDelete.do")
				.param("uId", user.getuId());
	
		// 대역 객체를 통해 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();

		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		// jsonString to VO
		Gson gson = new Gson();
		MessageVO messageVO = gson.fromJson(result, MessageVO.class);
		LOG.debug("============================");
		LOG.debug("=messageVO="+messageVO);
		LOG.debug("============================");	
	}
	
//	@Test
//	@Ignore
	public UserVO doSelectOne(UserVO user) throws Exception {
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doSelectOne.do?uId=p31 <== GET방식 일때만 가능
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/doSelectOne.do")
				.param("uId", user.getuId());
		
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk());
		
		String result = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();
		LOG.debug("============================");
		LOG.debug("=result="+result);
		LOG.debug("============================");
		
		Gson gson = new Gson();

		// gson String to UserVO
		UserVO outVO = gson.fromJson(result, UserVO.class);
		
		return outVO;
	}

}
