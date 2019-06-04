package io.renren.modules.app.dao.im;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.ImTaskNoticeDto;
import io.renren.modules.app.entity.im.ImTaskNotice;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IM任务状态
 */
@Mapper
public interface ImTaskDao extends BaseMapper<ImTaskNotice> {

    /**
     * 分页获取任务状态通知列表
     */
    List<ImTaskNoticeDto> getTaskNotices(@Param("memberId") Long memberId, @Param("page") PageWrapper page);

    /**
     * 任务状态通知总数
     * @return
     */
    int getTaskNoticeCount(@Param("memberId")Long memberId);
}
