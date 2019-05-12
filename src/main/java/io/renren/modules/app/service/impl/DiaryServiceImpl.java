package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.story.DiaryContentDao;
import io.renren.modules.app.dao.story.DiaryDao;
import io.renren.modules.app.dto.DiaryDto;
import io.renren.modules.app.entity.story.DiaryContentEntity;
import io.renren.modules.app.entity.story.DiaryEntity;
import io.renren.modules.app.form.DiaryContentForm;
import io.renren.modules.app.form.DiaryForm;
import io.renren.modules.app.service.DiaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryDao, DiaryEntity> implements DiaryService {
    private final static Logger logger = LoggerFactory.getLogger(DiaryServiceImpl.class);

    @Autowired
    private DiaryDao diaryDao;

    @Autowired
    private DiaryContentDao diaryContentDao;

    @Override
    @Transactional
    public void createDiary(Long memberId,DiaryForm form) {
        ValidatorUtils.validateEntity(form);
        DiaryEntity diary = new DiaryEntity();
        BeanUtils.copyProperties(form, diary);
        diary.setCreatorId(memberId);
        diary.setCreateTime(DateUtils.now());
        this.insert(diary);
        addDiaryContent(diary.getId(),form.getContents());

    }


    //添加日记内容
    private void addDiaryContent(Long diaryId,List<DiaryContentForm> contents){
        long createTime = DateUtils.now();
        for (DiaryContentForm form:contents){
            DiaryContentEntity content= new DiaryContentEntity();
            BeanUtils.copyProperties(form, content);
            content.setDiaryId(diaryId);
            content.setCreateTime(createTime);
            diaryContentDao.insert(content);
        }
    }


    @Override
    public DiaryDto getDiary(Long id) {
       return baseMapper.getDiary(id);
    }
}
