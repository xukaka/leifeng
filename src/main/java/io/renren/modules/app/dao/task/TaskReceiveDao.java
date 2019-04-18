package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 任务领取
 */
@Mapper
public interface TaskReceiveDao extends BaseMapper<TaskReceiveEntity> {

    /**
     * 获取任务领取人
     */
//    MemberDto getReceiver(@Param("receiveId") Long receiveId);

}
