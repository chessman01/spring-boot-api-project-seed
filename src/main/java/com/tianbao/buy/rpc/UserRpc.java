package com.tianbao.buy.rpc;

import com.aliyuncs.exceptions.ClientException;
import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.vo.InvitationVO;
import com.tianbao.buy.vo.UserVO;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserRpc {
    @Resource
    private UserService userService;

    /** 获取手机验证码，如果是确认朋友推荐，如果自已已购买过，则不能再由朋友推荐 **/
    @PostMapping("/pin")
    public Result getPin(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                         @RequestParam String phone, boolean isObtainRecommend) throws WxErrorException, ClientException {
        User user = userService.getUserByWxOpenId(openId, lang);
        String result = userService.getPin(phone, isObtainRecommend, user);

        if (StringUtils.isBlank(result)) return ResultGenerator.genSuccessResult();
        return ResultGenerator.genFailResult(result);
    }

    /** 验证手机 **/
    @PostMapping("/validate/phone")
    public Result validatePhone(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                                @RequestParam String code, @RequestParam String phone) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        String result = userService.validatePhone(code, phone, user);
        if (StringUtils.isBlank(result)) return ResultGenerator.genSuccessResult();
        return ResultGenerator.genFailResult(result);
    }

    /** 自已的推荐页，给朋友的 **/
    @PostMapping("/invitation")
    public Result invitation(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        InvitationVO invitation = userService.invitation(user);
        return ResultGenerator.genSuccessResult(invitation);
    }

    /** 朋友推荐来的，要记下谁推荐的自个，并且给朋友发个券 **/
    @PostMapping("/recommend")
    public Result recommend(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang,
                            @RequestParam long inviter, @RequestParam String code, @RequestParam String phone) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        String result = userService.recommend(inviter, code, phone, user);
        if (StringUtils.isBlank(result)) return ResultGenerator.genSuccessResult();
        return ResultGenerator.genFailResult(result);
    }

    @PostMapping("/self")
    public Result self(@RequestParam String openId, @RequestParam(defaultValue = "zh_CN") String lang) throws WxErrorException {
        User user = userService.getUserByWxOpenId(openId, lang);
        UserVO userVO = userService.self(user);
        return ResultGenerator.genSuccessResult(userVO);
    }
}

