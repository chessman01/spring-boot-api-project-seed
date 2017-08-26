package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.impl.YenCardServiceImpl;
import com.tianbao.buy.vo.YenCardVO;
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

    @PostMapping("/list")
    public Result list() {
        List<YenCardVO> voList = yenCardServiceImpl.getCardByUser();

        return ResultGenerator.genSuccessResult(voList);
    }

    @PostMapping("/build")
    public Result build(Long cardId) {
        YenCardVO cardVO = yenCardServiceImpl.build(cardId);

        return ResultGenerator.genSuccessResult(cardVO);
    }

    @PostMapping("/adjust")
    public Result adjust(@RequestParam Long cardId, @RequestParam Long templateId,
                         Long couponId) {
        YenCardVO cardVO = yenCardServiceImpl.adjust(cardId, templateId, couponId);

        return ResultGenerator.genSuccessResult(cardVO);
    }

    @PostMapping("/create")
    public Result create(@RequestParam long cardId, @RequestParam long templateId,
                         Long couponId) {
        String orderId = yenCardServiceImpl.create(cardId, templateId, couponId);

        return ResultGenerator.genSuccessResult(orderId);
    }

}
