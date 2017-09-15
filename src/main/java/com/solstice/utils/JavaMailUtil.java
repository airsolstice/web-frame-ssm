package com.solstice.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {
	/**
	 * 
	 * @param 主题
	 * @param 内容
	 * @param 接收者
	 * @throws Exception
	 */
	public static void sendMail(String subject, String text, String[] to)
			throws Exception {
		Properties props = new Properties();
		// 服务器（smtp）要求认证，是否同意认证
		props.setProperty("mail.smtp.auth", "true");
		// 传输协议
		props.setProperty("mail.transport.protocol", "smtp");
		// 实例化session
		Session session = Session.getInstance(props);
		// 开启调试模式
		session.setDebug(true);
		// 创建message对象
		Message msg = new MimeMessage(session);
		// 设置主题
		msg.setSubject(subject);
		// 设置message内容
		msg.setText(text);
		// 设置发件人
		msg.setFrom(new InternetAddress("1621696311@qq.com"));
		Transport transport = session.getTransport();
		// 主机，端口，用户名，密码
		transport.connect("smtp.qq.com", 587, "1621696311@qq.com",
				"cznctehjpzyueidi");
		// 发送信息，收件人地址
		for (int i = 0; i < to.length; i++) {
			transport.sendMessage(msg, new Address[] { new InternetAddress(
					to[i]) });
		}
		// 关闭传输
		transport.close();
	}

}
