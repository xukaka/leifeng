package io.renren.modules.app.controller.story;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.app.entity.story.SubscribeEntity;
import io.renren.modules.app.service.SubscribeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@RestController
@RequestMapping("app/subscribe")
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = subscribeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
			SubscribeEntity subscribe = subscribeService.selectById(id);

        return R.ok().put("subscribe", subscribe);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SubscribeEntity subscribe){
        subscribeService.insert(subscribe);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SubscribeEntity subscribe){
        subscribeService.updateById(subscribe);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        subscribeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
