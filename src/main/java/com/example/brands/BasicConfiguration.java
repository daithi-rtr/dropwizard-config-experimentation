package com.example.brands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.ServerFactory;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BasicConfiguration extends Configuration {
    private final int defaultSize;
    private String username;
    private String password;
    private String host;
    private String port;

    @JsonCreator
    public BasicConfiguration(@JsonProperty("defaultSize") int defaultSize, @JsonProperty("username") String username, @JsonProperty("password") String password,
                                     @JsonProperty("host") String host, @JsonProperty("port") String port) {

        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.defaultSize = defaultSize;
    }

    public int getDefaultSize() {
        return defaultSize;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getHost() {
        return host;
    }
    public String getPort() {
        return port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void sethost(String host) {
        this.host = host;
    }

    public void setConnectorPort(String port){
        DefaultServerFactory defaultServerFactory = (DefaultServerFactory)this.getServerFactory();
        List<ConnectorFactory> adminConnectors = defaultServerFactory.getAdminConnectors();


    }
    public void setPort(String port) {
        this.port = port;
    }
}
