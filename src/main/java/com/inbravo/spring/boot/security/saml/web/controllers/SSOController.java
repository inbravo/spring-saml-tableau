package com.inbravo.spring.boot.security.saml.web.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/saml")
public class SSOController {

  // Logger
  private static final Logger LOG = LoggerFactory.getLogger(SSOController.class);

  @Autowired
  private MetadataManager metadata;

  @RequestMapping(value = "/idpSelection", method = RequestMethod.GET)
  public String idpSelection(final HttpServletRequest request, final Model model) throws MetadataProviderException {

    if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      LOG.warn("The current user is already logged.");
      return "redirect:/landing";
    } else {

      /* Check if it is a forward request */
      if (isForwarded(request)) {

        LOG.info("idpSelection: metadata: " + metadata);
        LOG.info("idpSelection: HostedSPName: " + metadata.getHostedSPName());
        LOG.info("idpSelection: DefaultIDP: " + metadata.getDefaultIDP());

        final Set<String> idps = metadata.getIDPEntityNames();

        for (final String idp : idps) {
          LOG.info("Configured Identity Provider for SSO: " + idp);
        }

        /* Set all IDP in model */
        model.addAttribute("idps", idps);
        return "saml/idpselection";
      } else {
        LOG.warn("Direct accesses to '/idpSelection' route are not allowed");
        return "redirect:/";
      }
    }
  }

  /*
   * Checks if an HTTP request is forwarded from servlet.
   */
  private boolean isForwarded(HttpServletRequest request) {

    /* If forward request uri is null */
    if (request.getAttribute("javax.servlet.forward.request_uri") == null)
      return false;
    else
      return true;
  }

}
