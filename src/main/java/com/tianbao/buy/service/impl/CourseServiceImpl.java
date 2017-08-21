package com.tianbao.buy.service.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.CourseManager;
import com.tianbao.buy.service.*;
import com.tianbao.buy.utils.DateUtils;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private CourseManager courseManager;

    @Resource
    private CoachService coachService;

    @Resource
    private YenCardService yenCardService;

    @Resource
    private UserService userService;

    @Override
    public Map<Long, CourseVO> getNormalCourse() {
        DateTime current = new DateTime().withMillisOfDay(0);
        List<Course> courses = getSubscribeCourse(current);

        return toMap(courses);
    }

    @Override
    public Map<Long, CourseVO> getCourse(Set<Long> ids) {
        Condition condition = new Condition(Course.class);
        condition.createCriteria().andIn("id", ids);

        List<Course> courses = courseManager.findByCondition(condition);

        return toMap(courses);
    }

    @Override
    public ScheduleVO schedule(String date, int num) {
        ScheduleVO scheduleVO = new ScheduleVO();
        DateTime in = StringUtils.isBlank(date) ? new DateTime().withMillisOfDay(0)
                : new DateTime(date).withMillisOfDay(0);

        setCalendar(in, scheduleVO);
        setBanner(scheduleVO);
        scheduleVO.setAddress(getAddress(false));
        setCourse(num, scheduleVO);

        return scheduleVO;
    }

    @Override
    public CourseVO detail(long id) {
        Course course = courseManager.findById(id);

        return convert2CourseVO(course, true);
    }

    private Address getAddress(boolean isShort) {
        String[] tmp = tianmaAddress.split(",");
        Address address = new Address();

        if (tmp.length != 4) return null;

        if (!isShort) {
            address.setDetailAddress(tmp[0]);
            address.setLatitude(tmp[2]);
            address.setLongitude(tmp[3]);
        }

        address.setName(tmp[1]);

        return address;
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

    private List<Course> getSubscribeCourse(DateTime current) {
        Condition condition = new Condition(Course.class);

        condition.orderBy("startTime");
        condition.createCriteria().andCondition("status=", CourseVO.Status.NORMAL.getCode())
                .andCondition("start_time<", current);

        return courseManager.findByCondition(condition);
    }

    /** 获取到指定天数的课程，并按日期分组 **/
    private void setCourse(int days, ScheduleVO scheduleVO) {
        DateTime current = new DateTime().withMillisOfDay(0);
        List<Course> courses = getSubscribeCourse(current);

        DateTime tmp;
        List<ScheduleVO.Course4Day> course4Days = Lists.newArrayList();

        for (int i = 0; i < days; i++) {
            tmp = DateUtils.plusDays(i, current);

            Predicate<Course> predicateUserStatus = PredicateWrapper.getPredicate4Course(tmp);
            Predicate<Course> unionUserPredicate = Predicates.and(predicateUserStatus);
            List<Course> course4Day = Lists.newArrayList(Iterators.filter(courses.iterator(), unionUserPredicate));

            List<CourseVO> courseVOs = convert2CourseVO(course4Day, false, false);

            course4Days.add(new ScheduleVO.Course4Day(DateUtils.yearMonthDayFormat(tmp), courseVOs));
        }

        scheduleVO.setCourse4Day(course4Days);
    }

    private Map<Long, CourseVO> toMap (List<Course> courses) {
        Map<Long, CourseVO> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(courses)) return map;

        List<CourseVO> courseVOs = convert2CourseVO(courses, false, true);

        for (CourseVO courseVO : courseVOs) {
            map.put(courseVO.getId(), courseVO);
        }

        return map;
    }

    private CourseVO convert2CourseVO(Course course, boolean needDesc) {
        CourseVO courseVO = convert2CourseVO(Lists.newArrayList(course), needDesc, true).get(NumberUtils.INTEGER_ZERO);

        BeanUtils.copyProperties(course, courseVO);
        courseVO.setAddress(getAddress(false));

        return courseVO;
    }

    private List<CourseVO> convert2CourseVO(List<Course> courses, boolean needDesc, boolean fullTime) {
        List<CourseVO> courseVOs = Lists.newArrayList();

        for (Course course : courses) {
            CourseVO courseVO = new CourseVO();

            courseVO.setAddress(getAddress(true));

            courseVO.setTitle(course.getTitle());
            courseVO.setTags(course.getTags().split("\\."));

            if (fullTime) {
                courseVO.setTime(DateUtils.yearMonthDayFormat(new DateTime(course.getStartTime())) + " " + courseVO.getTime());
            } else {
                courseVO.setTime(DateUtils.hourMinuteFormat(new DateTime(course.getStartTime())) + "-"
                        + DateUtils.hourMinuteFormat(new DateTime(course.getEndTime())));
            }

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
            courseVO.setPrice(MoneyUtils.format(2, course.getPrice() / 100f));

            // 处理瘾卡折扣价
            int minDiscountRate = 100;
            User user = userService.getUserByWxUnionId();
            List<YenCard> yenCards = yenCardService.getCardByUser(user.getId());

            for (YenCard yenCard : yenCards) {
                if (yenCard.getDiscountRate() < minDiscountRate) minDiscountRate = yenCard.getDiscountRate();
            }

            double yenPrice = (course.getPrice() / 100f) * (minDiscountRate / 100f);
            courseVO.setYenPrice(MoneyUtils.format(2, yenPrice / 100f));
            courseVO.setCoach(coachService.getCoach(course.getCoachId(), needDesc));

            courseVOs.add(courseVO);
        }

        return courseVOs;
    }
}
