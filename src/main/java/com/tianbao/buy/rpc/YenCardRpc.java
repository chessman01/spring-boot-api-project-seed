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
public class YenCardRpc extends BaseRpc{
    @Resource
    private YenCardServiceImpl yenCardServiceImpl;

    @PostMapping("/list")
    public Result list() {
        List<YenCardVO> voList = yenCardServiceImpl.getAllByUser();

        return ResultGenerator.genSuccessResult(voList);
    }

    @PostMapping("/build")
    public Result build(@RequestParam(defaultValue = "0") long cardId) {
        YenCardVO cardVO = yenCardServiceImpl.build(cardId);

        return ResultGenerator.genSuccessResult(cardVO);
    }

    @PostMapping("/adjust")
    public Result adjust(Long cardId, Long rechargeId, Long couponId) {
        YenCardVO cardVO = yenCardServiceImpl.adjust(cardId, rechargeId, couponId);

        return ResultGenerator.genSuccessResult(cardVO);
    }

    @PostMapping("/create")
    public Result create(long cardId, long rechargeId, Long couponId) {
        String url = yenCardServiceImpl.create(cardId, rechargeId, couponId);

        return ResultGenerator.genSuccessResult(url);
    }

}
