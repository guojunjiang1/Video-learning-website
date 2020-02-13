package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;
    private TeachplanMedia teachplanMedia;
    private String mediaId;//媒资文件ID
    private String mediaFileOriginalName;//媒资文件名称
    @Override
    public String toString() {
        return super.toString()+"TeachplanNode{" +
                "children=" + children +
                '}';
    }
}
