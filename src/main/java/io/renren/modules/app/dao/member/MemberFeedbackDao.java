package io.renren.modules.app.dao.member;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.member.MemberFeedback;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 16:03:12
 */
@Mapper
public interface MemberFeedbackDao extends BaseMapper<MemberFeedback> {

}
