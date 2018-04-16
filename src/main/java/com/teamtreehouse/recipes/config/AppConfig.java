package com.teamtreehouse.recipes.config;

import com.teamtreehouse.recipes.util.ReferrerInterceptor;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Rumy on 3/5/2018.
 */

@Configuration
@PropertySource("application.properties")
public class AppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private Environment env;

   @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ReferrerInterceptor());
    }


    @Bean
    public Hashids hashids() {
        return new Hashids(env.getProperty("recipes.hash.salt"),8);
    }

}
