package com.inbravo.spring.boot.security.saml.web.controllers;

import com.inbravo.spring.boot.security.saml.web.CommonTestSupport;
import com.inbravo.spring.boot.security.saml.web.TestConfig;
import com.inbravo.spring.boot.security.saml.web.controllers.SSOController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
public class SSOControllerTest extends CommonTestSupport {

  private static final Set<String> IDPS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("idp1", "idp2", "idp3")));

  @InjectMocks
  SSOController ssoController;

  @Mock
  private MetadataManager metadata;

  @Mock
  private View mockView;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mockMvc = standaloneSetup(ssoController).setSingleView(mockView).build();
  }

  @Test
  public void testIdpSelection() throws Exception {
    mockMvc.perform(get("/saml/idpSelection").session(mockHttpSession(false))).andExpect(status().isOk()).andExpect(view().name("redirect:/landing"));
  }

  @Test
  public void testIdpSelectionWithoutForwarding() throws Exception {
    mockMvc.perform(get("/saml/idpSelection").session(mockAnonymousHttpSession())).andExpect(status().isOk()).andExpect(view().name("redirect:/"));
  }

  @Test
  public void testIdpSelectionWithForwarding() throws Exception {
    // given
    when(metadata.getIDPEntityNames()).thenReturn(IDPS);

    // when / then
    mockMvc
        .perform(get("/saml/idpSelection").session(mockAnonymousHttpSession()).requestAttr("javax.servlet.forward.request_uri", "http://forward.to"))
        .andExpect(status().isOk()).andExpect(model().attribute("idps", IDPS)).andExpect(view().name("saml/idpselection"));
  }

}
