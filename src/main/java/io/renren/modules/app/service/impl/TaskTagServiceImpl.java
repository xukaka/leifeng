package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.app.dao.task.TaskTagDao;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.TaskTagService;
import io.renren.modules.oss.entity.SysOssEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskTagServiceImpl extends ServiceImpl<TaskTagDao, TaskTagEntity> implements TaskTagService {
    private final static Logger logger = LoggerFactory.getLogger(TaskTagServiceImpl.class);


    @Override
    public List<TaskTagEntity> getTaskTags() {
        Wrapper<TaskTagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("deleted",false)
                .orderBy("usageCount",false);
        List<TaskTagEntity> tags = this.selectList(wrapper);
        if (CollectionUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }
        return tags;

    }

    @Override
    public void createTaskTag(String tagName) {
        if (exists(tagName)) {
            throw new RRException("标签已存在");
        }

        TaskTagEntity tag = new TaskTagEntity(DateUtils.now(),tagName);
        this.insert(tag);
    }

    @Override
    public void updateTaskTag(Long tagId, String tagName) {
        checkNameExistsOtherTags(tagId,tagName);
        TaskTagEntity tag = this.selectById(tagId);
        tag.setName(tagName);
        this.updateById(tag);

    }

    @Override
    public void deleteTaskTag(Long tagId) {
        TaskTagEntity tag = this.selectById(tagId);
        if (tag != null) {
            tag.setDeleted(true);
            this.updateById(tag);
        }
    }

    @Override
    public PageUtils<TaskTagEntity> getTasks( Map<String,Object> pageMap) {
        Wrapper<TaskTagEntity> wrapper = new EntityWrapper<>();
        wrapper.orderBy("usage_count",false);
        Page<TaskTagEntity> page = this.selectPage(
                new Query<TaskTagEntity>(pageMap).getPage(),wrapper
        );
        return new PageUtils<>(page);

      /*
        List<TaskCommentDto> comments = this.baseMapper.getComments(taskId, page);
        if (CollectionUtils.isEmpty(comments)) {
            return new PageUtils<>();
        }
        setCommentRepies(comments);
        int total = this.baseMapper.count(taskId);
        return new PageUtils<>(comments, total, page.getPageSize(), page.getCurrPage());
*/
    }

    /**
     * 检查标签名是否存在其它标签中
     */
    private void checkNameExistsOtherTags(Long tagId, String tagName) {
        Wrapper<TaskTagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("name",tagName).notIn("id",tagId);
        boolean exists = this.selectCount(wrapper) > 0;
        if (exists){
            throw new RRException("标签已存在");
        }

    }

    /**
     * 标签是否已存在
     */
    private boolean exists(String name){
        Wrapper<TaskTagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        return this.selectCount(wrapper) > 0;
    }
}