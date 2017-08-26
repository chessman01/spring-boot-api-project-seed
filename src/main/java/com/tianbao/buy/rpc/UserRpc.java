package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.vo.InvitationVO;
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
    public Result getPin(@RequestParam String phone, @RequestParam boolean isObtainRecommend) {
        boolean result = userService.getPin(phone, isObtainRecommend);
        return ResultGenerator.genSuccessResult(result, "验证码发放成功.");
    }

    /** 验证手机 **/
    @PostMapping("/validate/phone")
    public Result validatePhone(@RequestParam String code, @RequestParam String phone) {
        boolean result = userService.validatePhone(code, phone);
        return ResultGenerator.genSuccessResult(result, "验证成功.");
    }

    /** 自已的推荐页，给朋友的 **/
    @PostMapping("/invitation")
    public Result invitation() {
        InvitationVO invitation = userService.invitation();
        return ResultGenerator.genSuccessResult(invitation);
    }

    /** 朋友推荐来的，要记下谁推荐的自个，并且给朋友发个券 **/
    @PostMapping("/recommend")
    public Result recommend(@RequestParam long inviter, @RequestParam String code,
                             @RequestParam String phone) {
        userService.recommend(inviter, code, phone);
        return ResultGenerator.genSuccessResult();
    }
}

