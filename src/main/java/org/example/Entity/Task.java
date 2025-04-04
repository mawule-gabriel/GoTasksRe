package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@Data
@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String id;
    private String content;
    private Boolean completed;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @DynamoDbAttribute("completed")
    public Boolean getCompleted() {
        return completed != null ? completed : false;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed != null ? completed : false;
    }

    public boolean isCompleted() {
        return getCompleted();
    }
}