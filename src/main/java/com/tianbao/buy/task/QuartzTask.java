package com.tianbao.buy.task;

import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.*;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.CourseVO;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/***
 * Quartz设置项目全局的定时任务
 * @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
 * @Scheduled(fixedRate = 5000)//每5秒执行一次
 * @Scheduled(cron = "0/2 * * * * ?") //每2秒执行一次
 * @Scheduled(cron = "0 0 0/1 * * ?") // 每一小时执行一次
 */
@Component
@Configurable
@EnableScheduling
public class QuartzTask {
    @Resource
    private OrderMainManager orderMainManager;

    @Resource
    private CouponUserManager couponUserManager;

    @Resource
    private FundDetailManager fundDetailManager;

    @Resource
    private CouponTemplateManager couponTemplateManager;

    @Resource
    private CourseManager courseManager;

    /** 订单老没得到支付成功的结果，一小时后直接关掉，并且释放礼券 **/
    @Scheduled(cron = "0 0 0/1 * * ?") // 每一小时执行一次
    @Transactional
    public void releaseOrder() throws Exception {
        Condition condition = new Condition(OrderMain.class);
        DateTime dt = new DateTime().minusHours(1);

        condition.createCriteria().andCondition("status=", OrderVO.Status.PENDING.getCode())
                .andCondition("modify_time<", dt.toDate());

        List<OrderMain> orders = orderMainManager.findByCondition(condition);

        for (OrderMain order : orders) {
            CouponUser couponUser = new CouponUser();
            couponUser.setStatus(CouponVO.Status.NORMAL.getCode());

            Condition couponUserCondition = new Condition(CouponUser.class);
            couponUserCondition.createCriteria().andCondition("status=", CouponVO.Status.PENDING.getCode())
                    .andCondition("id=", order.getCouponId());

            couponUserManager.update(couponUser, couponUserCondition);

            FundDetail fundDetail = new FundDetail();
            fundDetail.setStatus(FundDetailVO.Status.DEL.getCode());

            Condition fundDetailCondition = new Condition(FundDetail.class);
            fundDetailCondition.createCriteria().andCondition("status=", FundDetailVO.Status.PENDING.getCode())
                    .andCondition("order_id=", order.getOrderId());

            fundDetailManager.update(fundDetail, fundDetailCondition);

            OrderMain orderMain = new OrderMain();
            orderMain.setStatus(FundDetailVO.Status.DEL.getCode());

            Condition orderMainCondition = new Condition(OrderMain.class);
            orderMainCondition.createCriteria().andCondition("status=", FundDetailVO.Status.PENDING.getCode())
                    .andCondition("id=", order.getId());

            orderMainManager.update(orderMain, orderMainCondition);
        }
    }

    @Scheduled(cron = "0 1 0 ? * *")
    @Transactional
    public void expired() throws Exception {
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setStatus(CouponVO.Status.EXPIRED.getCode());

        Condition couponTemplateCondition = new Condition(CouponTemplate.class);
        couponTemplateCondition.createCriteria().andCondition("status=", CouponVO.Status.NORMAL.getCode())
                .andCondition("end_time<", new Date());

        couponTemplateManager.update(couponTemplate, couponTemplateCondition);


        CouponUser couponUser = new CouponUser();
        couponUser.setStatus(CouponVO.Status.EXPIRED.getCode());

        Condition couponUserCondition = new Condition(CouponUser.class);
        couponUserCondition.createCriteria().andCondition("status=", CouponVO.Status.NORMAL.getCode())
                .andCondition("end_time<", new Date());

        couponUserManager.update(couponUser, couponUserCondition);


        Course course = new Course();
        course.setStatus(CourseVO.Status.EXPIRED.getCode());

        Condition courseCondition = new Condition(Course.class);
        courseCondition.createCriteria().andCondition("status=", CourseVO.Status.NORMAL.getCode())
                .andCondition("end_time<", new Date());

        courseManager.update(course, courseCondition);
    }
}