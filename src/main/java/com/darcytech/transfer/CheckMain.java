package com.darcytech.transfer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.darcytech.transfer.check.CheckCustomerJob;
import com.darcytech.transfer.check.CheckTradeJob;
import com.darcytech.transfer.check.OnlyTradeJob;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by darcy on 2015/12/24.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EntityScan("com.darcytech.transfer.model")
@EnableJpaRepositories("com.darcytech.transfer.dao")
@EnableTransactionManagement
public class CheckMain {

    private static final Logger logger = LoggerFactory.getLogger(CheckMain.class);

    @Value("${es.cluster.name}")
    private String esClusterName;

    @Value("${es.transport.addresses}")
    private String[] esAddresses;

    private int esDefaultPort = 9300;

    public static void main(String[] args) {
        try {
            ApplicationContext context = SpringApplication.run(TransferMain.class, args);

            logger.info("start testing customer......");
            CheckCustomerJob checkCustomerJob = context.getBean(CheckCustomerJob.class);
            checkCustomerJob.test();
            logger.info("testing customer complete.");

            logger.info("start testing trade......");
            CheckTradeJob checkTradeJob = context.getBean(CheckTradeJob.class);
            checkTradeJob.test();
            logger.info("testing trade complete.");

            logger.info("showing only trade customer......");
            OnlyTradeJob onlyTradeJob = context.getBean(OnlyTradeJob.class);
            onlyTradeJob.showDirtyCustomer();
            logger.info("showing only trade customer complete.");

            logger.info("Application is closing......");
            ((ConfigurableApplicationContext) context).close();
            logger.info("Application is closed.");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Bean
    public Client esClient() throws UnknownHostException {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", esClusterName).build();
        TransportClient client = new TransportClient(settings);

        for (String transport : esAddresses) {
            String[] parts = transport.trim().split(":");
            String host = parts[0];
            int port = esDefaultPort;
            if (parts.length == 2) {
                port = Integer.parseInt(parts[1]);
            }
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        }
        return client;
    }

    @Bean
    public HttpClient httpClient() {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .build();

        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();

        return httpClient;
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        return objectMapper;
    }

    @Bean
    public TaskExecutor transferExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        return executor;
    }

}
