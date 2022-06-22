package com.pcwk.ehr.user.dao;

import java.sql.SQLException;
import java.util.List;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.domain.UserVO;


public interface UserDao {
	
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
	 * 목록 조회
	 * @param dto
	 * @return List<UserVO>
	 * @throws SQLException
	 */
	public List<UserVO> doRetrieve(DTO dto) throws SQLException;
	
	/**
	 * 사용자 삭제
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	public int doDelete(UserVO inVO) throws SQLException;
	
	/**
	 * 사용자 수정 가능
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	public int doUpdate(UserVO inVO) throws SQLException;
	
	/**
	 * 사용자 등록
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int doInsert(UserVO inVO) throws SQLException;

	/**
	 * 모든 데이터 삭제
	 * @throws SQLException
	 */
	public void deleteAll() throws SQLException;

	/**
	 * 조회 건수 출력
	 * @param inVO
	 * @return int
	 * @throws SQLException
	 */
	public int getCount(UserVO inVO) throws SQLException;
	
	/**
	 * 회원 다건 조회
	 * @return
	 */
	public List<UserVO> getAll(UserVO inVO);
	
	/**
	 * 회원 단건 조회
	 * @param inVO
	 * @return UserVO
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public UserVO doSelectOne(UserVO inVO) throws SQLException;

}