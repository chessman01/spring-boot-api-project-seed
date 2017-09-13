package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.impl.YenCardServiceImpl;
import com.tianbao.buy.vo.OrderVO;
import com.tianbao.buy.vo.YenCardVO;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/yen/card")
public class YenCardRpc {
    @Resource
    private YenCardServiceImpl yenCardServiceImpl;

    @Resource
    private UserService userService;

    @PostMapping("/list")
    public Result list(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        List<YenCardVO> voList = yenCardServiceImpl.getCardByUser(user);

        return ResultGenerator.genSuccessResult(voList);
    }

    @PostMapping("/build")
    public Result build(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang, Long cardId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        YenCardVO cardVO = yenCardServiceImpl.build(cardId, user);

        return ResultGenerator.genSuccessResult(cardVO);
    }

    @PostMapping("/adjust")
    public Result adjust(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam Long cardId, @RequestParam Long templateId, Long couponId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        YenCardVO cardVO = yenCardServiceImpl.adjust(cardId, templateId, couponId, user);

        return ResultGenerator.genSuccessResult(cardVO);
    }

    @PostMapping("/create")
    public Result create(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam long cardId, @RequestParam long templateId, Long couponId) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        String orderId = yenCardServiceImpl.create(cardId, templateId, couponId, user);

        OrderVO.Order order = new OrderVO.Order();
        order.setOrderId(orderId);

        return ResultGenerator.genSuccessResult(order);
    }

}
