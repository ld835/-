package com.water.warning.modules.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.water.warning.modules.base.entity.StStationUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 管理单位关联表Mapper接口
 */
@Mapper
public interface StStationUnitMapper extends BaseMapper<StStationUnit> {

    /**
     * 根据测站编码查询所属单位
     */
    @Select("SELECT * FROM ST_STATION_UNIT WHERE STCD = #{stcd} AND unit_type = 1")
    List<StStationUnit> selectOwnUnits(@Param("stcd") String stcd);

    /**
     * 根据测站编码查询所有单位（包括所属和上级）
     */
    @Select("SELECT * FROM ST_STATION_UNIT WHERE STCD = #{stcd}")
    List<StStationUnit> selectAllUnits(@Param("stcd") String stcd);
}
