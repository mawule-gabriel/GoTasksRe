package org.example.Repository;

import org.example.Entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public TaskRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public Task save(Task task) {
        if (task.getCompleted() == null) {
            task.setCompleted(false);
        }
        System.out.println("Before saving to DynamoDB: " + task);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(task.getId()).build());
        item.put("content", AttributeValue.builder().s(task.getContent()).build());
        item.put("completed", AttributeValue.builder().bool(task.getCompleted()).build());

        PutItemRequest putRequest = PutItemRequest.builder()
                .tableName("Tasks")
                .item(item)
                .build();
        dynamoDbClient.putItem(putRequest);

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(task.getId()).build());
        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("Tasks")
                .key(key)
                .consistentRead(true)
                .build();
        GetItemResponse response = dynamoDbClient.getItem(getRequest);
        Task fetched = mapToTask(response.item());
        System.out.println("Fetched immediately after save: " + fetched);
        if (fetched == null || !fetched.getCompleted().equals(task.getCompleted())) {
            System.out.println("Save failed! Expected completed=" + task.getCompleted() + ", got " + (fetched != null ? fetched.getCompleted() : "null"));
        }
        return task;
    }

    public List<Task> findAll() {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Tasks")
                .consistentRead(true)
                .build();
        ScanResponse response = dynamoDbClient.scan(scanRequest);
        List<Task> tasks = response.items().stream()
                .map(this::mapToTask)
                .collect(Collectors.toList());
        System.out.println("Tasks from DynamoDB: " + tasks);
        return tasks;
    }

    public Optional<Task> findById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());
        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("Tasks")
                .key(key)
                .consistentRead(true)
                .build();
        GetItemResponse response = dynamoDbClient.getItem(getRequest);
        Task task = mapToTask(response.item());
        return Optional.ofNullable(task);
    }

    public void deleteById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());
        DeleteItemRequest deleteRequest = DeleteItemRequest.builder()
                .tableName("Tasks")
                .key(key)
                .build();
        dynamoDbClient.deleteItem(deleteRequest);
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    private Task mapToTask(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }
        Task task = new Task();
        task.setId(item.get("id").s());
        task.setContent(item.get("content").s());
        task.setCompleted(item.get("completed") != null ? Boolean.valueOf(item.get("completed").bool()) : false);
        return task;
    }
}