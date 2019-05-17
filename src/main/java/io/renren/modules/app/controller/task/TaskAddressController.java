package io.renren.modules.app.controller.task;

import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.form.TaskAddressForm;
import io.renren.modules.app.service.TaskAddressService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/address")
@Api(tags = "任务地址")
public class TaskAddressController {

    @Autowired
    private TaskAddressService taskAddressService;


    @Login
    @PostMapping("/create")
    @ApiOperation("创建任务地址")
    public R createAddress( @RequestBody TaskAddressForm form) {
        taskAddressService.createAddress(ReqUtils.curMemberId(), form);
        return R.ok();
    }


    @Login
    @GetMapping("/list")
    @ApiOperation("获取任务地址列表")
    public R getAddresses() {
        List<TaskAddressEntity> addresses = taskAddressService.getAddresses(ReqUtils.curMemberId());
        return R.ok().put("result", addresses);
    }


    @GetMapping("/detail/{id}")
    @ApiOperation("获取任务地址详细信息")
    public R getAddress(@PathVariable("id") Long id) {
        TaskAddressEntity address = taskAddressService.getAddress(id);
        return R.ok().put("result", address);
    }


    @PutMapping("/update")
    @ApiOperation("更新任务地址信息")
    public R updateAddress(@RequestBody TaskAddressForm form) {
        taskAddressService.updateAddress(form);
        return R.ok();
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除任务地址")
    public R deleteAddress(@PathVariable("id") Long id) {
        taskAddressService.deleteAddress(id);
        return R.ok();
    }

}
