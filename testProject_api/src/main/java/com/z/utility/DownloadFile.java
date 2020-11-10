package com.z.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;

public class DownloadFile {
	
	public void download(HttpServletRequest request, HttpServletResponse response, String realFilePath, String realFileName, String originalFileName) {
		String defaultFilePath = request.getSession().getServletContext().getRealPath("/");
		
		File downloadFile = new File(defaultFilePath + realFilePath + realFileName);
		System.out.println(downloadFile);
		if (downloadFile.exists() && downloadFile.isFile()) {
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setContentLength((int) downloadFile.length());
			OutputStream os = null;
			FileInputStream fis = null;
			try {
				response.setHeader("Content-Disposition", getDisposition(realFileName, check_Browser(request)));
				response.setHeader("Content-Transfer-Encoding", "binary");
				os = response.getOutputStream();
				fis = new FileInputStream(downloadFile);
				
				//Spring framework 사용시
				FileCopyUtils.copy(fis, os);
				
				//일반 자바/jsp 사용시
//				byte[] buffer = new byte[1024];
//				int length = 0;
//				while((length = fis.read(buffer))>0) {
//					os.write(buffer, 0, length);
//				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
					os.flush();
					os.close();
				} catch (Exception e2) {}
			}
		} else {
			System.out.println("파일 존재하지 않음");
		}
	}
	
	private String check_Browser(HttpServletRequest request) {
		String browser = "";
		String header = request.getHeader("User-Agent");
		if(header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
			browser = "ie";
		}
		else if (header.indexOf("Chrome") > -1) {
			browser = "chrome";
		}
		else if (header.indexOf("Opera") > -1) {
			browser = "opera";
		}
		else if (header.indexOf("Apple") > -1) {
			browser = "safari";
		} else {
			browser = "firefox";
		}
		return browser;
	}
	
	private String getDisposition(String down_FileName, String browser_Check) throws Exception {
		String prefix = "attachment;filename=";
		String encodedFileName = null;
		if (browser_Check.equals("ie")) {
			encodedFileName = URLEncoder.encode(down_FileName, "UTF-8").replaceAll("\\+", "%20");
		}else if (browser_Check.equals("chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i=0; i<down_FileName.length(); i++) {
				char c = down_FileName.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFileName = sb.toString();
		} else {
			encodedFileName = "\"" + new String(down_FileName.getBytes("UTF-8"), "8859_1") + "\"";
		}
		return prefix + encodedFileName;
	}
}
