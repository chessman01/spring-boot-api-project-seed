package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCare;
import com.tianbao.buy.manager.UserManager;
import com.tianbao.buy.manager.YenCareManager;
import com.tianbao.buy.service.YenCareService;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.YenCareVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;

@Service
public class YenCareServiceImpl implements YenCareService{
    @Resource
    private YenCareManager yenCareManager;

    @Resource
    private UserManager userManager;

    @Override
    public List<YenCareVO> getAllByUser() {
        // todo 这里是要依据微信接口拿到用户uid，到userManager查用户，反退过来得到用户ID
        String wxUnionId = "12345";

        User user = userManager.findBy("wxUnionId", wxUnionId);

        Long userId = user.getId();

        Condition condition = new Condition(User.class);
        condition.orderBy("createTime");

        condition.createCriteria().andCondition("user_id=", userId).andCondition("status=", 1);

        List<YenCare> doList = yenCareManager.findByCondition(condition);

        return convert2VO(doList);
    }

    @Override
    public YenCareVO build(long cardId) {
        // 要校验用户是不是有这卡


        return null;
    }

    private List<YenCareVO> convert2VO(List<YenCare> doList) {
        List<YenCareVO> voList = Lists.newArrayList();

        for (YenCare yenCare : doList) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();

            numberFormat.setGroupingUsed(false);
            numberFormat.setMaximumFractionDigits(2);

            String gift = numberFormat.format(yenCare.getGiftAccount() / 100f);
            String cash = numberFormat.format(yenCare.getCashAccount() / 100f);
            String total = numberFormat.format((yenCare.getGiftAccount() + yenCare.getCashAccount()) / 100f);

            numberFormat.setMaximumFractionDigits(1);
            String discount = String.format("消费立打%s折", numberFormat.format(yenCare.getDiscountRate() / 10f));

            YenCareVO vo = new YenCareVO(yenCare.getId(), "http://gw.alicdn.com/tps/TB1LNMxPXXXXXbhaXXXXXXXXXXX-183-129.png",
                    gift, cash, total,
                    discount);

            voList.add(vo);
        }

        return voList;
    }
}
