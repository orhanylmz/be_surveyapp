package com.turkcell.surveyapp.util;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	private static final String TIMEZONE = "Europe/Istanbul";

	public static LocalDateTime now() {
		return LocalDateTime.now(ZoneId.of(TIMEZONE));
	}

	public static String objectToJsonString(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	public static String encodeWithMD5(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte rawByte[] = md.digest();
			return Base64Utils.encodeToString(rawByte);
		} catch (Exception e) {
			return text;
		}
	}

	public static <T> HttpEntity<T> mapToHttpEntity(T request) throws JsonProcessingException {
		return new HttpEntity<T>(request, null);
	}
	
	public static <T> HttpEntity<T> mapToHttpEntityWithCookie(T request, HttpHeaders headers) throws JsonProcessingException {
		return new HttpEntity<T>(request, clearCookieHeader(headers));
	}
	
	public static <T> HttpEntity<T> mapToVoidHttpEntityWithCookie(HttpHeaders headers) throws JsonProcessingException {
		return new HttpEntity<>(clearCookieHeader(headers));
	}
	
	public static HttpHeaders clearCookieHeader(HttpHeaders headers) throws JsonProcessingException {
		String sessionCookie = headers.get("Set-Cookie").get(0).split(";")[0];
		HttpHeaders newHeaders = new HttpHeaders();
		newHeaders.add(HttpHeaders.COOKIE, sessionCookie);
		return newHeaders;
	}
	
}
