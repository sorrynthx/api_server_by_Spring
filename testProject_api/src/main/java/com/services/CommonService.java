package com.services;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.z.utility.WebConfig;

@Service
public class CommonService {
	static WebConfig config = new WebConfig();
	
	@Autowired
    private SqlSession sqlSession;

	public String getDBcheck() {
		return sqlSession.selectOne("common.getDBcheck");
	}
}
