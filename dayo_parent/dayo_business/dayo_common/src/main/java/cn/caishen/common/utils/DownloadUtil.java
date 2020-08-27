package cn.caishen.common.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 文件下载工具类
 * @author LB
 */
public class DownloadUtil {

	/**
	 * @param byteArrayOutputStream 将文件内容写入ByteArrayOutputStream
	 * @param response HttpServletResponse	写入response
	 * @param returnName 返回的文件名
	 */
	public static void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response, String returnName) throws IOException{
		response.setContentType("application/octet-stream;charset=utf-8");
		//保存的文件名,必须和页面编码一致,否则乱码
		returnName = response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));
		response.addHeader("Content-Disposition",   "attachment;filename=" + returnName);  
		response.setContentLength(byteArrayOutputStream.size());

		//取得输出流
		ServletOutputStream outputstream = response.getOutputStream();
		//写到输出流
		byteArrayOutputStream.writeTo(outputstream);
		//关闭
		byteArrayOutputStream.close();
		//刷数据
		outputstream.flush();
	}
	
}
