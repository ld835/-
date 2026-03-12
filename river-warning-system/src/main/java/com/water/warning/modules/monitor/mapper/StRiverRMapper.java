package com.water.warning.modules.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.water.warning.modules.monitor.entity.StRiverR;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 河道水情表Mapper接口
 */
@Mapper
public interface StRiverRMapper extends BaseMapper<StRiverR> {

    /**
     * 查询测站最新水位数据
     */
    @Select("SELECT * FROM ST_RIVER_R WHERE STCD = #{stcd} ORDER BY TM DESC LIMIT 1")
    StRiverR selectLatestByStcd(@Param("stcd") String stcd);

    /**
     * 查询所有测站最新水位数据
     */
    @Select("SELECT r1.* FROM ST_RIVER_R r1 INNER JOIN ( " +
            "SELECT STCD, MAX(TM) AS maxTm FROM ST_RIVER_R GROUP BY STCD " +
            ") r2 ON r1.STCD = r2.STCD AND r1.TM = r2.maxTm")
    List<StRiverR> selectAllLatest();

    /**
     * 查询指定时间范围内的水位数据
     */
    @Select("SELECT * FROM ST_RIVER_R WHERE STCD = #{stcd} AND TM BETWEEN #{startTime} AND #{endTime} ORDER BY TM DESC")
    List<StRiverR> selectByTimeRange(@Param("stcd") String stcd,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);
}
