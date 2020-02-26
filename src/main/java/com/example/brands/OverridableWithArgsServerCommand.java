package com.example.brands;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverridableWithArgsServerCommand<T extends BasicConfiguration> extends ConfiguredCommand<T> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OverridableWithArgsServerCommand.class);
    
    protected OverridableWithArgsServerCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);

        subparser.addArgument("--brokerhost")
                .dest("brokerhost")
                .type(String.class)
                .required(false)
                .help("The host name of the broker server");

        subparser.addArgument("--brokerport")
                .dest("brokerport")
                .type(String.class)
                .required(false)
                .help("The port used to connect to the broker");

        subparser.addArgument("--brokeruser")
                .dest("brokeruser")
                .type(String.class)
                .required(false)
                .help("The user used to connect to the broker");

        subparser.addArgument("--brokerpass")
                .dest("brokerpass")
                .type(String.class)
                .required(false)
                .help("The user password used to connect to the broker");
    }

    // function will attempt to provide glue between overriding config with cmd args and starting http server
    @Override
    protected void run(Bootstrap<T> bootstrap, Namespace namespace, T config) throws Exception {
        LOGGER.debug(String.format("Config before updating: broker host: %s, db port: %s, user: %s",
                config.getHost(), config.getPort(), config.getUsername()));

        config.sethost(namespace.getString("brokerhost"));
        config.setPort(namespace.getString("brokerport"));
        config.setPassword(namespace.getString("brokerpass"));
        config.setUsername(namespace.getString("brokeruser"));

        LOGGER.debug(String.format("Config after before running serverSandbox command: broker host: %s, db port: %s, user: %s",
                config.getHost(), config.getPort(), config.getUsername()));

        Environment environment = new Environment(bootstrap.getApplication().getName(),
                bootstrap.getObjectMapper(),
                bootstrap.getValidatorFactory(),
                bootstrap.getMetricRegistry(),
                bootstrap.getClassLoader(),
                bootstrap.getHealthCheckRegistry(),
                config);

        config.getMetricsFactory().configure(environment.lifecycle(), bootstrap.getMetricRegistry());
        config.getServerFactory().configure(environment);
        bootstrap.run(config, environment);
        bootstrap.getApplication().run(config, environment); // run application specific
        LOGGER.debug("Attempt to start server... run server command");

        // the below code was taken (and slightly modified) from io.dropwizard.cli.ServerCommand,
        // note that we needed to re-implement to prevent the config being deserialized again by the ConfiguredCommand base object
        Server server = config.getServerFactory().build(environment);

        try {
            server.addLifeCycleListener(new OverridableWithArgsServerCommand.LifeCycleListener());
            this.cleanupAsynchronously();
            server.start();
        } catch (Exception startException) {
            LOGGER.error("Unable to start server, shutting down", startException);

            try {
                server.stop();
            } catch (Exception stopException) {
                LOGGER.warn("Failure during stop server", stopException);
            }

            try {
                this.cleanup();
            } catch (Exception cleanupException) {
                LOGGER.warn("Failure during cleanup", cleanupException);
            }

            throw startException;
        }
        
        LOGGER.info(String.format("Config after server has been started: broker host: %s, db port: %s, user: %s",
                config.getHost(), config.getPort(), config.getUsername()));
    }

    private class LifeCycleListener extends AbstractLifeCycle.AbstractLifeCycleListener {
        private LifeCycleListener() {
        }

        public void lifeCycleStopped(LifeCycle event) {
            OverridableWithArgsServerCommand.this.cleanup();
        }
    }
}
