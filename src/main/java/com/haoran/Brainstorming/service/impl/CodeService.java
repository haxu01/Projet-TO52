package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.CodeMapper;
import com.haoran.Brainstorming.model.Code;
import com.haoran.Brainstorming.service.ICodeService;
import com.haoran.Brainstorming.util.DateUtil;
import com.haoran.Brainstorming.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CodeService implements ICodeService {

    @Resource
    private CodeMapper codeMapper;

    // Génération récursive de code pour éviter la duplication de code
    private String generateToken() {
        String _code = StringUtil.randomNumber(6);
        Code code = this.selectByCode(_code);
        if (code != null) {
            return this.generateToken();
        }
        return _code;
    }

    @Override
    public Code selectByCode(String _code) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Code::getCode, _code);
        return codeMapper.selectOne(wrapper);
    }

    // Interroger le code inutilisé
    @Override
    public Code selectNotUsedCode(Integer userId, String email, String mobile) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Code> lambda = wrapper.lambda();
        if (email != null) {
            lambda.eq(Code::getEmail, email);
            lambda.eq(Code::getUserId, userId);
        } else if (mobile != null) {
            lambda.eq(Code::getMobile, mobile);
        }
        lambda.eq(Code::getUsed, false);
        lambda.gt(Code::getExpireTime, new Date());
        List<Code> codes = codeMapper.selectList(wrapper);
        if (codes.isEmpty()) return null;
        return codes.get(0);
    }


    // Créer un enregistrement de code de vérification
    @Override
    public Code createCode(Integer userId, String email, String mobile) {
        Code code = this.selectNotUsedCode(userId, email, mobile);
        if (code == null) {
            code = new Code();
            String _code = generateToken();
            code.setUserId(userId);
            code.setCode(_code);
            code.setEmail(email);
            code.setMobile(mobile);
            code.setInTime(new Date());
            code.setExpireTime(DateUtil.getMinuteAfter(new Date(), 30));
            codeMapper.insert(code);
        }
        return code;
    }


    @Override
    public Code validateCode(Integer userId, String email, String mobile, String _code) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Code> lambda = wrapper.lambda();
        if (email != null) {
            lambda.eq(Code::getEmail, email);
            lambda.eq(Code::getUserId, userId);
        } else if (mobile != null) {
            lambda.eq(Code::getMobile, mobile);
        }
        lambda.eq(Code::getCode, _code);
        lambda.eq(Code::getUsed, false);
        lambda.gt(Code::getExpireTime, new Date());
        return codeMapper.selectOne(wrapper);
    }



    @Override
    public void update(Code code) {
        codeMapper.updateById(code);
    }


    // supprimer les enregistrements de commentaires en fonction de l'identifiant de l'utilisateur
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Code::getUserId, userId);
        codeMapper.delete(wrapper);
    }
}
