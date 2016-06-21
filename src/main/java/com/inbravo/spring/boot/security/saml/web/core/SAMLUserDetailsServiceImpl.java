package com.inbravo.spring.boot.security.saml.web.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

  // Logger
  private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

  public Object loadUserBySAML(final SAMLCredential credential) throws UsernameNotFoundException {

    // The method is supposed to identify local account of user referenced by
    // data in the SAML assertion and return UserDetails object describing the user.
    final String userID = credential.getNameID().getValue();

    LOG.info(userID + " is logged in with credentials: " + credential.getAttributes());

    final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    /* Add role */
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    // In a real scenario, this implementation has to locate user in a arbitrary
    // dataStore based on information present in the SAMLCredential and
    // returns such a date in a form of application specific UserDetails object.
    return new User(userID, "<abc123>", true, true, true, true, authorities);
  }
}
