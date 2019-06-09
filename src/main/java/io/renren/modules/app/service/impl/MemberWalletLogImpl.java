package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.BeanUtil;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dao.pay.MemberWalletLogDao;
import io.renren.modules.app.dto.MemberWalletLogDto;
import io.renren.modules.app.dto.MoneyCheckDto;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.entity.task.WithdrawalOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.MemberWalletLogService;
import io.renren.modules.app.service.TaskOrderService;
import io.renren.modules.app.service.WithdrawalOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class MemberWalletLogImpl extends ServiceImpl<MemberWalletLogDao, MemberWalletLogEntity> implements MemberWalletLogService {


    @Autowired
    private WithdrawalOrderService withdrawalOrderService;

    @Autowired
    private TaskOrderService taskOrderService;


    public PageUtils<MemberWalletLogDto> getLogs(Long memberId, PageWrapper page) {
        List<MemberWalletLogEntity> logs = baseMapper.getLogs(memberId, page);
        if (CollectionUtils.isEmpty(logs)) {
            return new PageUtils<>();
        }
        List<MemberWalletLogDto> logDtos = cover2LogDtos(logs);

        int total = baseMapper.count(memberId);
        return new PageUtils<>(logDtos, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public MoneyCheckDto checkTotalMoney(Long memberId) {
        MoneyCheckDto checkData = baseMapper.checkTotalMoney(memberId);
        if (checkData != null) {
            if (checkData.getTotalBalance() == checkData.getTotalExpense() + checkData.getTotalIncome()
            && checkData.getTotalBalance().longValue() == checkData.getWalletMoney().longValue()) {
                checkData.setCheckStatus("success");
            }else {
                checkData.setCheckStatus("error");
            }
        }
        return checkData;
    }


    //类型转换
    private List<MemberWalletLogDto> cover2LogDtos(List<MemberWalletLogEntity> logs) {
        List<MemberWalletLogDto> logDtos = BeanUtil.copy(logs, MemberWalletLogDto.class);
        //设置交易状态
        Map<String, TaskOrderEntity> taskOrderMap = getTaskOrderOutTradeNoMap(logs);
        Map<String, WithdrawalOrderEntity> withdrawalOrderMap = getWithdrawalOrderOutTradeNoMap(logs);
        for (MemberWalletLogDto logDto : logDtos) {
            if (taskOrderMap.containsKey(logDto.getOutTradeNo())) {
                logDto.setTradeState(taskOrderMap.get(logDto.getOutTradeNo()).getTradeState());
            } else if (withdrawalOrderMap.containsKey(logDto.getOutTradeNo())) {
                logDto.setTradeState(withdrawalOrderMap.get(logDto.getOutTradeNo()).getTradeState());
            }
        }
        return logDtos;
    }

    //获取任务订单和订单id映射关系
    private Map<String, TaskOrderEntity> getTaskOrderOutTradeNoMap(List<MemberWalletLogEntity> logs) {
        Map<String, TaskOrderEntity> taskOrderMap = new HashMap<>();
        List<String> taskIncomeOutTradeNos = new ArrayList<>();
        for (MemberWalletLogEntity log : logs) {
            if (StringUtils.equals("taskIncome", log.getType())) {
                taskIncomeOutTradeNos.add(log.getOutTradeNo());
            }
        }
        if (!CollectionUtils.isEmpty(taskIncomeOutTradeNos)) {
            Wrapper<TaskOrderEntity> wrapper = new EntityWrapper<>();
            wrapper.in("out_trade_no", taskIncomeOutTradeNos);
            List<TaskOrderEntity> taskOrders = taskOrderService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(taskOrders)) {
                taskOrderMap = taskOrders.stream().collect(Collectors.toMap(TaskOrderEntity::getOutTradeNo, Function.identity(), (key1, key2) -> key2));
            }
        }
        return taskOrderMap;
    }

    // 获取提现订单和订单id映射关系
    private Map<String, WithdrawalOrderEntity> getWithdrawalOrderOutTradeNoMap(List<MemberWalletLogEntity> logs) {
        Map<String, WithdrawalOrderEntity> withdrawalOrderMap = new HashMap<>();
        List<String> withdrawalOutTradeNos = new ArrayList<>();
        for (MemberWalletLogEntity log : logs) {
            if (StringUtils.equals("withdrawal", log.getType())) {
                withdrawalOutTradeNos.add(log.getOutTradeNo());
            }
        }
        if (!CollectionUtils.isEmpty(withdrawalOutTradeNos)) {
            Wrapper<WithdrawalOrderEntity> wrapper = new EntityWrapper<>();
            wrapper.in("out_trade_no", withdrawalOutTradeNos);
            List<WithdrawalOrderEntity> withdrawalOrders = withdrawalOrderService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(withdrawalOrders)) {
                withdrawalOrderMap = withdrawalOrders.stream().collect(Collectors.toMap(WithdrawalOrderEntity::getOutTradeNo, Function.identity(), (key1, key2) -> key2));
            }
        }
        return withdrawalOrderMap;
    }


}
