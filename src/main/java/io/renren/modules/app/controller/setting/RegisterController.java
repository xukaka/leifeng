package io.renren.modules.app.controller.setting;


import com.alibaba.fastjson.JSON;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dto.WXSession;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.member.MemberAuths;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.form.LoginForm;
import io.renren.modules.app.form.RegisterForm;
import io.renren.modules.app.form.WxUserInfoForm;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.service.impl.WXRequest;
import io.renren.modules.app.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-26 17:27
 */
@RestController
@RequestMapping("/app")
@Api(tags = "登录接口")
public class RegisterController {
    private final static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MemberAuthsService memberAuthsService;

    @Autowired
    private WXRequest wxRequest;

    /* @PostMapping("register")
    @ApiOperation("注册")
   public R wxRegister(@RequestBody RegisterForm form) {
        logger.info("[AppRegisterController.wxRegister] request param:{}", JSON.toJSONString(form));
        //表单校验
        ValidatorUtils.validateEntity(form);
        Member member = new Member();
        BeanUtils.copyProperties(form, member);
        member.setCreateTime(System.currentTimeMillis());

        MemberAuths auths = new MemberAuths();
        auths.setCredential(DigestUtils.sha256Hex(form.getCredential()));
        auths.setIdentityType(form.getIdentityType());
        auths.setIdentifier(form.getIdentifier());

        memberService.registerMemberWithAuth(member, auths);

        return R.ok();
    }*/

    /**
     * 微信登录
     */
    @PostMapping("wxLogin")
    @ApiOperation("微信登录")
    @ApiImplicitParam(name = "code", value = "微信login方法返回的code", paramType = "query")
    public R wxLogin(String code) {
        //表单校验
        logger.info("RegisterController.wxLogin 参数code：{}" , code);
        if (StringUtils.isEmpty(code)) {
            throw new RRException("code is null");
        }

        WXSession wxSession = wxRequest.loginWXWithCode(code);

        if (!ObjectUtils.isEmpty(wxSession)) {
            logger.info("wxsession={}",JsonUtil.Java2Json(wxSession));


            MemberAuths auths = memberAuthsService.queryByTypeAndCredential(Constant.WX_TYPE, DigestUtils.sha256Hex(wxSession.getOpenid()));
            //通过openid查询不到则注册新用户
            if (ObjectUtils.isEmpty(auths)) {
                Member member = new Member();
                member.setCreateTime(DateUtils.now());

                //创建用户钱包
                MemberWalletEntity wallet = new MemberWalletEntity();
                wallet.setOpenId(wxSession.getOpenid());

                auths = new MemberAuths();
                auths.setCredential(DigestUtils.sha256Hex(wxSession.getOpenid()));
                auths.setIdentityType(Constant.WX_TYPE);
                auths.setIdentifier(Constant.WX_IDENTIFIER);
                memberService.registerMemberWithAuth(member,wallet, auths);
            }

            String token = jwtUtils.generateToken(auths.getMemberId());
            Member member = memberService.selectById(auths.getMemberId());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("memberId", auths.getMemberId());
            map.put("openId",wxSession.getOpenid());
            map.put("avatar",member.getAvatar());
            map.put("nickName",member.getNickName());
            map.put("sex",member.getSex());
            return R.ok(map);

        } else {
            return R.error(HttpStatus.SC_BAD_REQUEST, "请求失败");
        }

    }

    /**
     * 手机密码登录
     */
    @PostMapping("phone/login")
    @ApiOperation("手机密码登录")
    public R login(@RequestBody LoginForm form) {
        //表单校验
        ValidatorUtils.validateEntity(form);

        //用户登录
        form.setIdentityType("phone");
        MemberAuths auths = memberAuthsService.queryByTypeAndIdentifier(form.getIdentityType(), form.getPhone());
        if (ObjectUtils.isEmpty(auths)) {
            throw new RRException("手机号不存在");
        }
        if (!auths.getCredential().equals(DigestUtils.sha256Hex(form.getCredential()))) {
            String s = DigestUtils.sha256Hex(form.getCredential());
            throw new RRException("登录密码(凭证)错误");
        }
        //生成token
        String token = jwtUtils.generateToken(auths.getMemberId());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("memberId", auths.getMemberId());

        return R.ok(map);
    }

    @PostMapping("/phone/send")
    @ApiOperation("通过手机号发送短信验证码")
    public R phoneSend(String phone) {
        try {
            memberService.sendPhoneCode(phone);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/phone/validate")
    @ApiOperation("验证手机号和验证码是否一致")
    public R phoneValidate(String phone, String code) {
        boolean success = memberService.validatePhoneCode(phone, code);
        if (success) {
            return R.ok();
        }
        return R.error(HttpStatus.SC_FORBIDDEN, "验证码错误");
    }

    @PostMapping("/phone/bindPhone")
    @ApiOperation("用户绑定手机号")
    public R bindPhone(String phone, String memberId) {
        Member member = memberService.selectById(memberId);
        if (member != null) {
            WxUserInfoForm form = redisUtils.get(RedisKeys.WX_PHONE + phone, WxUserInfoForm.class);
            if (form != null) {
                if (StringUtils.isEmpty(member.getNickName())) {
                    member.setNickName(form.getNickName());
                }
                if (StringUtils.isEmpty(member.getAvatar())) {
                    member.setAvatar(form.getAvatarUrl());
                }
                if (member.getSex() == null) {
                    member.setSex(form.getGender());
                }
            }
            member.setMobile(phone);
            memberService.updateById(member);
            return R.ok();
        }
        return R.error(HttpStatus.SC_FORBIDDEN, "没有查询到用户");
    }

}
