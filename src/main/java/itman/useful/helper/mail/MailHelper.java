package itman.useful.helper.mail;

import itman.useful.helper.util.LoggerUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;

public class MailHelper {
	private MailConfig config;
	final String LINE_BREAK = "\r\n";
	Logger mailLogger = LoggerUtil.getMailLogger();
	SimpleDateFormat simpleDF = new SimpleDateFormat("MM-dd");
	SimpleDateFormat detailDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat snapshotDF = new SimpleDateFormat("yy-MM-dd_HH:mm:ss");

	public MailHelper() {
		try {
			config = MailConfig.getInstance();
		} catch (ConfigurationException e) {
			mailLogger.warn(e);
		}
	}

	private EmailAttachment getAttachment(String filePath, Date date) {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(filePath);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setName("Snapshot_" + snapshotDF.format(date));
		return attachment;
	}

	private MultiPartEmail getMultipartMail() throws EmailException {
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(config.getSmptHostName());
		email.setSmtpPort(config.getSmptPort());
		email.setSslSmtpPort(config.getSmptSSLPort());
		email.setAuthenticator(new DefaultAuthenticator(config.getMailAccount(), config.getMailPassword()));

		email.setDebug(config.debugEnabled());
		email.setStartTLSEnabled(config.tlsEnabled());
		email.setStartTLSRequired(config.tlsEnabled());
		email.setSSLCheckServerIdentity(config.sslEnabled());
		email.setSSLOnConnect(config.sslEnabled());
		email.setCharset(config.getCharSet());

		email.setFrom(config.getSender());
		email.addTo(config.getReceiver());

		return email;
	}

	public void send(String subject, String content, String filePath) {
		if (!config.mailNotificationEnabled()) {
			mailLogger.debug("Mail Notification is disabled.");
			return;
		}

		Date now = new Date();
		content = "Time: " + detailDF.format(now) + LINE_BREAK + LINE_BREAK + content + LINE_BREAK + LINE_BREAK;

		MultiPartEmail email;
		try {
			email = getMultipartMail();
			email.setSubject(subject);
			email.setMsg(content);

			if (filePath != null && !filePath.equals("")) {
				email.attach(getAttachment(filePath, now));
			}
			email.send();
		} catch (EmailException e) {
			mailLogger.info("Failed to sending mail.", e);
		}
	}

	public void sendEndEarly(String content) {
		sendEndEarly(content, "");
	}

	public void sendEndEarly(String content, String filePath) {
		Date now = new Date();
		String subject = "Clock On Ended Early " + simpleDF.format(now);

		content = "The clock on job ended early." + LINE_BREAK + LINE_BREAK + "------------------------------------------" + LINE_BREAK + content;

		send(subject, content, filePath);
	}

	public void sendFailed(Exception e) {
		sendFailed(e, "");
	}

	public void sendFailed(Exception e, String filePath) {
		Date now = new Date();
		String subject = "Clock On Failed " + simpleDF.format(now);

		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String content = e.getMessage() + LINE_BREAK + LINE_BREAK + "------------------------------------------" + LINE_BREAK + errors.toString();

		send(subject, content, filePath);
	}

	public void sendSuccess(String content) {
		Date now = new Date();
		String subject = "Clock On Success " + simpleDF.format(now);

		send(subject, content, "");
	}
}
