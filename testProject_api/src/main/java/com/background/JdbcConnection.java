package com.background;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.utility.MyFunctions;

public class JdbcConnection {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MyFunctions myFn = new MyFunctions();
	Properties in_config = myFn.read_inConfig();
	
	Connection con;
    Statement stmt;
    ResultSet rs;
 
    String url;
    String id;
    String pw;
	
    // DB클래스 선택 (mssql-jdbc-8.2.2 / mysql-connector-java-5.1.31 / ojdbc6-11.2.0.3)
    public JdbcConnection() {
    	
    	String dbType = "mysql";
    	
    	try {
    		// 드라이버 로딩 (Mysql 또는 Oracle 중에 선택)
    		if (dbType.equals("mysql")) Class.forName("com.mysql.jdbc.Driver");    						// mysql
    		else if (dbType.equals("oracle")) Class.forName("oracle.jdbc.driver.OracleDriver");    		// oracle
    		else logger.info("DB 클래스 필요함");															// jar 설치 필요
    		
    	} catch (Exception e) {
    		logger.info("JDBC Type Error : " + e);
    	}
    }
    
    // 연결
    public void getConnection() {
    	
    	try {
            // 커넥션을 가져온다.
    		url = in_config.getProperty("url");
    	    id = in_config.getProperty("id");
    	    pw = in_config.getProperty("pw");
    		
            con = DriverManager.getConnection(url, id, pw);
        } catch(Exception e) {
            logger.info("JDBC getConnection : " + e);
        }
    	
    }
    
    // 닫기
    public void closeConnection() {
    	
    	try {
            // 자원 반환
            rs.close();
            stmt.close();
            con.close();
 
        } catch(Exception e) {
        	logger.info("JDBC closeConnection : " + e);
        }
    }
    
    // 테스트 쿼리
    public void getTestQuery() {
    	try {
    		
    		stmt = con.createStatement();
    		// 데이터를 가져온다.
    		rs = stmt.executeQuery("SELECT SYSDATE() AS TODAY FROM DUAL");
    		
    		while(rs.next()) {
    			// 출력
    			logger.info("MYSQL DB CONNECTION TEST QUERY VALUE : " + rs.getString("TODAY"));
    		}
    		
    	} catch(Exception e) {
    		logger.info("JDBC getTestQuery : " + e);
    	}
    }
    
}
