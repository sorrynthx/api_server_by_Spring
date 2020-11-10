package com.services;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
	
	@Autowired
    private SqlSession sqlSession;

	public String getDBcheck() {
		return sqlSession.selectOne("common.getDBcheck");
	}
}
