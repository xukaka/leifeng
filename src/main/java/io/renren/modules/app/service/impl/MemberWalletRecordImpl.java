package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.pay.MemberWalletDao;
import io.renren.modules.app.dao.pay.MemberWalletRecordDao;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletRecordEntity;
import io.renren.modules.app.service.MemberWalletRecordService;
import io.renren.modules.app.service.MemberWalletService;
import org.springframework.stereotype.Service;


@Service
public class MemberWalletRecordImpl extends ServiceImpl<MemberWalletRecordDao, MemberWalletRecordEntity> implements MemberWalletRecordService {

}
