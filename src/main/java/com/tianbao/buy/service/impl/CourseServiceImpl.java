package com.tianbao.buy.service.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tianbao.buy.domain.Coach;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.domain.Course;
import com.tianbao.buy.domain.Tag;
import com.tianbao.buy.manager.CoachManager;
import com.tianbao.buy.manager.CourseManager;
import com.tianbao.buy.manager.TagManager;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.service.PredicateWrapper;
import com.tianbao.buy.utils.DateUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {
    @Value("${biz.schedule.top.banner}")
    private String topBanner;

    @Value("${biz.schedule.tianma.address}")
    private String tianmaAddress;

    @Resource
    private TagManager tagManager;

    @Resource
    private CoachManager coachManager;

    @Resource
    private CourseManager courseManager;

    public ScheduleVO getSchedule(String date) {
        ScheduleVO scheduleVO = new ScheduleVO();
        DateTime in = StringUtils.isBlank(date) ? new DateTime().withMillisOfDay(0) : new DateTime(date);

        setCalendar(in, scheduleVO);
        setBanner(scheduleVO);
        setAddress(scheduleVO);

        return scheduleVO;
    }

    private void setAddress(ScheduleVO scheduleVO) {
        String[] tmp = tianmaAddress.split(",");
        ScheduleVO.Address address = new ScheduleVO.Address();

        if (tmp.length != 4) return;

        address.setDetailAddress(tmp[0]);
        address.setName(tmp[1]);
        address.setLatitude(tmp[2]);
        address.setLongitude(tmp[3]);

        scheduleVO.setAddress(address);
    }

    private void setCalendar(DateTime in, ScheduleVO scheduleVO) {
        DateTime current = new DateTime().withMillisOfDay(0);
        DateTime tmp;
        List<ScheduleVO.Date> dates = Lists.newArrayList();

        for (int i = 0; i < 7; i++) {
            ScheduleVO.Date date = new ScheduleVO.Date();

            tmp = DateUtils.plusDays(i, current);

            date.setSelected(DateUtils.isEqual(tmp, in));
            date.setDayOfWeek(DateUtils.getDayOfWeek(tmp));
            date.setDayOfWeekDetail(DateUtils.getDayOfWeek(tmp, current));
            date.setMonthDayFormat(DateUtils.monthDayFormat(tmp));
            date.setYearMonthDayFormat(DateUtils.yearMonthDayFormat(tmp));

            dates.add(date);
        }

        scheduleVO.setCalendar(dates);
    }

    private void setBanner(ScheduleVO scheduleVO) {
        List<Button> banners = Lists.newArrayList();
        Map<String,String> kvs = Splitter.onPattern("##").withKeyValueSeparator("??").split(topBanner);

        for (Map.Entry<String,String> entry : kvs.entrySet()) {
            System.out.println(String.format("%s=%s", entry.getKey(),entry.getValue()));

            Button banner = new Button(null, null, new Button.Event(entry.getValue(), null), entry.getKey());

            banners.add(banner);
        }

        scheduleVO.setBanner(banners);
    }

    /** 获取到课程相关的所有tag **/
    private List<TagVO> getAllTag4Course() {
        Condition condition = new Condition(Tag.class);

        condition.createCriteria().andCondition("status=", TagVO.Status.NORMAL.getCode())
                .andIn("type", Lists.newArrayList(TagVO.Type.NORMAL.getCode(), TagVO.Type.COURSE.getCode()));

        List<Tag> tags = tagManager.findByCondition(condition);
        List<TagVO> tagVOs = Lists.newArrayList();

        tags.stream().forEach(tag -> {
            TagVO tagVO = new TagVO();
            BeanUtils.copyProperties(tag, tagVO);
            tagVOs.add(tagVO);
        });

        return tagVOs;
    }

    /** 获取到所有教练 **/
    private Map<Long, CoachVO> getAllCoach() {
        Condition condition = new Condition(Coach.class);

        condition.createCriteria().andCondition("status=", CoachVO.Status.NORMAL.getCode());

        List<Coach> coaches = coachManager.findByCondition(condition);
        Map<Long, CoachVO> coachVOs = Maps.newConcurrentMap();

        coaches.stream().forEach(coach -> {
            CoachVO coachVO = new CoachVO();
            BeanUtils.copyProperties(coach, coachVO);
            coachVOs.put(coachVO.getId(), coachVO);
        });

        return coachVOs;
    }

    /** 获取到一周的课程，并按日期分组 **/
    private List<ScheduleVO.Course4Day> getCourseInWeek(int days) {
        DateTime current = new DateTime().withMillisOfDay(0);
        Condition condition = new Condition(Course.class);

        condition.orderBy("startTime");
        condition.createCriteria().andCondition("status=", CourseVO.Status.NORMAL.getCode())
                .andGreaterThanOrEqualTo("startTime", current).andLessThanOrEqualTo("endTime", current.plusDays(days));

        List<Course> courses = courseManager.findByCondition(condition);

        DateTime tmp;
        List<ScheduleVO.Course4Day> course4Days = Lists.newArrayList();

        for (int i = 0; i < days; i++) {
            tmp = DateUtils.plusDays(i, current);

            Predicate<Course> predicateUserStatus = PredicateWrapper.getPredicate4Course(tmp);
            Predicate<Course> unionUserPredicate = Predicates.and(predicateUserStatus);
            List<Course> course4Day = Lists.newArrayList(Iterators.filter(courses.iterator(), unionUserPredicate));

            List<CourseVO> courseVOs = convert2CourseVO(course4Day);

            course4Days.add(new ScheduleVO.Course4Day(DateUtils.yearMonthDayFormat(tmp), courseVOs));
        }

        return course4Days;
    }

    private List<CourseVO> convert2CourseVO(List<Course> course4Day) {
        List<CourseVO> courseVOs = Lists.newArrayList();

        for (Course course : course4Day) {
            CourseVO courseVO = new CourseVO();

            courseVO.setTitle(course.getTitle());
            courseVO.setTagName();
            courseVO.setTime();
            courseVO.setPrice();
            courseVO.setYenPrice();
            courseVO.setButton();
            courseVO.setHotIcon();
            courseVO.setStockIcon();
            courseVO.setStock(course.getStock());
        }

        return null;
    }
}
