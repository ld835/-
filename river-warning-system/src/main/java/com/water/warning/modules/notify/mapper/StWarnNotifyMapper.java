package com.water.warning.modules.notify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.water.warning.modules.notify.entity.StWarnNotify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 预警通知记录表Mapper接口
 */
@Mapper
public interface StWarnNotifyMapper extends BaseMapper<StWarnNotify> {

    /**
     * 查询预警的通知记录
     */
    @Select("SELECT * FROM ST_WARN_NOTIFY WHERE WARN_ID = #{warnId} ORDER BY CREATE_TIME DESC")
    List<StWarnNotify> selectByWarnId(@Param("warnId") Long warnId);

    /**
     * 查询待发送的通知
     */
    @Select("SELECT * FROM ST_WARN_NOTIFY WHERE NOTIFY_STAT = 0 ORDER BY CREATE_TIME ASC")
    List<StWarnNotify> selectPendingNotifies();

    /**
     * 更新通知状态
     */
    @Update("UPDATE ST_WARN_NOTIFY SET NOTIFY_STAT = #{notifyStat}, SEND_TM = #{sendTm}, " +
            "FAIL_REASON = #{failReason}, UPDATE_TIME = NOW() WHERE NOTIFY_ID = #{notifyId}")
    int updateNotifyStat(@Param("notifyId") Long notifyId,
                         @Param("notifyStat") Integer notifyStat,
                         @Param("sendTm") java.time.LocalDateTime sendTm,
                         @Param("failReason") String failReason);
}
