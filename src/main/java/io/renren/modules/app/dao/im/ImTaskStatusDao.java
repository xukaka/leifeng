package io.renren.modules.app.dao.im;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.ImTaskStatusNoticeDto;
import io.renren.modules.app.entity.im.ImTaskStatusNotice;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IM任务状态
 */
@Mapper
public interface ImTaskStatusDao extends BaseMapper<ImTaskStatusNotice> {

    /**
     * 分页获取任务状态通知列表
     */
    List<ImTaskStatusNoticeDto> getTaskStatusNotices(@Param("to") String to, @Param("page") PageWrapper page);

    /**
     * 任务状态通知总数
     * @return
     */
    int getTaskStatusNoticeCount(@Param("to")String to);
}
