package com.example.demo.common.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamPortConfig {
    @Bean
    public IamportClient IamportClient(){
        return new IamportClient("0464502130540227","IwPKN6heL2a1kshpRyHl23uBxwQvK3Wyv9bqXUZTrQryxbKWMUohAhyepHOuV0L1gvOP1a8tsqyRV1sL");
    }
}
