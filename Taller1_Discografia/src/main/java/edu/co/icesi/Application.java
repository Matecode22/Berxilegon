package edu.co.icesi;

import edu.co.icesi.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    private static final ApplicationContext context =
            new AnnotationConfigApplicationContext(
                    AppConfig.class
            );
    public static ApplicationContext getContext() {
        return context;
    }

}
