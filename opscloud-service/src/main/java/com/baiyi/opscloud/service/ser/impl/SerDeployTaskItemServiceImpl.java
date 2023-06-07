package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTaskItem;
import com.baiyi.opscloud.mapper.SerDeployTaskItemMapper;
import com.baiyi.opscloud.service.ser.SerDeployTaskItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 修远
 * @Date 2023/6/7 11:00 AM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class SerDeployTaskItemServiceImpl implements SerDeployTaskItemService {

    private final SerDeployTaskItemMapper serDeployTaskItemMapper;

    @Override
    public void add(SerDeployTaskItem serDeployTaskItem) {
        serDeployTaskItemMapper.insert(serDeployTaskItem);
    }

    @Override
    public void update(SerDeployTaskItem serDeployTaskItem) {
        serDeployTaskItemMapper.updateByPrimaryKey(serDeployTaskItem);
    }
}
