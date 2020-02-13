package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置GridFs存储文件
@Configuration
public class GridFsTest {
    @Value("${spring.data.mongodb.database}")
    private String db;

    //注入一个Grid中获取文件的bean
    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient){
        MongoDatabase database=mongoClient.getDatabase(db);
        GridFSBucket bucket= GridFSBuckets.create(database);
        return bucket;
    }
}
