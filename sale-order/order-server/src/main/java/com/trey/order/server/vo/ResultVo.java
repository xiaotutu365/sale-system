package com.trey.order.server.vo;

import lombok.Data;

/**
 * http请求返回的最外层对象
 *
 * @param <T>
 */
@Data
public class ResultVo<T> {
    /**
     * 状态码，正常为0，错误为1
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 具体内容
     */
    private T data;
}