package com.trey.order.server.utils;


import com.trey.order.server.vo.ResultVo;

public class ResultVoUtils {
    public static ResultVo success(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }
}