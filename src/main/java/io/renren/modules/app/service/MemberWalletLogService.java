package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.MemberWalletLogDto;
import io.renren.modules.app.dto.MoneyCheckDto;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户钱包日志
 */
public interface MemberWalletLogService extends IService<MemberWalletLogEntity> {
    /**
     * 分页获取交易日志列表
     */
    PageUtils<MemberWalletLogDto> getLogs(Long memberId, PageWrapper page);


    //校验总金额
    MoneyCheckDto checkTotalMoney(Long memberId);


}

