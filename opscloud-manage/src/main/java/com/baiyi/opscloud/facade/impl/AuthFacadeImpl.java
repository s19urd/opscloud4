package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcAuthResource;
import com.baiyi.opscloud.domain.generator.OcAuthRole;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.vo.auth.OcResourceVO;
import com.baiyi.opscloud.domain.vo.auth.OcRoleVO;
import com.baiyi.opscloud.facade.AuthFacade;
import com.baiyi.opscloud.service.auth.OcAuthResourceService;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:21 下午
 * @Version 1.0
 */
@Service
public class AuthFacadeImpl implements AuthFacade {

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Resource
    private OcAuthResourceService ocAuthResourceService;

    @Override
    public DataTable<OcRoleVO.OcRole> queryRolePage(RoleParam.PageQuery pageQuery) {
        DataTable<OcAuthRole> table = ocAuthRoleService.queryOcAuthRoleByParam(pageQuery);

        List<OcRoleVO.OcRole> page = BeanCopierUtils.copyListProperties(table.getData(), OcRoleVO.OcRole.class);
        DataTable<OcRoleVO.OcRole> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public void addRole(OcRoleVO.OcRole ocRole) {
        OcAuthRole ocAuthRole = BeanCopierUtils.copyProperties(ocRole, OcAuthRole.class);
        ocAuthRoleService.addOcAuthRole(ocAuthRole);

    }

    @Override
    public void updateRole(OcRoleVO.OcRole ocRole) {
        OcAuthRole ocAuthRole = BeanCopierUtils.copyProperties(ocRole, OcAuthRole.class);
        ocAuthRoleService.updateOcAuthRole(ocAuthRole);
    }

    @Override
    public BusinessWrapper<Boolean> deleteRoleById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(id);
        if (ocAuthRole == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_ROLE_NOT_EXIST);
//        BinlogConfig binlogConfig = binlogConfigService.queryById(binlogConfigId);
//        if (binlogConfig == null) {
//            return new BusinessWrapper<>(ErrorEnum.BINLOG_CONFIG_NOT_EXIST);
//        }
//        int count = ruleDetailService.countByDbTable(binlogConfig.getTopic(), binlogConfig.getTable());
        int count = 0;
        if (count == 0) {
            ocAuthRoleService.deleteOcAuthRoleById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_ROLE_HAS_USED);
        }
    }

    @Override
    public DataTable<OcResourceVO.OcResource> queryResourcePage(ResourceParam.PageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryOcAuthResourceByParam(pageQuery);

        List<OcResourceVO.OcResource> page = BeanCopierUtils.copyListProperties(table.getData(), OcResourceVO.OcResource.class);
        DataTable<OcResourceVO.OcResource> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public void addResource(OcResourceVO.OcResource ocResource) {
        OcAuthResource ocAuthResource = BeanCopierUtils.copyProperties(ocResource, OcAuthResource.class);
        ocAuthResourceService.addOcAuthResource(ocAuthResource);

    }

    @Override
    public void updateResource(OcResourceVO.OcResource ocResource) {
        OcAuthResource ocAuthResource = BeanCopierUtils.copyProperties(ocResource, OcAuthResource.class);
        ocAuthResourceService.updateOcAuthResource(ocAuthResource);
    }

    @Override
    public BusinessWrapper<Boolean> deleteResourceById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceById(id);
        if (ocAuthResource == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_RESOURCE_NOT_EXIST);
//        BinlogConfig binlogConfig = binlogConfigService.queryById(binlogConfigId);
//        if (binlogConfig == null) {
//            return new BusinessWrapper<>(ErrorEnum.BINLOG_CONFIG_NOT_EXIST);
//        }
//        int count = ruleDetailService.countByDbTable(binlogConfig.getTopic(), binlogConfig.getTable());
        int count = 0;
        if (count == 0) {
            ocAuthResourceService.deleteOcAuthResourceById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_RESOURCE_HAS_USED);
        }
    }
}
