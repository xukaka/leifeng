package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.app.dao.task.TagDao;
import io.renren.modules.app.entity.task.TagEntity;
import io.renren.modules.app.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl extends ServiceImpl<TagDao, TagEntity> implements TagService {
    private final static Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Override
    public List<TagEntity> getAllTags() {
        Wrapper<TagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("deleted", false)
                .orderBy("usageCount", false);
        List<TagEntity> tags = selectList(wrapper);
        if (CollectionUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }
        return tags;
    }


    @Override
    public void createTag(String tagName) {
        LOG.debug("create tag params:tagName={}",tagName);
        if (exists(tagName)) {
            throw new RRException("标签已存在");
        }
        TagEntity tag = new TagEntity(DateUtils.now(), tagName);
        this.insert(tag);
    }


    @Override
    public void updateTag(Long tagId, String tagName) {
        LOG.debug("update tag params:tagId={},tagName={}",tagId,tagName);
        checkNameExistsOtherTags(tagId, tagName);
        TagEntity tag = this.selectById(tagId);
        tag.setName(tagName);
        this.updateById(tag);

    }

    @Override
    public void deleteTag(Long tagId) {
        LOG.debug("delete tag params:tagId={}",tagId);
        TagEntity tag = this.selectById(tagId);
        if (tag != null) {
            tag.setDeleted(true);
            this.updateById(tag);
        }
    }

    @Override
    public PageUtils<TagEntity> getTags(Map<String, Object> pageMap) {
        LOG.debug("get tags params:{}",pageMap);
        Wrapper<TagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("deleted", false)
                .orderBy("usage_count", false);
        Page<TagEntity> page = this.selectPage(
                new Query<TagEntity>(pageMap).getPage(), wrapper
        );
        return new PageUtils<>(page);
    }

    /**
     * 检查标签名是否存在其它标签中
     */
    private void checkNameExistsOtherTags(Long tagId, String tagName) {
        Wrapper<TagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("name", tagName).notIn("id", tagId);
        boolean exists = this.selectCount(wrapper) > 0;
        if (exists) {
            throw new RRException("标签已存在");
        }
    }

    /**
     * 标签是否已存在
     */
    private boolean exists(String name) {
        Wrapper<TagEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("name", name);
        return this.selectCount(wrapper) > 0;
    }


    @Override
    public List<TagEntity> getTagsByTaskId(Long taskId) {
        LOG.debug("getTagsByTaskId params:taskId={}",taskId);
        List<TagEntity> tags = baseMapper.getTagsByTaskId(taskId);
        if (CollectionUtils.isEmpty(tags)){
            return new ArrayList<>();
        }
        return tags;
    }
}