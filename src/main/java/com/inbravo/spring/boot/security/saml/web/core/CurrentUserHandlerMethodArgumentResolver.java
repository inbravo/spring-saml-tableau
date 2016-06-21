package com.inbravo.spring.boot.security.saml.web.core;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.inbravo.spring.boot.security.saml.web.stereotypes.CurrentUser;

@Component
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentUserHandlerMethodArgumentResolver.class);

  /**
   * 
   */
  public final boolean supportsParameter(final MethodParameter methodParameter) {

    LOG.info("supportsParameter, methodParameter: " + methodParameter.getParameterName());
    return methodParameter.getParameterAnnotation(CurrentUser.class) != null && methodParameter.getParameterType().equals(User.class);
  }

  /**
   * 
   */
  public final Object resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer mavContainer,
      final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {

    LOG.info("resolveArgument, methodParameter: " + methodParameter.getParameterName());

    if (this.supportsParameter(methodParameter)) {

      final Principal principal = (Principal) webRequest.getUserPrincipal();
      LOG.info("resolveArgument, principal: " + principal.getName());

      return (User) ((Authentication) principal).getPrincipal();
    } else {
      return WebArgumentResolver.UNRESOLVED;
    }
  }
}
