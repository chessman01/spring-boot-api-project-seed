package com.tianbao.buy.service.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.CourseManager;
import com.tianbao.buy.service.*;
import com.tianbao.buy.utils.DateUtils;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class CourseServiceImpl implements CourseService {
    private static Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

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
    public Map<Long, Course> getNormalCourse() {
        DateTime current = new DateTime().withMillisOfDay(0);
        List<Course> courses = getSubscribeCourse(current);

        return toMap(courses);
    }

    @Override
    public Map<Long, Course> getCourse(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Maps.newHashMap();
        }

        Condition condition = new Condition(Course.class);
        condition.createCriteria().andIn("id", ids);

        List<Course> courses = courseManager.findByCondition(condition);

        return toMap(courses);
    }

    @Override
    public ScheduleVO schedule(String date, int num4day) {
        checkArgument(num4day > NumberUtils.INTEGER_ZERO);

        ScheduleVO scheduleVO = new ScheduleVO();
        DateTime in = StringUtils.isBlank(date) ? new DateTime().withMillisOfDay(0)
                : new DateTime(date).withMillisOfDay(0);

        setCalendar(in, scheduleVO, num4day);
        setBanner(scheduleVO);
        scheduleVO.setAddress(getAddress());
        setCourse(in, num4day, scheduleVO);

        return scheduleVO;
    }

    @Override
    public Course getCourse(long id) {
        checkArgument(id > NumberUtils.LONG_ZERO);
        return courseManager.findById(id);
    }

    @Override
    public CourseVO getCourse(long id, boolean isDetail) {
        checkArgument(id > NumberUtils.LONG_ZERO);
        Course course = courseManager.findById(id);

        CourseVO courseVO = convert2CourseVO(course, false, false);

        if (courseVO == null) {
            logger.error(String.format("获取课程失败.id[%d]", id));
            throw new BizException("获取课程失败");
        }

        if(!isDetail) filter(courseVO, true);
        return courseVO;
    }

    private Address getAddress() {
        String[] tmp = tianmaAddress.split(",");
        Address address = new Address();

        if (tmp.length != 4) return null;

        address.setDetailAddress(tmp[0]);
        address.setName(tmp[1]);
        address.setLatitude(tmp[2]);
        address.setLongitude(tmp[3]);

        return address;
    }

    private void setCalendar(DateTime in, ScheduleVO scheduleVO, int num4day) {
        DateTime tmp, current = new DateTime().withMillisOfDay(0);
        List<ScheduleVO.Date> dates = Lists.newArrayList();
        if (in == null)  in = new DateTime().withMillisOfDay(0);

        for (int i = 0; i < num4day; i++) {
            ScheduleVO.Date date = new ScheduleVO.Date();

            tmp = DateUtils.plusDays(i, in);

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
            Button banner = new Button(null, null, new Button.Event(entry.getValue(), null), entry.getKey(), true);
            banners.add(banner);
        }

        scheduleVO.setBanner(banners);
    }

    /** 获取XX某个时间点的可订课表 **/
    private List<Course> getSubscribeCourse(DateTime current) {
        checkNotNull(current);
        Condition condition = new Condition(Course.class);

        condition.orderBy("startTime");
        condition.createCriteria().andCondition("status=", CourseVO.Status.NORMAL.getCode())
                .andLessThanOrEqualTo("startTime", current);

        return courseManager.findByCondition(condition);
    }

    /** 获取到指定天数的课程，并按日期分组 **/
    private void setCourse(DateTime in, int days, ScheduleVO scheduleVO) {
        DateTime current;

        if (in != null) {
            current = in.withMillisOfDay(0);
        } else {
            current = new DateTime().withMillisOfDay(0);
        }

        List<Course> courses = getSubscribeCourse(current);

        DateTime tmp;
        List<ScheduleVO.Course4Day> course4Days = Lists.newArrayList();

        for (int i = 0; i < days; i++) {
            tmp = DateUtils.plusDays(i, current);

            Predicate<Course> predicateUserStatus = PredicateWrapper.getPredicate4Course(tmp);
            Predicate<Course> unionUserPredicate = Predicates.and(predicateUserStatus);
            List<Course> course4Day = Lists.newArrayList(Iterators.filter(courses.iterator(), unionUserPredicate));

            List<CourseVO> courseVOs = convert2CourseVO(course4Day);

            filter(courseVOs, false);

            course4Days.add(new ScheduleVO.Course4Day(DateUtils.yearMonthDayFormat(tmp), courseVOs));
        }

        scheduleVO.setCourse4Day(course4Days);
    }

    private void filter(CourseVO item, boolean hasAddress) {
        if (!hasAddress) item.setAddress(null);
        item.setCare(null);
        item.setCrowd(null);
        item.setDescription(null);
        item.setFaq(null);
        item.setTrainingEffect(null);

        if (item.getCoach() != null) {
            item.getCoach().setDesc(null);
            item.getCoach().setNick(null);
        }
    }

    private void filter(List<CourseVO> courseVOs, boolean hasAddress) {
        courseVOs.forEach(item -> filter(item, hasAddress));
    }

    private Map<Long, Course> toMap (List<Course> courses) {
        Map<Long, Course> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(courses)) return map;

        for (Course course : courses) {
            map.put(course.getId(), course);
        }

        return map;
    }

    public CourseVO convert2CourseVO(Course course, boolean isFilter, boolean hasAddress) {
        if (course == null) return null;
        List<CourseVO> courseVOs = convert2CourseVO(Lists.newArrayList(course));

        if(isFilter) filter(courseVOs, hasAddress);

        CourseVO courseVO = courseVOs.get(NumberUtils.INTEGER_ZERO);

        if (courseVO.getButton().getTitle().equals("预约")) {
            courseVO.getButton().setTitle("立即预约");
        }

        if (courseVO.getButton().getTitle().equals("满员")) {
            courseVO.getButton().setTitle("已满员");
        }

        if (courseVO.getButton().getTitle().equals("结束")) {
            courseVO.getButton().setTitle("已结束");
        }

        return courseVO;
    }

    public List<CourseVO> convert2CourseVO(List<Course> courses) {
        List<CourseVO> courseVOs = Lists.newArrayList();
        if (CollectionUtils.isEmpty(courses)) return courseVOs;

        for (Course course : courses) {
            CourseVO courseVO = new CourseVO();

            BeanUtils.copyProperties(course, courseVO);

            courseVO.setAddress(getAddress());
            courseVO.setTitle(course.getTitle());
            courseVO.setTags(course.getTags().split("\\."));
            courseVO.setTime(DateUtils.yearMonthDayFormat(new DateTime(course.getStartTime())) + " " +
                    DateUtils.hourMinuteFormat(new DateTime(course.getStartTime())) + "-"
                    + DateUtils.hourMinuteFormat(new DateTime(course.getEndTime())));
            courseVO.setShortTime(DateUtils.hourMinuteFormat(new DateTime(course.getStartTime())) + "-"
                    + DateUtils.hourMinuteFormat(new DateTime(course.getEndTime())));

            Button button = new Button();

            button.setTitle("预约");
            if (course.getStock() < 10) {
                courseVO.setStockIcon(lowStockPic);
            }

            if (course.getStock() == 0 ) {
                courseVO.setStockIcon(sellOutPic);
                button.setDisable(false);
                button.setTitle("满员");
            }

            if (!course.getStatus().equals(CouponVO.Status.NORMAL.getCode())) {
                button.setDisable(false);
            }

            if (new DateTime(course.getStartTime()).isBeforeNow()) {
                button.setDisable(false);
                button.setTitle("结束");
            }

            courseVO.setButton(button);
            courseVO.setStock(course.getStock());

            // 处理原价
            courseVO.setPrice(MoneyUtils.unitFormat(2, course.getPrice() / 100f));

            // 处理瘾卡折扣价
            int minDiscountRate = 100;
            User user = userService.getUserByWxUnionId();
            List<YenCard> yenCards = yenCardService.getCardByUser(user.getId());

            for (YenCard yenCard : yenCards) {
                if (yenCard.getDiscountRate() < minDiscountRate) minDiscountRate = yenCard.getDiscountRate();
            }

            double yenPrice = course.getPrice() * (minDiscountRate / 100f);
            courseVO.setYenPrice(MoneyUtils.unitFormat(2, yenPrice / 100f));
            courseVO.setCoach(coachService.getCoach(course.getCoachId()));

            courseVOs.add(courseVO);
        }

        return courseVOs;
    }
}
