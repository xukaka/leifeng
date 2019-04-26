package io.renren.modules.app.dao.im;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.HotSearchDto;
import io.renren.modules.app.entity.im.ImGroupNotice;
import io.renren.modules.app.entity.search.SearchHistoryEntity;
import io.renren.modules.app.entity.search.SearchLogEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.LongLongSeqHelper;

import java.util.List;

/**
 * IM
 */
@Mapper
public interface ImDao extends BaseMapper<ImGroupNotice> {
    /**
     * 分页获取群组通知列表
     */
    List<ImGroupNotice> getGroupNotices(@Param("memberId") Long memberId, @Param("page") PageWrapper page);

    /**
     * 群组通知总数
     * @param memberId
     * @return
     */
    int groupNoticeCount(Long memberId);
}
