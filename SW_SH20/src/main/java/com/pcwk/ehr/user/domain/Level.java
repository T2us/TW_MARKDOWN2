package com.pcwk.ehr.user.domain;

public enum Level {
// Level을 Enum을 이용하여 설계
//	   개발을 진행할때 Enum을 통해 얻는 기본적인 장점들은 아래와 같습니다.
//
//	  1. 문자열과 비교해, IDE의 적극적인 지원을 받을 수 있습니다.
//	  2. 자동완성, 오타검증, 텍스트 리팩토링 등등
//	  3. 허용 가능한 값들을 제한할 수 있습니다.
//	  4. 리팩토링시 변경 범위가 최소화 됩니다.
//	  5. 내용의 추가가 필요하더라도, Enum 코드외에 수정할 필요가 없습니다.
	GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);
	
	private final int value;
	
	// 다음 레벨에 대한 멤버 변수
	private final Level next;

	private Level(int value, Level next) {
		this.value = value;
		this.next = next;
	}

	/**
	 * 다음 Level 가지고 오기
	 * @return
	 */
	public Level nextLevel() {
		return this.next;
	}
	
	/**
	 * 값을 가지고 올 때 사용
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 값으로 부터 Level 가져오기
	 * 1 -> BASIC
	 * 2 -> SILVER
	 * 3 -> GOLD
	 * @param value
	 * @return
	 */
	public static Level valueOf(int value) {
		switch (value) {
			case 1:	return BASIC;
			case 2: return SILVER;
			case 3: return GOLD;
			default: throw new AssertionError("Unknwon value:"+value);
		}
	}
}
