package io.renren.modules.app.dao.setting;

import io.renren.modules.app.entity.setting.MemberFriend;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员的好友关联表
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 16:03:12
 */
@Mapper
public interface MemberFriendDao extends BaseMapper<MemberFriend> {
	
}
