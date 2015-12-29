package com.darcytech.transfer;

import java.io.IOException;
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
import com.darcytech.transfer.job.ActionRecordTransferJob;
import com.darcytech.transfer.job.CorrectCustomerDetailJob;
import com.darcytech.transfer.job.CustomerTransferJob;
import com.darcytech.transfer.job.IndexMappingCreateJob;
import com.darcytech.transfer.job.MarketingJobResultOrderTransferJob;
import com.darcytech.transfer.job.MarketingJobResultTransferJob;
import com.darcytech.transfer.job.RefundTransferJob;
import com.darcytech.transfer.job.TradeRateTransferJob;
import com.darcytech.transfer.job.TradeTransferJob;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by darcy on 2015/11/30.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EntityScan("com.darcytech.transfer.model")
@EnableJpaRepositories("com.darcytech.transfer.dao")
@EnableTransactionManagement
public class TransferMain {

    private static final Logger logger = LoggerFactory.getLogger(TransferMain.class);

    @Value("${es.cluster.name}")
    private String esClusterName;

    @Value("${es.transport.addresses}")
    private String[] esAddresses;

    private int esDefaultPort = 9300;

    public static void main(String[] args) {

        try {
            ApplicationContext context = SpringApplication.run(TransferMain.class, args);
            String mode = args[0];

            if ("clean".equals(mode)) {
                runClean(context);
            } else if ("check".equals(mode)) {
                runCheck(context);
            } else if (mode == null || "transfer".equals(mode)) {
                runTransfer(context);
            }

            logger.info("Application is closing......");
            ((ConfigurableApplicationContext) context).close();
            logger.info("Application is closed.");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private static void runClean(ApplicationContext context) throws Exception {

        logger.info("start correcting repeat customer detail ......");
        CorrectCustomerDetailJob correctCustomerDetailJob = context.getBean(CorrectCustomerDetailJob.class);
        correctCustomerDetailJob.doCorrectCustomerDetail();
        logger.info("correcting repeat customer detail complete.");
    }

    private static void runCheck(ApplicationContext context) throws Exception {

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
    }

    private static void runTransfer(ApplicationContext context) throws Exception {

        logger.info("start es to es transferring......");
        logger.info("start creating index map......");
        IndexMappingCreateJob indexMappingCreateJob = context.getBean(IndexMappingCreateJob.class);
        indexMappingCreateJob.createElasticIndexMapping();
        logger.info("creating index map complete.");

        logger.info("start transferring customers......");
        CustomerTransferJob customerTransferJob = context.getBean(CustomerTransferJob.class);
        customerTransferJob.doTransfer();
        logger.info("transferring customer complete.");

        logger.info("start transferring trades and orders......");
        TradeTransferJob tradeTransferJob = context.getBean(TradeTransferJob.class);
        tradeTransferJob.doTransfer();
        logger.info("transferring trades and orders complete.");

        logger.info("start transferring traderate......");
        TradeRateTransferJob tradeRateTransferJob = context.getBean(TradeRateTransferJob.class);
        tradeRateTransferJob.doTransfer();
        logger.info("transferring traderate complete.");

        logger.info("transferring es to es complete.");

        logger.info("start transferring es to mySQL......");
        logger.info("start transferring marketing job result......");
        MarketingJobResultTransferJob marketingJobResultJob = context.getBean(MarketingJobResultTransferJob.class);
        marketingJobResultJob.doTransfer();
        logger.info("transferring marketing job result complete.");

        logger.info("start transferring marketing job result order......");
        MarketingJobResultOrderTransferJob marketingJobResultOrderJob = context.getBean(MarketingJobResultOrderTransferJob.class);
        marketingJobResultOrderJob.doTransfer();
        logger.info("transferring marketing job result order complete.");

        logger.info("start transferring action record......");
        ActionRecordTransferJob actionRecordTransferJob = context.getBean(ActionRecordTransferJob.class);
        actionRecordTransferJob.doTransfer();
        logger.info("transferring action record complete.");

        logger.info("start transferring refund......");
        RefundTransferJob refundTransferJob = context.getBean(RefundTransferJob.class);
        refundTransferJob.doTransfer();
        logger.info("transferring refund complete.");
        logger.info("transferring es to mySQL complete.");
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
