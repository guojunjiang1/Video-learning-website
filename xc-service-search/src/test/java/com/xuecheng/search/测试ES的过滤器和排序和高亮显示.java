package com.xuecheng.search;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 测试ES的过滤器和排序和高亮显示 {
    @Autowired
    private RestHighLevelClient client;//高等级的客户端
    @Autowired
    private RestClient restClient;//低等级的客户端

    @Test
    //过滤器(对搜索的结果进行过滤,可以过滤类似于精准查询和范围查询的数据)
    //和布尔查询配合使用
    public void testFilter() throws Exception {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //boolQuery搜索方式
        //先定义一个MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //定义一个boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        //定义过虑器
        //过滤studyModel是201001的文档
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        //过滤price在60-100之间
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        searchSourceBuilder.query(boolQueryBuilder);
        //设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        //搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SearchHit hit:searchHits){
            //文档的主键
            String id = hit.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            //由于前边设置了源文档字段过虑，这时description是取不到的
            String description = (String) sourceAsMap.get("description");
            //学习模式
            String studymodel = (String) sourceAsMap.get("studymodel");
            //价格
            Double price = (Double) sourceAsMap.get("price");
            //日期
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(id);
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
            System.out.println(price);
            System.out.println(timestamp);
        }
    }

    //排序，对操作结果进行排序
    @Test
    public void testSort() throws Exception{
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //boolQuery搜索方式
        //定义一个boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //定义过虑器
        //过滤price在0-100之间
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));

        searchSourceBuilder.query(boolQueryBuilder);
        //添加排序
        //根据studyModel字段降序,studyModel相等对price进行升序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        //设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        //搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SearchHit hit:searchHits){
            //文档的主键
            String id = hit.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            //由于前边设置了源文档字段过虑，这时description是取不到的
            String description = (String) sourceAsMap.get("description");
            //学习模式
            String studymodel = (String) sourceAsMap.get("studymodel");
            //价格
            Double price = (Double) sourceAsMap.get("price");
            //日期
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
            System.out.println(price);
            System.out.println(timestamp);
            System.out.println("------");
        }
    }

    //高亮显示：可以将搜索结果一个或多个字突出显示
    @Test
    public void testHighlight() throws Exception {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //boolQuery搜索方式
        //先定义一个MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发框架", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);

        //定义一个boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        //定义过虑器
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));

        searchSourceBuilder.query(boolQueryBuilder);
        //设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"description","name","studymodel","price","timestamp"},new String[]{});

        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        //搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SearchHit hit:searchHits){
            System.out.println("-------");
            //文档的主键
            String id = hit.getId();
            //取出高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null){
                System.out.println("高亮字段");
                //取出name高亮字段
                HighlightField nameHighlightField = highlightFields.get("name");
                if(nameHighlightField!=null){
                    Text[] fragments = nameHighlightField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for(Text text:fragments){
                        stringBuffer.append(text);
                    }
                    String name = stringBuffer.toString();
                    System.out.println(name);
                }
                //取出description高亮字段
                HighlightField description = highlightFields.get("description");
                if(description!=null){
                    Text[] fragments = description.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for(Text text:fragments){
                        stringBuffer.append(text);
                    }
                    String name = stringBuffer.toString();
                    System.out.println(name);
                }
            }
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //源文档的name字段内容
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            //学习模式
            String studymodel = (String) sourceAsMap.get("studymodel");
            //价格
            Double price = (Double) sourceAsMap.get("price");
            //日期
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println("普通获取字段：");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
            System.out.println(price);
            System.out.println(timestamp);
        }
    }
}
