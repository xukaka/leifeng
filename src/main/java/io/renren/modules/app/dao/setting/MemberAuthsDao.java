package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.setting.MemberAuths;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户授权表
 * @author xukaijun
 */
@Mapper
public interface MemberAuthsDao extends BaseMapper<MemberAuths> {
     MemberAuths queryByTypeAndIdentifier(@Param("identityType") String type,@Param("identifier") String identifier);
     MemberAuths queryByTypeAndCredential(@Param("identityType") String type,@Param("credential") String credential);
}
