package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.domain.UserVO;

public interface UserService {
	/**
	 * pw check
	 * @param inVO
	 * @return uId가 같고, passwd가 같으면 1/ 그 외 0
	 * @throws SQLException
	 */
	public int pwCheck(UserVO inVO) throws SQLException;
	
	/**
	 * 중복 id check
	 * @param inVO
	 * @return 1(중복)/ 0(중복 없음)
	 * @throws SQLException
	 */
	public int idCheck(UserVO inVO) throws SQLException;
	
	/**
	 * 회원목록 조회
	 * @param dto
	 * @return List<UserVO>
	 * @throws SQLException
	 */
	public List<UserVO> doRetrieve(DTO dto) throws SQLException;
	
	/**
	 * 회원정보 삭제
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	public int doDelete(UserVO inVO) throws SQLException;
	
	/**
	 * 회원 단건 조회
	 * @param inVO
	 * @return UserVO
	 * @throws SQLException
	 */
	public UserVO doSelectOne(final UserVO inVO) throws SQLException;
	
	/**
	 * 사용자 등록
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	public int doInsert(final UserVO inVO) throws SQLException;
	
	/**
	 * 회원정보 수정
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	public int doUpdate(UserVO inVO) throws SQLException;
	
	/**
	 * 등업 기능
	 * @throws SQLException
	 */
	public void upgradeLevels(UserVO inVO) throws SQLException;
	
	/**
	 * 최초 가입자는 기본적으로 BASIC 레벨이어야 한다.
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	public int add(UserVO inVO) throws SQLException;
}
