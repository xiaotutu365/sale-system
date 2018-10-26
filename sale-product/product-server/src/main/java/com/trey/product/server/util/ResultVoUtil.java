package com.trey.product.server.util;

import com.trey.product.server.vo.ResultVo;

public class ResultVoUtil {
    public static ResultVo success(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }
}