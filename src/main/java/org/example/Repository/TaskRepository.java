package org.example.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.example.Entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public TaskRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Task save(Task task) {
        dynamoDBMapper.save(task);
        return task;
    }

    public List<Task> findAll() {
        return dynamoDBMapper.scan(Task.class, new DynamoDBScanExpression());
    }

    public Optional<Task> findById(String id) {
        Task task = dynamoDBMapper.load(Task.class, id);
        return Optional.ofNullable(task);
    }

    public void deleteById(String id) {
        Task task = dynamoDBMapper.load(Task.class, id);
        if (task != null) {
            dynamoDBMapper.delete(task);
        }
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }
}