package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.pay.MemberWalletDao;
import io.renren.modules.app.dao.pay.MemberWalletLogDao;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.service.MemberWalletLogService;
import io.renren.modules.app.service.MemberWalletService;
import org.springframework.stereotype.Service;


@Service
public class MemberWalletLogImpl extends ServiceImpl<MemberWalletLogDao, MemberWalletLogEntity> implements MemberWalletLogService {



}
