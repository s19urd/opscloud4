package com.baiyi.opscloud.domain.vo.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/14 9:52 下午
 * @Version 1.0
 */
public class OcResourceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class OcResource {

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "资源路径")
        private String resourceName;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "鉴权")
        private Integer needAuth;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }


}
