package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.setting.MemberAuthsDao;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.service.MemberAuthsService;
import org.springframework.stereotype.Service;


@Service("MemberAuthsService")
public class MemberAuthsServiceImpl extends ServiceImpl<MemberAuthsDao, MemberAuths> implements MemberAuthsService {

    @Override
    public MemberAuths queryByTypeAndIdentifier(String identityType,String identifier) {
        return baseMapper.queryByTypeAndIdentifier(identityType,identifier);
    }

    @Override
    public MemberAuths queryByTypeAndCredential(String identityType,String credential) {
        return baseMapper.queryByTypeAndCredential(identityType,credential);
    }
}
