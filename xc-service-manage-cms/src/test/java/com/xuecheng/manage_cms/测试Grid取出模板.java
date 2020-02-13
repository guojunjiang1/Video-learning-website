package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 测试Grid取出模板 {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Test
    public void test() throws Exception{
        GridFSFile one = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e05dafef94c401ddc35f18e")));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(one.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(one, gridFSDownloadStream);
        String string = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(string);
    }
}
