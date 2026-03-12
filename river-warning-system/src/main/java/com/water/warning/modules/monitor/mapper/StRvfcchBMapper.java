package com.water.warning.modules.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.water.warning.modules.monitor.entity.StRvfcchB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 河道站防洪指标表Mapper接口
 */
@Mapper
public interface StRvfcchBMapper extends BaseMapper<StRvfcchB> {

    /**
     * 根据测站编码查询防洪指标
     */
    @Select("SELECT * FROM ST_RVFCCH_B WHERE STCD = #{stcd}")
    StRvfcchB selectByStcd(@Param("stcd") String stcd);
}
