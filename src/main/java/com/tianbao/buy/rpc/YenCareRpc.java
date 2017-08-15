package com.tianbao.buy.rpc;

import com.tianbao.buy.core.Result;
import com.tianbao.buy.core.ResultGenerator;
import com.tianbao.buy.service.impl.YenCareServiceImpl;
import com.tianbao.buy.vo.YenCareVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2017/08/15.
*/
@RestController
@RequestMapping("/yen/care")
public class YenCareRpc {
    @Resource
    private YenCareServiceImpl yenCareServiceImpl;

    @PostMapping("/list")
    public Result list() {
        List<YenCareVO> voList = yenCareServiceImpl.getAllByUser();

        return ResultGenerator.genSuccessResult(voList);
    }

    @PostMapping("/build")
    public Result build(@RequestParam Long cardId) {
        YenCareVO careVO = yenCareServiceImpl.build(cardId);

        return ResultGenerator.genSuccessResult(careVO);
    }
}
