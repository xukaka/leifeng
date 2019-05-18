package io.renren.modules.app.dao.banner;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.BannerDto;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.banner.BannerEntity;
import io.renren.modules.app.entity.task.CommentEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 横幅
 */
@Mapper
public interface BannerDao extends BaseMapper<BannerEntity> {

    /**
     * 分页获取横幅列表
     */
    List<BannerDto> getBanners(@Param("type") BannerTypeEnum type, @Param("page") PageWrapper page);

    /**
     * 获取横幅总数
     */
    int count(@Param("type") BannerTypeEnum type);


    //获取横幅详情
    BannerDto getBanner(@Param("id") Long id);
}
