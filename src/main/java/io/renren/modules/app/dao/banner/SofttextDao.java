package io.renren.modules.app.dao.banner;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.SofttextDto;
import io.renren.modules.app.entity.banner.SofttextEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 横幅软文
 */
@Mapper
public interface SofttextDao extends BaseMapper<SofttextEntity> {

    /**
     * 分页获取软文列表
     */
    List<SofttextDto> getSofttexts(@Param("page") PageWrapper page);

    /**
     * 获取横幅软文总数
     */
    int count();


    //获取软文详情
//    SofttextDto getSofttext(@Param("id") Long id);

    //浏览数+inc
    void incViewCount(@Param("id") Long id,@Param("inc") Integer inc);
}
