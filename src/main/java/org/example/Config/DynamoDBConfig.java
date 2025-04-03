package org.example.Config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class DynamoDBConfig {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDBConfig.class);

    @Value("${amazon.dynamodb.endpoint:}")
    private String dynamoDBEndpoint;

    @Value("${amazon.aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        logger.info("Initializing DynamoDB client with region: {}, endpoint: {}",
                awsRegion,
                StringUtils.hasText(dynamoDBEndpoint) ? dynamoDBEndpoint : "default");


        AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider);

        if (StringUtils.hasText(dynamoDBEndpoint)) {
            logger.info("Using custom DynamoDB endpoint: {}", dynamoDBEndpoint);
            builder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, awsRegion));
        } else {
            logger.info("Using default AWS region: {}", awsRegion);
            builder.withRegion(awsRegion);
        }

        return builder.build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
    }
}