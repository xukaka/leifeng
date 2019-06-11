package io.renren.modules.app.dao.im;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.ImCircleNoticeDto;
import io.renren.modules.app.dto.ImTaskNoticeDto;
import io.renren.modules.app.entity.im.ImCircleNotice;
import io.renren.modules.app.entity.im.ImTaskNotice;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IM圈通知
 */
@Mapper
public interface ImCircleDao extends BaseMapper<ImCircleNotice> {

    /**
     * 分页获取圈通知列表
     */
    List<ImCircleNoticeDto> getCircleNotices(@Param("toMemberId") Long toMemberId, @Param("page") PageWrapper page);

    /**
     * 任务状态通知总数
     */
    int getCircleNoticeCount(@Param("toMemberId") Long toMemberId);
}
