package com.tianbao.buy.service.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.CoachManager;
import com.tianbao.buy.manager.CourseManager;
import com.tianbao.buy.manager.TagManager;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.service.PredicateWrapper;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.YenCareService;
import com.tianbao.buy.utils.DateUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {
    @Value("${biz.schedule.top.banner}")
    private String topBanner;

    @Value("${biz.schedule.tianma.address}")
    private String tianmaAddress;

    @Value("${biz.schedule.stock.sell.out}")
    private String sellOutPic;

    @Value("${biz.schedule.stock.low}")
    private String lowStockPic;

    @Resource
    private TagManager tagManager;

    @Resource
    private CoachManager coachManager;

    @Resource
    private CourseManager courseManager;

    @Resource
    private YenCareService yenCareService;

    @Resource
    private UserService userService;

    @Override
    public ScheduleVO schedule(String date, int num) {
        ScheduleVO scheduleVO = new ScheduleVO();
        DateTime in = StringUtils.isBlank(date) ? new DateTime().withMillisOfDay(0) : new DateTime(date);

        setCalendar(in, scheduleVO);
        setBanner(scheduleVO);
        setAddress(scheduleVO);
        setCourse(num, scheduleVO);

        return scheduleVO;
    }

    @Override
    public CourseVO detail(long id) {
        return null;
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

            Button banner = new Button(null, null, new Button.Event(entry.getValue(), null), entry.getKey(), true);

            banners.add(banner);
        }

        scheduleVO.setBanner(banners);
    }

    /** 获取到课程相关的所有tag 改了方案，暂不用了，搞太复杂了**/
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

    private CourseVO getCourse(long id) {
        Course course = courseManager.findById(id);

        return convert2CourseVO(course);
    }

    /** 获取到指定天数的课程，并按日期分组 **/
    private void setCourse(int days, ScheduleVO scheduleVO) {
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

        scheduleVO.setCourse4Day(course4Days);
    }

    private CourseVO convert2CourseVO(Course course) {
        CourseVO courseVO = convert2CourseVO(Lists.newArrayList(course)).get(NumberUtils.INTEGER_ZERO);
        Map<Long, CoachVO> coachVOMap = getAllCoach();



        return courseVO;
    }

    private List<CourseVO> convert2CourseVO(List<Course> course4Day) {
        List<CourseVO> courseVOs = Lists.newArrayList();

        for (Course course : course4Day) {
            CourseVO courseVO = new CourseVO();

            courseVO.setTitle(course.getTitle());
            courseVO.setTags(course.getTags());
            courseVO.setTime(DateUtils.monthDayFormat(new DateTime(course.getStartTime())) + "-"
                    + DateUtils.monthDayFormat(new DateTime(course.getStartTime())));

            Button button = new Button();

            Button.Event event = new Button.Event("h5.m.taobao.com","click");
            button.setEvent(event);
            button.setTitle("预约");

            if (course.getStock() < 10) {
                courseVO.setStockIcon(lowStockPic);

            }

            if (course.getStock() == 0) {
                courseVO.setStockIcon(sellOutPic);
                button.setDisable(false);
            }

            courseVO.setButton(button);
            courseVO.setStock(course.getStock());

            // 处理原价
            NumberFormat numberFormat = NumberFormat.getNumberInstance();

            numberFormat.setGroupingUsed(false);
            numberFormat.setMaximumFractionDigits(2);

            courseVO.setPrice(numberFormat.format(course.getPrice() / 100f));

            // 处理瘾卡折扣价
            int minDiscountRate = 100;
            User user = userService.getUserByWxUnionId();
            List<YenCare> yenCares = yenCareService.getCareByUser(user.getId());

            for (YenCare yenCare : yenCares) {
                if (yenCare.getDiscountRate() < minDiscountRate) minDiscountRate = yenCare.getDiscountRate();
            }

            double yenPrice = (course.getPrice() / 100f) * (minDiscountRate / 100f);
            courseVO.setYenPrice(numberFormat.format(yenPrice));

            courseVOs.add(courseVO);
        }

        return courseVOs;
    }
}
