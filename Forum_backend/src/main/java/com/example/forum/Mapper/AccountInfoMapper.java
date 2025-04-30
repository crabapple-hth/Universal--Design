package com.example.forum.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.forum.Entity.Dto.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {

    @Select("SELECT sex, COUNT(*) as count FROM db_account_details GROUP BY sex")
    List<Map<String, Object>> countUsersByGender();
}
