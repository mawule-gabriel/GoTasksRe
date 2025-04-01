//package org.example.Config;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
//import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
//import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
//import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
//import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
//import com.amazonaws.services.dynamodbv2.model.TableStatus;
//import org.example.Entity.Task;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DynamoDBConfig {
//
//    @Value("${amazon.dynamodb.endpoint}")
//    private String dynamoDBEndpoint;
//
//    @Value("${amazon.aws.accesskey}")
//    private String awsAccessKey;
//
//    @Value("${amazon.aws.secretkey}")
//    private String awsSecretKey;
//
//    @Value("${amazon.aws.region}")
//    private String awsRegion;
//
//    @Bean
//    public AmazonDynamoDB amazonDynamoDB() {
//        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
//                .standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, awsRegion))
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
//                .build();
//
//        // Create the Tasks table if it doesn't exist
//        createTableIfNotExists(amazonDynamoDB);
//
//        return amazonDynamoDB;
//    }
//
//    @Bean
//    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
//        return new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
//    }
//
//    private void createTableIfNotExists(AmazonDynamoDB amazonDynamoDB) {
//        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
//        CreateTableRequest tableRequest = mapper.generateCreateTableRequest(Task.class)
//                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
//
//        try {
//            amazonDynamoDB.createTable(tableRequest);
//            // Wait for the table to become active
//            waitForTableToBeActive(amazonDynamoDB, "Tasks", 60);
//        } catch (ResourceInUseException e) {
//            // Table already exists, no action needed
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException("Interrupted while waiting for table creation", e);
//        }
//    }
//
//    private void waitForTableToBeActive(AmazonDynamoDB amazonDynamoDB, String tableName, int timeoutSeconds) throws InterruptedException {
//        long startTime = System.currentTimeMillis();
//        long endTime = startTime + (timeoutSeconds * 1000L);
//
//        while (System.currentTimeMillis() < endTime) {
//            DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
//            DescribeTableResult result = amazonDynamoDB.describeTable(request);
//            String status = result.getTable().getTableStatus();
//
//            if (TableStatus.ACTIVE.toString().equals(status)) {
//                return;
//            }
//
//            Thread.sleep(1000);
//        }
//
//        throw new RuntimeException("Timeout waiting for table " + tableName + " to become active");
//    }
//}

package org.example.Config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String awsAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String awsSecretKey;

    @Value("${amazon.aws.region}")
    private String awsRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
    }
}