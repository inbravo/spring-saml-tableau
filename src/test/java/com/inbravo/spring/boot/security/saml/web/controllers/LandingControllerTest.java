package com.inbravo.spring.boot.security.saml.web.controllers;

import com.inbravo.spring.boot.security.saml.web.CommonTestSupport;
import com.inbravo.spring.boot.security.saml.web.TestConfig;
import com.inbravo.spring.boot.security.saml.web.controllers.LandingController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
public class LandingControllerTest extends CommonTestSupport {

  @InjectMocks
  private LandingController landingController;

  @Mock
  private View mockView;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mockMvc = standaloneSetup(landingController).setCustomArgumentResolvers(new MockArgumentResolver()).setSingleView(mockView).build();
  }

  @Test
  public void testAnonymousLanding() throws Exception {
    mockMvc.perform(get("/landing").session(mockHttpSession(true))).andExpect(status().isOk()).andExpect(model().attribute("username", USER_NAME))
        .andExpect(view().name("landing"));
  }

  private static class MockArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
      return methodParameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
        WebDataBinderFactory webDataBinderFactory) throws Exception {
      return CommonTestSupport.USER_DETAILS;
    }
  }

}
