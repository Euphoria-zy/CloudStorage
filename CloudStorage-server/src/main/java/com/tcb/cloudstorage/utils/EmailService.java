package com.tcb.cloudstorage.utils;

import com.sun.mail.util.MailSSLSocketFactory;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.tcb.cloudstorage.domain.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class EmailService
{
    public static String EMAIL_TITLE1 = "加密极速云盘：注册验证";
    public static String EMAIL_TITLE2 = "加密极速云盘：找回密码";
    //发送密码
    public static void sendEmail(User user, String checkCode, int codeType) throws GeneralSecurityException
    {
        //账户信息
        String accountId;
        String accountCode;

        //读取账户
        Properties accountProperties = new Properties();
        InputStream is = EmailService.class.getClassLoader().getResourceAsStream("email.properties");
        try {
            accountProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountId = accountProperties.getProperty("id");
        accountCode = accountProperties.getProperty("code");

        //创建一个配置文件并保存
        Properties properties = new Properties();
        //邮件发送采用smtp服务，服务器地址一般是smtp.xxx.com；QQ：smtp.qq.com
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        //QQ存在一个特性设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        /*
        * 创建一个session对象
        * 定义程序所需的环境信息：主机号，端口号，采用邮件发送和接收协议
        * */
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(accountId, accountCode);
            }
        });
        //开启debug模式
        session.setDebug(true);
        //连接
        try
        {
            //获取连接对象：用来发送邮件
            Transport transport = session.getTransport();
            //连接服务器
            transport.connect("smtp.qq.com", accountId, accountCode);
            //创建邮件对象：表示一封邮件
            MimeMessage mimeMessage = new MimeMessage(session);
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress(accountId));
            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            //邮件标题
            if (codeType == 1)
                mimeMessage.setSubject(EMAIL_TITLE1);
            else
                mimeMessage.setSubject(EMAIL_TITLE2);
            //邮件内容
            mimeMessage.setText("您好！" + user.getUsername() + "\n您的验证码是：" + checkCode);
            //发送邮件
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            //关闭连接
            transport.close();
        } catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
