package com.inbravo.spring.boot.security.saml.web.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.inbravo.spring.boot.security.saml.web.core.CurrentUserHandlerMethodArgumentResolver;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

  @Autowired
  CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver;

  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {

    LOG.info("addViewControllers");
    registry.addViewController("/").setViewName("index");
    registry.addViewController("/error").setViewName("error");
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {

    LOG.info("addArgumentResolvers");
    argumentResolvers.add(currentUserHandlerMethodArgumentResolver);
  }
}
