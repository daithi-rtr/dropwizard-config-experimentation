package com.example.brands;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.ArrayList;

public class IntroductionApplication extends Application<BasicConfiguration> {

    private static final IntroductionApplication app = new IntroductionApplication();

    public static void main(String[] args) throws Exception {
        new IntroductionApplication().run(args);
    }

    @Override
    public void run(BasicConfiguration basicConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new BrandResource(basicConfiguration.getDefaultSize(),
                new BrandRepository(
                        new ArrayList<Brand>() {{
                            add(new Brand(new Long(123), "dress0"));
                            add(new Brand(new Long(12), "dress1"));
                            add(new Brand(new Long(1234), "dress2"));
                            add(new Brand(new Long(321), "dress3"));
                            add(new Brand(new Long(2341), "dress4"));
                        }})
        ));
    }

    @Override
    public void initialize(Bootstrap<BasicConfiguration> bootstrap) {
        bootstrap.addCommand(new OverridableWithArgsServerCommand<BasicConfiguration>("serverTest",
                "Set the parameters for sandbox test."));
        super.initialize(bootstrap);
    }
}
