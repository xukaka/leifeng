package io.renren.modules.app.dao.im;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.ImDynamicNoticeDto;
import io.renren.modules.app.entity.im.ImDynamicNotice;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IM
 */
@Mapper
public interface ImDao extends BaseMapper<ImDynamicNotice> {
    /**
     * 分页获取最新动态通知列表
     */
    List<ImDynamicNoticeDto> getDynamicNotices(@Param("memberId") Long memberId, @Param("page") PageWrapper page);

    /**
     * 最新动态通知总数
     * @param memberId
     * @return
     */
    int getDynamicNoticeCount(Long memberId);

}
