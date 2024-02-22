package com.myorg.aiml.config;

import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import lombok.Data;
import org.springframework.context.ApplicationContext;

import java.net.InetSocketAddress;
import java.util.List;

@Data
public class SessionBuilderConfigurerModel {
    List<InetSocketAddress> addressList;
    DriverConfigLoader driverConfigLoader;
    String keyspaceName;
    String dataCenter;
    String userName;
    String password;
    String prefix = "";
    ApplicationContext context;
}
