package io.renren.modules.app.controller.search;

import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.HotSearchDto;
import io.renren.modules.app.entity.search.SearchHistoryEntity;
import io.renren.modules.app.service.SearchService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/app/search")
@Api(tags = "搜索")
public class SearchController {

    @Resource
    private SearchService searchService;


    @Login
    @GetMapping("/history/save")
    @ApiOperation("保存搜索历史")
    public R saveHistory(  String keyword) {
        searchService.saveHistory(ReqUtils.curMemberId(), keyword);
        return R.ok();
    }

    @Login
    @GetMapping("/history/list")
    @ApiOperation("获取搜索历史列表")
    public R getHistories() {
        List<SearchHistoryEntity> histories = searchService.getHistories(ReqUtils.curMemberId());
        return R.ok().put("result", histories);
    }

    @Login
    @DeleteMapping("/history/clear")
    @ApiOperation("清空搜索历史")
    public R clearHistories() {
        searchService.clearHistories(ReqUtils.curMemberId());
        return R.ok();
    }

    @GetMapping("/hot")
    @ApiOperation("获取热门搜索列表")
    public R getHotSearch() {
        List<HotSearchDto> hotSearches = searchService.getHotSearch();
        return R.ok().put("result", hotSearches);
    }


}
