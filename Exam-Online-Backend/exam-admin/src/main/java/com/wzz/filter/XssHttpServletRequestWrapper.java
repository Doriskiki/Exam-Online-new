package com.wzz.filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return clean(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        String[] cleaned = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            cleaned[i] = clean(values[i]);
        }
        return cleaned;
    }

    @Override
    public String getHeader(String name) {
        return clean(super.getHeader(name));
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }
        return Jsoup.clean(value, Safelist.none());
    }
}
