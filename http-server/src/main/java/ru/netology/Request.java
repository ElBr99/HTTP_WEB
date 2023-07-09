package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Request {
    private final String path;
    private final List<NameValuePair> params;

    public Request(String path) {
        int delimiter = path.indexOf("?");
        if (delimiter == -1) {
            this.path = path;
            this.params = new ArrayList<>();
        } else {
            this.path = path.substring(0, delimiter);
            this.params = URLEncodedUtils.parse(path.substring(delimiter + 1), StandardCharsets.UTF_8);
        }
    }

    public NameValuePair getQueryParam(String name) {
        return params
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();
    }

    public String getParamValue(String name) {
        return getQueryParam(name).getValue();
    }

    public List<NameValuePair> getQueryParams() {
        return params;
    }

    public String getPath() {
        return path;
    }

    public List<String> getHeaders() {
        return params
                .stream()
                .map(NameValuePair::getName)
                .collect(Collectors.toList());
    }
}