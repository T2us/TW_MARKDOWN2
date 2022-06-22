package com.pcwk.ehr.user.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.cmn.SearchVO;
import com.pcwk.ehr.cmn.StringUtil;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserService;

@Controller("userController")
@RequestMapping("user") // 모든 메소드 url 앞에 user를 붙인 역할을 해준다.
public class UserController {
	final Logger LOG = LogManager.getLogger(this.getClass());
	
	final String VIEW_NAME ="user/user_mng";  
	
	@Autowired
	UserService userService;
	
	public UserController() {}
	
	// testURL : http://localhost:8081/ehr/user/pwCheck.do?passwd=4321
		@RequestMapping(value="/pwCheck.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
		@ResponseBody
		public String pwCheck(Model model, UserVO inVO) throws SQLException {
			String resultMsg = "";
			String jsonString = "";
			
			LOG.debug("==========================");
			LOG.debug("=pwCheck()=");
			LOG.debug("==========================");
			
			int flag = userService.pwCheck(inVO);
			
			if(flag == 1) resultMsg = inVO.getuId() + "가 동일합니다.";
			else resultMsg = inVO.getuId() + "는 동일 하지 않습니다.";
					
			jsonString = new Gson().toJson(new MessageVO(String.valueOf(flag), resultMsg));
			
			LOG.debug("=========================");
			LOG.debug("=jsonString: ="+jsonString);
			LOG.debug("=========================");
			
			return jsonString;
		}
	
	// testURL : http://localhost:8081/ehr/user/idCheck.do?uId=p09
	@RequestMapping(value="/idCheck.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String idCheck(Model model, UserVO inVO) throws SQLException {
		String resultMsg = "";
		String jsonString = "";
		
		LOG.debug("==========================");
		LOG.debug("=idCheck()=");
		LOG.debug("==========================");
		
		int flag = userService.idCheck(inVO);
		
		if(flag == 1) resultMsg = inVO.getuId() + "가 중복되었습니다.";
		else resultMsg = inVO.getuId() + "는 중복되지 않았습니다.";
				
		jsonString = new Gson().toJson(new MessageVO(String.valueOf(flag), resultMsg));
		
		LOG.debug("=========================");
		LOG.debug("=jsonString: ="+jsonString);
		LOG.debug("=========================");
		
		return jsonString;
	}
	
	// testURL : http://localhost:8081/ehr/user/userView.do
	@RequestMapping(value="/userView.do", method = RequestMethod.GET)
	public String userView(Model model, SearchVO inVO) throws SQLException {
		LOG.debug("==========================");
		LOG.debug("=userView()=");
		LOG.debug("==========================");
		
		if(inVO.getPageSize() == 0) inVO.setPageSize(10); // 페이지 사이즈 default
		if(inVO.getPageNum() == 0) inVO.setPageNum(1); // 페이지 번호 default
		if(inVO.getSearchDiv() == null) inVO.setSearchDiv(StringUtil.nvl(inVO.getSearchDiv(), "")); // 검색구분 default
		if(inVO.getSearchWord() == null) inVO.setSearchWord(StringUtil.nvl(inVO.getSearchWord(), "")); // 검색어 default
		
		LOG.debug("=========================");
		LOG.debug("=inVO: ="+inVO);
		LOG.debug("=========================");
		
		List<UserVO> list = userService.doRetrieve(inVO);
		
		int totalCnt = 0;
		
		// 총글수: paging에 사용
		if(list.size() > 0 && list != null) {
			totalCnt = list.get(0).getTotalCnt();
		}
		
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("list", list);
		
		// /WEB-INF/views/ + VIEW_NAME + .jsp
		return VIEW_NAME;
	}
	
	@RequestMapping(value="/doRetrieve.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doRetrieve(SearchVO inVO) throws SQLException{
		String jsonString = "";
			
		if(inVO.getPageSize() == 0) inVO.setPageSize(10); // 페이지 사이즈 default
		if(inVO.getPageNum() == 0) inVO.setPageNum(1); // 페이지 번호 default
		if(inVO.getSearchDiv() == null) inVO.setSearchDiv(StringUtil.nvl(inVO.getSearchDiv(), "")); // 검색구분 default
		if(inVO.getSearchWord() == null) inVO.setSearchWord(StringUtil.nvl(inVO.getSearchWord(), "")); // 검색어 default
		
		LOG.debug("=========================");
		LOG.debug("=inVO: ="+inVO);
		LOG.debug("=========================");
		
		List<UserVO> list = userService.doRetrieve(inVO);
		Gson gson = new Gson();
		
		jsonString = gson.toJson(list);
		
		LOG.debug("=========================");
		LOG.debug("=jsonString: ="+jsonString);
		LOG.debug("=========================");
		
		return jsonString;
	}
	
	@RequestMapping(value="/doUpdate.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doUpdate(UserVO inVO) throws SQLException{
		String jsonString = "";
		
		LOG.debug("=========================");
		LOG.debug("=inVO: ="+inVO);
		LOG.debug("=========================");
		String resultMsg = "";
		
		int flag = userService.doUpdate(inVO);
		
		if(flag == 1) resultMsg = inVO.getuId() + "가 수정되었습니다.";
		else resultMsg = inVO.getuId() + "가 수정되지 않았습니다.";
		
		MessageVO message = new MessageVO(String.valueOf(flag), resultMsg);
		Gson gson = new Gson();
		
		jsonString = gson.toJson(message);
		
		LOG.debug("=========================");
		LOG.debug("=jsonString: ="+jsonString);
		LOG.debug("=========================");
		
		return jsonString;
	}
	
	// test url: http://localhost:8081/ehr/user/add.do
	// http://localhost:8081/ehr/user/add.do 이 요청이 되면 Controller가 아래의 메서드를 실행하여 매핑시켜준다.
	@RequestMapping(value = "/add.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody // 스프링에서 비동기 처리를 하는 경우, Http 요청의 본문 body부분이 전달된다.
	public String doInsert(UserVO inVO) throws SQLException{
		String jsonString = "";
		LOG.debug("=========================");
		LOG.debug("=inVO: ="+inVO);
		LOG.debug("=========================");
		String resultMsg = "";
		
		int flag = userService.add(inVO);
		
		if(flag == 1) resultMsg = inVO.getuId() + "가 입력되었습니다.";
		else resultMsg = inVO.getuId() + "가 입력되지 않았습니다.";
		
		MessageVO message = new MessageVO(String.valueOf(flag), resultMsg);
		Gson gson = new Gson();
		
		jsonString = gson.toJson(message);
		
		LOG.debug("=========================");
		LOG.debug("=jsonString: ="+jsonString);
		LOG.debug("=========================");
		
		return jsonString;
	}
	
	// test url: http://localhost:8081/ehr/user/doDelete.do?uId=p09 <== GET방식 일때만 가능
	// http://localhost:8081/ehr/user/doDelete.do 이 요청이 되면 Controller가 아래의 메서드를 실행하여 매핑시켜준다.
	@RequestMapping(value = "/doDelete.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody // 스프링에서 비동기 처리를 하는 경우, Http 요청의 본문 body부분이 전달된다.
	public String doDelete(HttpServletRequest req, Model model, UserVO inVO) throws SQLException{
		String jsonString = "";
		
		String uId = req.getParameter("uId"); // 해당 메소드로 UserVO의 uId변수 값을 가져온다.
		LOG.debug("============================");
		LOG.debug("=uId="+uId);
		// Command로 param읽기(from name값이 VO member 변수와 이름이 일치해야 한다.)
		LOG.debug("=inVO="+inVO);
		LOG.debug("============================");	
		
		String resultMsg = "";
		int flag = userService.doDelete(inVO);
		
		if(flag == 1) resultMsg = inVO.getuId() + "가 삭제 되었습니다.";
		else resultMsg = inVO.getuId() + "가 삭제되지 않았습니다.";
		
		MessageVO message = new MessageVO(String.valueOf(flag), resultMsg);
		Gson gson = new Gson();
		
		jsonString = gson.toJson(message);
		
		LOG.debug("=========================");
		LOG.debug("=jsonString: ="+jsonString);
		LOG.debug("=========================");
		
		return jsonString;
	}
	
	// test url: http://localhost:8081/ehr/user/doSelectOne.do?uId=p09 <== GET방식 일때만 가능
	// http://localhost:8081/ehr/user/doSelectOne.do 이 요청이 되면 Controller가 아래의 메서드를 실행하여 매핑시켜준다.
	@RequestMapping(value = "/doSelectOne.do", method = RequestMethod.GET
			,produces = "application/json;charset=UTF-8")
	@ResponseBody // 스프링에서 비동기 처리를 하는 경우, Http 요청의 본문 body부분이 전달된다.
	// UserVO inVO: form name VO 멤버변수명이 동일하면 자동으로 매핑한다.
	public String doSelectOne(UserVO inVO) throws SQLException{
		LOG.debug("=========================");
		LOG.debug("=inVO: ="+inVO);
		LOG.debug("=========================");
		
		UserVO outVO = userService.doSelectOne(inVO);
		Gson gson = new Gson();
		
		String jsonString = gson.toJson(outVO);
		
		LOG.debug("=========================");
		LOG.debug("=jsonString: ="+jsonString);
		LOG.debug("=========================");
		
		return jsonString;
	}
}
