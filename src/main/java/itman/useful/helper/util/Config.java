package itman.useful.helper.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
	private static Config instance = null;

	public static Config getInstance() throws ConfigurationException {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	private Configuration config;

	private Config() throws ConfigurationException {
		config = new PropertiesConfiguration("clock-on.properties");
	}

	public String getName() {
		return config.getString("clockon.name");
	}

	public String getPassword() {
		return config.getString("clockon.password");
	}

	public String getUrl() {
		return config.getString("clockon.url");
	}
}
