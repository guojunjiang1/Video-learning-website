package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class EsCourseServiceImpl implements EsCourseService {
    @Autowired
    private RestHighLevelClient client;//高等级的客户端
    @Autowired
    private RestClient restClient;//低等级的客户端
    @Value("${xuecheng.course.index}")
    private String index;
    @Value("${xuecheng.course.type}")
    private String type;
    @Value("${xuecheng.course.source_field}")
    private String source_field;
    @Value("${xuecheng.media.index}")
    private String media_index;
    @Value("${xuecheng.media.type}")
    private String media_type;
    @Value("${xuecheng.media.source_field}")
    private String media_source_field;

    @Override
    //课程搜索
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置分页参数
        if (page<=0){
            page=1;
        }
        if (size<=0){
            size=4;
        }
        //计算出记录起始下标
        int from  = (page-1)*size;
        searchSourceBuilder.from(from);//起始记录下标，从0开始
        searchSourceBuilder.size(size);//每页显示的记录数

        //boolean查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //搜索方式条件
        //一：关键字搜索(多词检索来完成(关键词分词并指定多个域))
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
            MultiMatchQueryBuilder a = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("30%").field("name", 10);
            boolQueryBuilder.must(a);
        }
        //二：根据课程分类(过滤器的方式搜索,精准匹配)
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())){
            //第二级存在时，直接过滤第二级
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }else {
            //二级不存在，看一级存不存在
            if (StringUtils.isNotEmpty(courseSearchParam.getMt())){
                boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
            }
        }
        //三：根据难度等级(过滤器的方式，精准匹配)
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }

        searchSourceBuilder.query(boolQueryBuilder);
        //设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        String[] split = source_field.split(",");
        searchSourceBuilder.fetchSource(split,new String[]{});

        //高亮设置
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //请求搜索
        searchRequest.source(searchSourceBuilder);
        //执行搜索,向ES发起http请求
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //得到文档
        SearchHit[] searchHits = hits.getHits();
        List<CoursePub> list=new ArrayList<>();
        for(SearchHit hit:searchHits) {
           CoursePub coursePub=new CoursePub();
            //源文档
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //取出CourseId
            String id = (String)sourceAsMap.get("id");
            coursePub.setId(id);
            //取出name
            String name = (String) sourceAsMap.get("name");
            //取出高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields!=null){
                if (highlightFields.containsKey("name")){
                    HighlightField name1 = highlightFields.get("name");
                    if (name1!=null){
                        Text[] fragments = name1.getFragments();
                        StringBuilder stringBuilder=new StringBuilder();
                        for (Text xx:fragments){
                            stringBuilder.append(xx);
                        }
                        name=stringBuilder.toString();//如果name存在高亮字段给name重新赋值
                    }
                }
            }
            coursePub.setName(name);
            //图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            //价格
            Double price = null;
            try {
                if(sourceAsMap.get("price")!=null ){
                    price = (Double) sourceAsMap.get("price");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            coursePub.setPrice(price);
            //旧价格
            Double price_old = null;
            try {
                if(sourceAsMap.get("price_old")!=null ){
                    price_old = (Double) sourceAsMap.get("price_old");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            coursePub.setPrice_old(price_old);
            list.add(coursePub);
        }
        QueryResult<CoursePub> queryResult=new QueryResult<>();
        queryResult.setTotal(totalHits);
        queryResult.setList(list);
        QueryResponseResult<CoursePub> responseResult=new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }

    @Override
    //根据课程id查询课程信息
    public Map<String, CoursePub> getall(String id) {
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //指定type
        searchRequest.types(type);

        //定义SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置使用termQuery
        searchSourceBuilder.query(QueryBuilders.termQuery("id",id));
        searchRequest.source(searchSourceBuilder);
        //最终要返回的课程信息
        Map<String,CoursePub> map = new HashMap<>();
        try {
            SearchResponse search = client.search(searchRequest);
            SearchHits hits = search.getHits();
            SearchHit[] searchHits = hits.getHits();
            for(SearchHit hit:searchHits){
                CoursePub coursePub = new CoursePub();
                //获取源文档的内容
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //课程id
                String courseId = (String) sourceAsMap.get("id");
                String name = (String) sourceAsMap.get("name");
                String grade = (String) sourceAsMap.get("grade");
                String charge = (String) sourceAsMap.get("charge");
                String pic = (String) sourceAsMap.get("pic");
                String description = (String) sourceAsMap.get("description");
                String teachplan = (String) sourceAsMap.get("teachplan");
                coursePub.setId(courseId);
                coursePub.setName(name);
                coursePub.setPic(pic);
                coursePub.setCharge(charge);
                coursePub.setGrade(grade);
                coursePub.setTeachplan(teachplan);
                coursePub.setDescription(description);
                map.put(courseId,coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    //根据课程计划id查询课程计划与媒资视频的数据
    public QueryResponseResult<TeachplanMediaPub> getmedia(String[] strings) {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest(media_index);
        //指定类型
        searchRequest.types(media_type);
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //搜索方式
        //根据id查询
        //定义id
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id",strings));

        //设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        String[] split = media_source_field.split(",");
        searchSourceBuilder.fetchSource(split,new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索,向ES发起http请求
        SearchResponse searchResponse = null;
        long total=0;
        List<TeachplanMediaPub> teachplanMediaPubList=new ArrayList<>();
        try {
            //执行搜索
            SearchResponse search = client.search(searchRequest);
            SearchHits hits = search.getHits();
            total = hits.totalHits;
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出课程计划媒资信息
                String courseid = (String) sourceAsMap.get("courseid");
                String media_id = (String) sourceAsMap.get("media_id");
                String media_url = (String) sourceAsMap.get("media_url");
                String teachplan_id = (String) sourceAsMap.get("teachplan_id");
                String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setMediaUrl(media_url);
                teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
                teachplanMediaPub.setMediaId(media_id);
                teachplanMediaPub.setTeachplanId(teachplan_id);
                teachplanMediaPubList.add(teachplanMediaPub);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        QueryResult<TeachplanMediaPub> queryResult=new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        queryResult.setTotal(total);
        QueryResponseResult<TeachplanMediaPub> queryResponseResult=new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
