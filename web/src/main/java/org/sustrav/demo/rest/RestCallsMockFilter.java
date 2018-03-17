package org.sustrav.demo.rest;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Mocking implementation
 */
@ConditionalOnProperty(name = "mock.active", havingValue = "true")
@Component
public class RestCallsMockFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestCallsMockFilter.class);


    private final RestCallsMockConfiguration configuration;

    @Autowired
    public RestCallsMockFilter(RestCallsMockConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("REST API mocking is active, " + configuration);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String path = httpServletRequest.getServletPath();
        if (!path.startsWith(configuration.getStartWith())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String resourceName = path.substring(configuration.getStartWith().length());
        resourceName += httpServletRequest.getParameterMap().size() > 0 ? "/" : "";
        for (String parName : httpServletRequest.getParameterMap().keySet()) {
            String parValue = httpServletRequest.getParameter(parName);
            String delim = resourceName.endsWith("/") ? "" : "_";
            resourceName += delim + parName + "-" + parValue;
        }

        if (!httpServletRequest.getMethod().equalsIgnoreCase("GET"))
            resourceName += "." + httpServletRequest.getMethod().toLowerCase();

        resourceName = configuration.getLocation() + resourceName + ".json";


        try {
            servletResponse.setContentType("application/json");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceName);
            IOUtils.copy(is, servletResponse.getOutputStream());
            IOUtils.closeQuietly(is);
        } catch (Exception ex) {
            LOGGER.info("Mock for request {} not found", resourceName);
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
