# TW_MARKDOWN2
TW_MARKDOWN2

## 이미지 넣기
![배고파](https://github.com/T2us/TW_MARKDOWN2/blob/main/f5cf314c3fe0aaa14412dfada8a2c34e.gif)  

## 강조
굵게 표시 : **오늘은 즐거운 금요일**  
굵게 표시 : __오늘은 즐거운 금요일__

## 하이퍼링크
[pcwk](http://cafe.daum.net/pcwk)

## 가로선
---
***

## 코드 블록
```
	@Test
	public void doDelete() throws Exception{
		// 호출 url, param, 호출 방식(get/ post)
		// test url: http://localhost:8081/ehr/user/doDelete.do?uId=p31 <== GET방식 일때만 가능
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/doDelete.do")
				.param("uId", user01.getuId());
	
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

## 순서가 있는 목록
1. 아이템1
3. 아이템2  
    9. 1단 하위 아이템  
        3. 2단 하위 아이템  
9. 아이템3

- 아이템1  
+ 아이템2

## 목록 : 기본적인 리스트 작성 방법  
* 목록 이름
- 목록 이름

## 인용상자 : > 내용 형식으로 인용 상자를 작성할 수 있다.  
> 여기에 인용할 내용을 채워 넣으면 됩니다.  
빈 줄이 없으면 자동으로 인용상자에 포함이 됩니다.

## 문단 구분을 위한 강제 개행: 줄의 마지막에 [Space bar]를 두번 이상 눌러 띄어쓰기 한다.  
가나
## 헤더 : #헤더 이름 식으로 작성  
# 헤더  
## 헤더  
### 헤더  
#### 헤더  
##### 헤더  
  

