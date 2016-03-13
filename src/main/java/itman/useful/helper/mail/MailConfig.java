package itman.useful.helper.mail;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class MailConfig {
	private static MailConfig instance = null;

	public static MailConfig getInstance() throws ConfigurationException {
		if (instance == null) {
			instance = new MailConfig();
		}
		return instance;
	}

	private Configuration config;

	private MailConfig() throws ConfigurationException {
		config = new PropertiesConfiguration("clock-on.properties");
	}

	public boolean debugEnabled() {
		return config.getBoolean("mail.debug", false);
	}

	public String getCharSet() {
		return config.getString("mail.charset", "utf-8");
	}

	public String getMailAccount() {
		String account = config.getString("mail.account", "");
		if (account == null || account.equals("")) {
			account = config.getString("clockon.name");
		}
		return account;
	}

	public String getMailPassword() {
		String password = config.getString("mail.password", "");
		if (password == null || password.equals("")) {
			password = config.getString("clockon.password");
		}
		return password;
	}

	public String getReceiver() {
		String receiver = config.getString("mail.receiver", "");
		if (receiver == null || receiver.equals("")) {
			receiver = config.getString("clockon.mail");
		}
		return receiver;
	}

	public String getSender() {
		String sender = config.getString("mail.sender", "");
		if (sender == null || sender.equals("")) {
			sender = config.getString("clockon.mail");
		}
		return sender;
	}

	public String getSmptHostName() {
		return config.getString("mail.smtp.hostname", "CAS-HT01.SYSTEX.TW");
	}

	public int getSmptPort() {
		return config.getInt("mail.smtp.port", 25);
	}

	public String getSmptSSLPort() {
		return config.getString("mail.smtp.sslport", "465");
	}

	public boolean mailNotificationEnabled() {
		String mail = config.getString("clockon.mail", "");
		if (mail != null && !mail.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean sslEnabled() {
		return config.getBoolean("mail.ssl.enabled", false);
	}

	public boolean tlsEnabled() {
		return config.getBoolean("mail.tls.enabled", true);
	}
}
