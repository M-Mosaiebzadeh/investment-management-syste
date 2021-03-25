package ir.maktab.investment.service;

import ir.maktab.investment.model.User;
import ir.maktab.investment.model.VerificationCode;
import ir.maktab.investment.repository.VerificationCodeRepository;
import net.bytebuddy.utility.RandomString;
import org.apache.catalina.valves.rewrite.InternalRewriteMap;
import org.hibernate.validator.constraints.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class VerificationCodeService {
    private final Long EXPIRE_TIME_MINUTE = 15L;
    private final String SENDER_MAIL = "m.mosaiebzadeh@gmail.com";
    private VerificationCodeRepository verificationCodeRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public VerificationCodeService(VerificationCodeRepository verificationCodeRepository,
                                   JavaMailSender javaMailSender) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.javaMailSender = javaMailSender;
    }

    public VerificationCode createVerificationCode(VerificationCode verificationCode) {
        if (verificationCode == null)
             verificationCode = new VerificationCode();

        verificationCode.setCode(RandomString.make(64));
        verificationCode.setRequestTime(new Date());
        verificationCode.setExpireTime(new Date(verificationCode.getRequestTime().getTime() + EXPIRE_TIME_MINUTE * 60 * 1000));
        return verificationCodeRepository.save(verificationCode);
    }


    public void sendVerificationCodeForRegistration(User user, String siteUrl)
            throws UnsupportedEncodingException, MessagingException {

        String subject = "Please verify your registration";
        String senderName = "Maktab Broker Team";
        String content = "<p>Dear " + user.getUsername() + ",</p>";
        content += "<p>Please click the link below to verify to your registration:</p>";

        String verifyUrl = siteUrl + "/registration/verify?code=" + user.getVerificationCode().getCode();
        content += "<h3><a href = '" + verifyUrl + "'>Verify Link</a></h3><br>";

        content += "<p>Thank you,<br>The Maktab Broker Team</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(SENDER_MAIL, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);

    }

    public void sendUsernameAndPasswordForCreateResponsible(User user, String password)
            throws UnsupportedEncodingException, MessagingException {

        String subject = "Welcome To Maktab Broker Team";
        String senderName = "Maktab Broker Team";
        String content = "<p>Hi " + user.getFirstname() + " " + user.getLastname() + ",</p>";
        content += "<p>Use default username and password to login:</p>";
        content += "<p style=\"font-weight: bold\">Username: </p>" + user.getUsername();
        content += "<p style=\"font-weight: bold\">Password: </p>" + password;
        content += "<p style=\"color: red\">Caution: after login, try to change default password</p>";

        content += "<p>Thank you,<br>The Maktab Broker Team</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(SENDER_MAIL, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);

    }

    public void delete(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

    public String createPasswordCode(User user) {
        List<Character> symbol = Arrays.asList('@','!','#','$','&');
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        password.append(user.getFirstname());
        password.setCharAt(0,user.getFirstname().toUpperCase().charAt(0));
        password.append(user.getLastname());
        password.setCharAt(user.getFirstname().length(),user.getLastname().toUpperCase().charAt(0));
        password.append(user.getNationalCode().substring(2,5));
        password.append(symbol.get(random.nextInt(5)));
        return password.toString();
    }

    public void sendVerificationCodeForForgetPassword(User user, String siteUrl)
            throws UnsupportedEncodingException, MessagingException {

        String subject = "Forget Password";
        String senderName = "Maktab Broker Team";
        String content = "<p>Dear " + user.getUsername() + ",</p>";
        content += "<p>Please click the link below to make new password:</p>";

        String verifyUrl = siteUrl + "/forget-password/verify?code=" + user.getVerificationCode().getCode();
        content += "<h3><a href = '" + verifyUrl + "'>Verify Link</a></h3><br>";

        content += "<p>Thank you,<br>The Maktab Broker Team</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(SENDER_MAIL, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);

    }
}
