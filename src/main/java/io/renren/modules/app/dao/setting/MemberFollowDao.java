package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberFollowEntity;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关注表
 */
@Mapper
public interface MemberFollowDao extends BaseMapper<MemberFollowEntity> {

    /**
     * 分页获取关注用户的列表
     * @param fromMemberId
     * @param page
     * @return
     */
    List<Member> getFollowMembers(@Param("fromMemberId") Long fromMemberId,@Param("page") PageWrapper page);

    /**
     * 分页获取粉丝的列表
     * @param fromMemberId
     * @param page
     * @return
     */
    List<Member> getFansMembers(@Param("toMemberId") Long fromMemberId,@Param("page") PageWrapper page);

    /**
     * 关注用户的总数
     * @param fromMemberId
     * @return
     */
    int followCount(@Param("fromMemberId") Long fromMemberId);

    /**
     * 粉丝总数
     * @return
     */
    int fansCount(@Param("toMemberId") Long toMemberId);


}
