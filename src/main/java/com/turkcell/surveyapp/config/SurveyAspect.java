package com.turkcell.surveyapp.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.exception.AccessDeniedException;
import com.turkcell.surveyapp.exception.UserNotLoginException;
import com.turkcell.surveyapp.intf.RequiredAdmin;
import com.turkcell.surveyapp.intf.RequiredLogin;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class SurveyAspect {
	private final UserSession userSession;

	@Around("@annotation(requiredAdmin)")
	public Object controlRequiredAdmin(ProceedingJoinPoint joinPoint, RequiredAdmin requiredAdmin) throws Throwable {
		if (userSession.getUser() == null) {
			throw new UserNotLoginException();
		}
		if (!UserType.ADMIN.equals(userSession.getUser().getUserType())) {
			throw new AccessDeniedException();
		}
		return joinPoint.proceed();
	}
	
	@Around("@annotation(requiredLogin)")
	public Object controlAuthority(ProceedingJoinPoint joinPoint, RequiredLogin requiredLogin) throws Throwable {
		if (userSession.getUser() == null) {
			throw new UserNotLoginException();
		}
		return joinPoint.proceed();
	}
}