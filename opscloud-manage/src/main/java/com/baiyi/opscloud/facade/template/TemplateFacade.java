package com.baiyi.opscloud.facade.template;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;

/**
 * @Author baiyi
 * @Date 2021/12/6 10:58 AM
 * @Version 1.0
 */
public interface TemplateFacade {

    DataTable<TemplateVO.Template> queryTemplatePage(TemplateParam.TemplatePageQuery pageQuery);

    TemplateVO.Template addTemplate(TemplateParam.Template template);

    TemplateVO.Template updateTemplate(TemplateParam.Template template);

    void deleteTemplateById(int id);

    DataTable<BusinessTemplateVO.BusinessTemplate> queryBusinessTemplatePage(BusinessTemplateParam.BusinessTemplatePageQuery pageQuery);

    BusinessTemplateVO.BusinessTemplate addBusinessTemplate(BusinessTemplateParam.BusinessTemplate businessTemplate);

    BusinessTemplateVO.BusinessTemplate updateBusinessTemplate(BusinessTemplateParam.BusinessTemplate businessTemplate);

    BusinessTemplateVO.BusinessTemplate createAssetByBusinessTemplate(int id);

    void scanBusinessTemplateByInstanceUuid(String instanceUuid);

    void deleteBusinessTemplateById(int id);

}