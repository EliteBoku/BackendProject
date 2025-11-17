package com.elitelms.lms;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class AuthDebugProbe {

    @Autowired
    private ApplicationContext ctx;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        System.out.println("=== AuthDebugProbe START ===");

        // 1) Lista providers registrados
        String[] providerNames = ctx.getBeanNamesForType(AuthenticationProvider.class);
        System.out.println("AuthenticationProvider beans found: " + providerNames.length);
        for (String name : providerNames) {
            Object bean = ctx.getBean(name);
            Class<?> beanClass = bean.getClass();
            System.out.println("Provider bean: " + name + " -> " + beanClass.getName() + " | proxy? " + AopUtils.isAopProxy(bean));

            // métodos llamados 'authenticate'
            for (Method m : beanClass.getMethods()) {
                if ("authenticate".equals(m.getName())) {
                    System.out.println("  has authenticate method: " + m);
                }
            }

            // campos que son AuthenticationManager (sospechosos)
            for (Field f : beanClass.getDeclaredFields()) {
                if (AuthenticationManager.class.isAssignableFrom(f.getType())) {
                    System.out.println("  DECLARES AuthenticationManager field: " + f.getName() + " (type: " + f.getType().getName() + ")");
                }
            }
        }

        // 2) Buscar cualquier bean que tenga un campo AuthenticationManager (más amplio)
        System.out.println("--- Scanning ALL beans for fields typed AuthenticationManager ---");
        for (String name : ctx.getBeanDefinitionNames()) {
            Object bean = ctx.getBean(name);
            Class<?> beanClass = bean.getClass();
            for (Field f : beanClass.getDeclaredFields()) {
                if (AuthenticationManager.class.isAssignableFrom(f.getType())) {
                    System.out.println("Bean: " + name + " -> " + beanClass.getName() + " declares field: " + f.getName());
                }
            }
        }

        System.out.println("=== AuthDebugProbe END ===");
    }
}
