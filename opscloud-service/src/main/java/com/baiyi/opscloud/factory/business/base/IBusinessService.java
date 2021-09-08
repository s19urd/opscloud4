package com.baiyi.opscloud.factory.business.base;

import com.baiyi.opscloud.domain.vo.business.BaseBusiness;

/**
 * @Author baiyi
 * @Date 2021/9/8 10:03 上午
 * @Version 1.0
 */
public interface IBusinessService<T> extends BaseBusiness.IBusinessType {

    T getById(Integer id);
}
