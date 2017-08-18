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
    public Result build() {
        YenCardVO cardVO = yenCardServiceImpl.build();

        return ResultGenerator.genSuccessResult(cardVO);
    }
}
