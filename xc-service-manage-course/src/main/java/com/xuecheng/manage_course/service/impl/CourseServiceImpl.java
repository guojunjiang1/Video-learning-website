package com.xuecheng.manage_course.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    private TeachPlanMapper teachPlanMapper;
    @Autowired
    private TeachPlanRepository teachPlanRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private TeachPlanMediaRepository teachPlanMediaRepository;
    @Override
    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId) {
        return teachPlanMapper.selectList(courseId);
    }

    @Override
    //添加课程计划
    /*
    * 第一种情况：填写了父节点，直接插入即可(第三级别课程)
    * 第二种情况：没有填写父节点，表示他的默认父节点就是根结点(第二级别课程)
    * 第三种情况，没有填写父节点，并且查询不到根节点(刚新建的课程，在课程计划中就没有数据),先新建根结点，再插入数据(第二级别课程)
    * */
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || !StringUtils.isNotEmpty(teachplan.getCourseid()) || !StringUtils.isNotEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        //没有填写父节点的情况,获取父节点id
        if (!StringUtils.isNotEmpty(parentid)){
            List<Teachplan> byCourseidAndParentid = teachPlanRepository.findByCourseidAndParentid(courseid,"0");//查询根结点
            //没有根结点，刚新建的课程，没有课程计划，手动插入根结点并获取根结点id
            if (byCourseidAndParentid==null||byCourseidAndParentid.size()==0){
                Optional<CourseBase> byId = courseBaseRepository.findById(courseid);//查询课程
                if (!byId.isPresent()){
                    ExceptionCast.cast(CommonCode.INVALID_PAPAM);
                }
                CourseBase courseBase = byId.get();
                Teachplan teachplan1=new Teachplan();
                teachplan1.setCourseid(courseid);
                teachplan1.setGrade("1");
                teachplan1.setPname(courseBase.getName());
                teachplan1.setParentid("0");
                teachplan1.setStatus("0");
                teachPlanRepository.save(teachplan1);
                parentid=teachplan1.getId();
            }else {
                //有根节点，获取根结点id
                parentid = byCourseidAndParentid.get(0).getId();
            }
        }
        //保存新添加的课程计划
        Teachplan teachplanNew = new Teachplan();
        //将teachplan的属性拷贝到teachplanNew中
        BeanUtils.copyProperties(teachplan,teachplanNew);
        //要设置必要的属性
        teachplanNew.setParentid(parentid);
        //获取父节点的等级
        Optional<Teachplan> courseBase = teachPlanRepository.findById(parentid);
        if (!courseBase.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        Teachplan teachplan1 = courseBase.get();
        String parent_grade = teachplan1.getGrade();
        if(parent_grade.equals("1")){
            teachplanNew.setGrade("2");
        }else{
            teachplanNew.setGrade("3");
        }
        teachplanNew.setStatus("0");//未发布
        teachPlanRepository.save(teachplanNew);//保存新建的课程计划
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //分页查询我的课程
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest,String companyId) {
        if (courseListRequest==null){
            courseListRequest=new CourseListRequest();
        }
        if (page<=0){
            page=1;
        }
        if (size<=0){
            size=20;
        }
        courseListRequest.setCompanyId(companyId);
        PageHelper.startPage(page,size);
        List<CourseInfo> list=courseBaseMapper.findCourseList(courseListRequest);
        PageInfo<CourseInfo> pageInfo=new PageInfo<>(list);
        long total = pageInfo.getTotal();
        List<CourseInfo> list1 = pageInfo.getList();
        QueryResult<CourseInfo> queryResult=new QueryResult<>();
        queryResult.setList(list1);
        queryResult.setTotal(total);
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    @Override
    //添加课程基本信息
    public ResponseResult addCourseBase(CourseBase courseBase) {
        if (courseBase==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //查询课程基本信息
    public CourseBase getCourseBaseByid(String courseId) {
        if (!StringUtils.isNotEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if (byId.isPresent()){
            CourseBase courseBase = byId.get();
            return courseBase;
        }
        return null;
    }

    @Override
    //修改课程基本信息
    public ResponseResult updateCourseBase(String courseId, CourseBase courseBase) {
        if (courseBase==null){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        CourseBase course = this.getCourseBaseByid(courseId);
        if (course==null){
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_NAMEISNULL1);
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //根据id查询课程营销
    public CourseMarket getCourseMarketById(String courseId) {
        if (!StringUtils.isNotEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        Optional<CourseMarket> byId = courseMarketRepository.findById(courseId);
        if (byId.isPresent()){
            CourseMarket courseMarket = byId.get();
            return courseMarket;
        }
        return null;
    }

    @Override
    //修改或新建课程营销
    public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket) {
        CourseBase courseBaseByid = this.getCourseBaseByid(courseId);
        if (courseBaseByid==null||courseMarket==null){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        courseMarket.setId(courseId);
        courseMarketRepository.save(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //将图片信息上传到mysql
    public ResponseResult addCoursePic(String courseId, String pic) {
        if (!StringUtils.isNotEmpty(courseId)||!StringUtils.isNotEmpty(pic)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        Optional<CoursePic> byId = coursePicRepository.findById(courseId);
        if (byId.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_NAMEISNULL2);
        }
        CoursePic coursePic=new CoursePic();
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //查询图片
    public CoursePic findCoursePic(String courseId) {
        if (!StringUtils.isNotEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        Optional<CoursePic> byId = coursePicRepository.findById(courseId);
        if (byId.isPresent()){
            CoursePic coursePic = byId.get();
            return coursePic;
        }
        return null;
    }

    @Override
    //删除图片
    public ResponseResult deleteCoursePic(String courseId) {
        if (!StringUtils.isNotEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        long l = coursePicRepository.deleteByCourseid(courseId);
        if (l>0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }else {
            return new ResponseResult(CommonCode.FAIL);
        }
    }

    @Override
    //课程视图查询(包括：课程基本信息，课程计划，课程营销，课程图片)
    public CourseView courseview(String courseId) {
        if (!StringUtils.isNotEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        CourseView courseView=new CourseView();
        CourseBase courseBase = this.getCourseBaseByid(courseId);//课程基本信息
        if (courseBase!=null){
            courseView.setCourseBase(courseBase);
        }
        CoursePic coursePic = this.findCoursePic(courseId);//课程图片
        if (coursePic!=null){
            courseView.setCoursePic(coursePic);
        }
        CourseMarket courseMarket = this.getCourseMarketById(courseId);//课程营销
        if (courseMarket!=null){
            courseView.setCourseMarket(courseMarket);
        }
        TeachplanNode teachplan = this.findTeachplanList(courseId);//课程计划
        if (teachplan!=null){
            courseView.setTeachplanNode(teachplan);
        }
        return courseView;
    }

    @Override
    //保存课程计划和媒资视频的关联
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia==null||StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        //只能在三级计划中添加视频
        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> byId = teachPlanRepository.findById(teachplanId);
        if (!byId.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        Teachplan teachplan = byId.get();
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade)||!grade.equals("3")){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        teachPlanMediaRepository.save(teachplanMedia);
        return new ResponseResult(CommonCode.SUCCESS);
    }

}
