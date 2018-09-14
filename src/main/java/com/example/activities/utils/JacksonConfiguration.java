package com.example.activities.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;

@Configuration  
//@Profile("test")
public class JacksonConfiguration {

//	@Autowired
//	ApplicationContext applicationContext;
//	
//    @Bean
//    public HandlerInstantiator handlerInstantiator() {
//        return new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory());
//    }

//    @Bean
//    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(HandlerInstantiator handlerInstantiator) {
//        Jackson2ObjectMapperBuilder result = new Jackson2ObjectMapperBuilder();
//        result.handlerInstantiator(handlerInstantiator);
//        return result;
//    }

//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder objectMapperBuilder) {
//        return new MappingJackson2HttpMessageConverter(objectMapperBuilder.build());
//    }

//    @Bean
//    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
//        List<HttpMessageConverter<?>> messageConverterList = new ArrayList<>();
//        messageConverterList.add(mappingJackson2HttpMessageConverter);
//        return new RestTemplate(messageConverterList);
//    }
    
    @Bean
    public HandlerInstantiator handlerInstantiator(ApplicationContext applicationContext) {
        return new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory());
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder(HandlerInstantiator handlerInstantiator) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.handlerInstantiator(handlerInstantiator);
        return builder;
    }
}