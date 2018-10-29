package com.trey.user.server.util;

import com.trey.user.server.enums.ResultEnum;
import com.trey.user.server.vo.ResultVo;

public class ResultVoUtils {
    /**
     * 成功返回
     * @param data
     * @return
     */
    public static ResultVo success(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }

    public static ResultVo success() {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }

    /**
     * 失败返回
     * @param resultEnum
     * @return
     */
    public static ResultVo error(ResultEnum resultEnum) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(resultEnum.getCode());
        resultVo.setMsg(resultEnum.getMessage());
        return resultVo;
    }
}