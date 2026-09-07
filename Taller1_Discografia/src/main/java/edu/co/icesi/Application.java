package edu.co.icesi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    private static final ApplicationContext context =
            new ClassPathXmlApplicationContext(
                    "applicationContext.xml"
            );
    public static ApplicationContext getContext() {
        return context;
    }

}
