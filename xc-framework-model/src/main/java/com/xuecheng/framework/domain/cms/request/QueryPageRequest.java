package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
//分页查询页面信息,接收页面查询的传递参数
@Data
public class QueryPageRequest {
    @ApiModelProperty("站点id")
    private String siteId;//站点id
    @ApiModelProperty("页面ID")
    private String pageId;//页面id
    @ApiModelProperty("页面名称")
    private String pageName;//页面名称
    @ApiModelProperty("页面别名")
    private String pageAliase;//页面别名
    @ApiModelProperty("模板ID")
    private String templateId;//模板id
}
