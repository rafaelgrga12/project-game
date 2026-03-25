package com.example.game.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

/**
 * Converte {@code postgres://} / {@code postgresql://} (formato Supabase) em
 * {@code jdbc:postgresql://} para o Hikari aceitar {@code spring.datasource.url}.
 */
public class PostgresJdbcUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private static final String URL_KEY = "spring.datasource.url";
	private static final String USER_KEY = "spring.datasource.username";
	private static final String PASS_KEY = "spring.datasource.password";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String url = environment.getProperty(URL_KEY);
		if (url == null || url.isBlank()) {
			return;
		}
		String trimmed = url.trim();
		if (trimmed.startsWith("jdbc:")) {
			return;
		}
		String scheme = trimmed.substring(0, Math.min(trimmed.length(), 15)).toLowerCase();
		if (!scheme.startsWith("postgres://") && !scheme.startsWith("postgresql://")) {
			return;
		}
		try {
			Parsed parsed = parse(trimmed);
			Map<String, Object> map = new LinkedHashMap<>();
			map.put(URL_KEY, parsed.jdbcUrl());
			if (parsed.username() != null && !parsed.username().isBlank()) {
				String existingUser = environment.getProperty(USER_KEY);
				if (existingUser == null || existingUser.isBlank()) {
					map.put(USER_KEY, parsed.username());
				}
			}
			if (parsed.password() != null && !parsed.password().isBlank()) {
				String existingPass = environment.getProperty(PASS_KEY);
				if (existingPass == null || existingPass.isBlank()) {
					map.put(PASS_KEY, parsed.password());
				}
			}
			MutablePropertySources sources = environment.getPropertySources();
			sources.addFirst(new MapPropertySource("postgresJdbcUrlConversion", map));
		} catch (Exception ignored) {
			// mantém URL original
		}
	}

	private record Parsed(String jdbcUrl, String username, String password) {}

	private static Parsed parse(String url) throws Exception {
		int schemeEnd = url.indexOf("://");
		if (schemeEnd < 0) {
			throw new IllegalArgumentException("missing ://");
		}
		String remainder = url.substring(schemeEnd + 3);
		URI uri = new URI("http://" + remainder);
		String userInfo = uri.getRawUserInfo();
		String user = null;
		String pass = null;
		if (userInfo != null && !userInfo.isEmpty()) {
			int colon = userInfo.indexOf(':');
			if (colon >= 0) {
				user = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
				pass = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);
			}
			else {
				user = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
			}
		}
		String host = uri.getHost();
		if (host == null || host.isEmpty()) {
			throw new IllegalArgumentException("missing host");
		}
		int port = uri.getPort();
		String path = uri.getRawPath();
		if (path == null || path.isEmpty()) {
			path = "/postgres";
		}
		StringBuilder jdbc = new StringBuilder();
		jdbc.append("jdbc:postgresql://").append(host);
		if (port > 0) {
			jdbc.append(":").append(port);
		}
		jdbc.append(path);
		String query = uri.getRawQuery();
		if (query != null && !query.isEmpty()) {
			jdbc.append("?").append(query);
		}
		String jdbcStr = jdbc.toString();
		if (!jdbcStr.contains("sslmode=")) {
			jdbcStr += (jdbcStr.contains("?") ? "&" : "?") + "sslmode=require";
		}
		return new Parsed(jdbcStr, user, pass);
	}
}
