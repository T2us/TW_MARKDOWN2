package com.pcwk.ehr.user.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.SearchVO;
import com.pcwk.ehr.user.domain.UserVO;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	
	final Logger LOG = LogManager.getLogger(this.getClass());	
	
	final String NAMESPACE = "com.pcwk.ehr.user";
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	public UserDaoImpl() {}
	
	@Override
	public int pwCheck(UserVO inVO) throws SQLException {
		String statement = NAMESPACE + ".pwCheck";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		int flag = this.sqlSessionTemplate.selectOne(statement, inVO);
		
		LOG.debug("================================");
		LOG.debug("flag: "+flag);
		LOG.debug("================================");
		
		return flag;
	}

	@Override
	public int idCheck(UserVO inVO) throws SQLException {
		String statement = NAMESPACE + ".idCheck";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		int flag = this.sqlSessionTemplate.selectOne(statement, inVO);
		
		LOG.debug("================================");
		LOG.debug("flag: "+flag);
		LOG.debug("================================");
		
		return flag;
	}
	
	@Override
	public List<UserVO> doRetrieve(DTO dto) throws SQLException {
		List<UserVO> list = null;
		SearchVO inVO = (SearchVO)dto;
		
		String statement = NAMESPACE + ".doRetrieve";

		LOG.debug("================================");
		LOG.debug("param: "+dto.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		list = sqlSessionTemplate.selectList(statement, inVO);   
		
		for(UserVO vo : list) LOG.debug("vo: "+vo.toString());
		
		return list;
	}

	@Override
	public int doDelete(UserVO inVO) throws SQLException {
		String statement = NAMESPACE + ".doDelete";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		int flag = this.sqlSessionTemplate.delete(statement, inVO);
		LOG.debug("flag: "+flag);
			
		return flag;
	}


	@Override
	public int doUpdate(UserVO inVO) throws SQLException {
		String statement = this.NAMESPACE + ".doUpdate";
		
		LOG.debug("================================");
		LOG.debug("statement: "+statement);
		LOG.debug("================================");	
		
		int flag = sqlSessionTemplate.update(statement, inVO);
		LOG.debug("flag: "+flag);
		
		return flag;
	}

	@Override
	public List<UserVO> getAll(UserVO inVO){
		List<UserVO> list = null;

		String statement = this.NAMESPACE + ".getAll";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");	

		
//		list = jdbcTemplate.query(sb.toString(), rowMapper, args);
		list = sqlSessionTemplate.selectList(statement, inVO);
		for(UserVO vo : list) LOG.debug("vo: "+vo.toString());
		
		return list;
	}
	
	@Override
	public int getCount(final UserVO inVO) throws SQLException{
		String statement = this.NAMESPACE + ".getCount";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());	
		LOG.debug("statement: "+statement);
		LOG.debug("================================");	

		int count = sqlSessionTemplate.selectOne(statement, inVO);
		LOG.debug("=======================================");
		LOG.debug("count= "+count);
		LOG.debug("=======================================");
		
		return count;
	}
		
	/**
	 * 사용자 등록
	 * @param inVO
	 * @return 1(성공)/ 0(실패)
	 * @throws SQLException
	 */
	@Override
	public int doInsert(final UserVO inVO) throws SQLException{
		String statement = NAMESPACE + ".doInsert";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		int flag = this.sqlSessionTemplate.insert(statement, inVO);
		LOG.debug("flag: "+flag);
		
		return flag;
	}
	
	@Override
	public void deleteAll() throws SQLException{
		String statement = this.NAMESPACE + ".deleteAll";
		
		LOG.debug("================================");
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		sqlSessionTemplate.delete(statement);
	}
	
	/**
	 * 회원 단건 조회
	 * @param inVO
	 * @return UserVO
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Override
	public UserVO doSelectOne(final UserVO inVO) throws SQLException {
		/*
		 * 모든 SQL은 user.xml로 이동
		 */
		UserVO outVO = null;
		
		String statement = this.NAMESPACE + ".doSelectOne";
		
		LOG.debug("================================");
		LOG.debug("param: "+inVO.toString());
		LOG.debug("statement: "+statement);
		LOG.debug("================================");
		
		outVO = this.sqlSessionTemplate.selectOne(statement, inVO);
		
		if(outVO == null) throw new NullPointerException();
		
		LOG.debug("================================");
		LOG.debug("**outVO: "+outVO.toString());
		LOG.debug("================================");
		
		return outVO;
	}
}