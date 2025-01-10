package com.api.market.core.service;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 9:49
 */
@Slf4j
@Service
public class EmailService {

	@Value("${pro.url:}")
	private String PRO_URL;

	@Resource
	private MailAccount mailAccount;

	public void sendAkSk(String merchantName, String merchantCode, String appKey, String appSecret, String email) {

		if (!Validator.isEmail(email)) {
			log.warn("不合法的邮箱地址：{}", email);
			return;
		}
		String template =
				"<div style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
						"<p>尊敬的 {}客户：</p>" +
						"<p>&nbsp;&nbsp;&nbsp;&nbsp;您好！以下是贵司在我司开通的账号相关信息，该信息作为贵司在我司系统后台身份识别的唯一标识，请注意保密！</p>" +
						"<p>merCode：{}</p>" +
						"<p>appKey：{}</p>" +
						"<p>appSecret：{}</p>" +
						"<p>生产Api地址：http://xxxx.xxxx.com</p>" +
						"<br/>" +
						"<p>祝商祺！</p>" +
						"</div>";
		;
		// 发送邮件
		try {
			String result = MailUtil.send(mailAccount, email, "账密信息", CharSequenceUtil.format(template, merchantName, merchantCode, appKey, appSecret, PRO_URL),
					true);
			System.out.println("邮件发送成功！" + result);
		} catch (Exception e) {
			System.err.println("邮件发送失败：" + e.getMessage());
		}
	}


}
