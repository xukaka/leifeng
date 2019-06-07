package io.renren.modules.app.controller.pay;

import io.renren.modules.app.service.MemberWalletLogService;
import io.renren.modules.app.service.MemberWalletRecordService;
import io.renren.modules.app.service.MemberWalletService;
import io.renren.modules.app.service.TaskOrderService;
import io.renren.modules.app.service.impl.WXPayService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/wallet")
@Api(tags = "用户钱包")
public class MemberWalletController {
    private final static Logger logger = LoggerFactory.getLogger(MemberWalletController.class);
    @Resource
    private WXPayService wxPayService;
    @Autowired
    private TaskOrderService taskOrderService;

    @Autowired
    private MemberWalletService memberWalletService;

    @Autowired
    private MemberWalletLogService memberWalletLogService;
    @Autowired
    private MemberWalletRecordService memberWalletRecordService;

    /*@Login
    @PostMapping("/withdraw")
    @ApiOperation("提现申请")
    @Transactional
    public R withdraw(Long amount, String password) {
        Long memberId = ReqUtils.curMemberId();
        Wrapper<MemberWalletEntity> wrapper = new EntityWrapper<MemberWalletEntity>()
                .eq("member_id", memberId)
                .eq("pay_password", password);
        MemberWalletEntity wallet = memberWalletService.selectOne(wrapper);
        if (wallet == null || wallet.getMoney() < amount) {
            return R.error("提现钱包余额不足");
        }

        //用户钱包金额减少
        memberWalletService.incMoney(memberId, -amount);
        //交易记录
        MemberWalletRecordEntity record = new MemberWalletRecordEntity();
        String outTradeNo = "TX" + OrderNoUtil.generateOrderNo(memberId);
        record.setMoney(amount);
        record.setPayType(0);
        record.setType(2);
        record.setOutTradeNo(outTradeNo);
        record.setCreateTime(DateUtils.now());
        record.setFromMemberId(0L);//商户账号
        record.setToMemberId(memberId);
        memberWalletRecordService.insert(record);

        //记录钱包金额变动日志
        MemberWalletLogEntity log = new MemberWalletLogEntity();
        log.setMemberId(memberId);
        log.setChangeMoney(-amount);
        log.setMoney(wallet.getMoney() - amount);
        log.setOutTradeNo(outTradeNo);
        log.setRemark("提现");
        log.setCreateTime(DateUtils.now());
        memberWalletLogService.insert(log);

        return R.ok();
    }*/



}
