package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.app.entity.setting.MemberAuths;

/**
 * 用户授权
 * @author xukaijun
 */
public interface MemberAuthsService extends IService<MemberAuths> {

    MemberAuths queryByTypeAndIdentifier(String identityType,String identifier);
    MemberAuths queryByTypeAndCredential(String identityType,String credential);
}
