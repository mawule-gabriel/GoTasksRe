package org.example.Config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint:}")
    private String dynamoDBEndpoint;

    @Value("${amazon.aws.region:us-east-1}")
    private String awsRegion;

    @Value("${amazon.aws.profile:default}")
    private String awsProfile;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        // Create credentials provider (try profile first, then fall back to default chain)
        AWSCredentialsProvider credentialsProvider;

        try {
            // Try to use the specified profile
            credentialsProvider = new ProfileCredentialsProvider(awsProfile);
            // Test if credentials are available from the profile
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            // If profile credentials fail, fall back to default chain
            credentialsProvider = new DefaultAWSCredentialsProviderChain();
        }

        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider);

        if (StringUtils.hasText(dynamoDBEndpoint)) {
            // When endpoint is specified (local DynamoDB or other endpoint)
            builder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, awsRegion));
        } else {
            // Use the standard region configuration
            builder.withRegion(awsRegion);
        }

        return builder.build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
    }
}