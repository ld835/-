package com.water.warning.modules.warning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.water.warning.modules.warning.entity.StWarnInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 预警信息表Mapper接口
 */
@Mapper
public interface StWarnInfoMapper extends BaseMapper<StWarnInfo> {

    /**
     * 查询测站未处理的预警
     */
    @Select("SELECT * FROM ST_WARN_INFO WHERE STCD = #{stcd} AND PROC_STAT IN (0, 1) ORDER BY WARN_TM DESC LIMIT 1")
    StWarnInfo selectActiveWarning(@Param("stcd") String stcd);

    /**
     * 查询所有未处理的预警
     */
    @Select("SELECT * FROM ST_WARN_INFO WHERE PROC_STAT IN (0, 1) ORDER BY WARN_TM DESC")
    List<StWarnInfo> selectAllActiveWarnings();

    /**
     * 查询指定级别的未处理预警
     */
    @Select("SELECT * FROM ST_WARN_INFO WHERE WARN_LV = #{warnLv} AND PROC_STAT IN (0, 1) ORDER BY WARN_TM DESC")
    List<StWarnInfo> selectActiveWarningsByLevel(@Param("warnLv") Integer warnLv);

    /**
     * 更新预警处理状态
     */
    @Update("UPDATE ST_WARN_INFO SET PROC_STAT = #{procStat}, HANDLER = #{handler}, " +
            "HANDLE_TIME = NOW(), HANDLE_REMARK = #{handleRemark}, UPDATE_TIME = NOW() " +
            "WHERE WARN_ID = #{warnId}")
    int updateProcStat(@Param("warnId") Long warnId,
                       @Param("procStat") Integer procStat,
                       @Param("handler") String handler,
                       @Param("handleRemark") String handleRemark);

    /**
     * 解除预警
     */
    @Update("UPDATE ST_WARN_INFO SET PROC_STAT = 2, RESOLVE_TM = NOW(), UPDATE_TIME = NOW() WHERE WARN_ID = #{warnId}")
    int resolveWarning(@Param("warnId") Long warnId);
}
