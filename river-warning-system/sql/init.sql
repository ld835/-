-- ==========================================
-- 河道水位预警系统数据库初始化脚本
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS river_warning DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE river_warning;

-- ==========================================
-- 1. 测站基本属性表 (ST_STBPRP_B)
-- ==========================================
DROP TABLE IF EXISTS ST_STBPRP_B;
CREATE TABLE ST_STBPRP_B (
    STCD VARCHAR(8) PRIMARY KEY COMMENT '测站编码',
    STNM VARCHAR(50) COMMENT '测站名称',
    ADDVCD VARCHAR(6) COMMENT '行政区划',
    BASIN VARCHAR(50) COMMENT '水系',
    RIVER VARCHAR(50) COMMENT '河流',
    STLC VARCHAR(100) COMMENT '所在位置',
    STTP VARCHAR(2) COMMENT '监测类型',
    DSCD VARCHAR(2) COMMENT '采集方式',
    ADMAUTH VARCHAR(100) COMMENT '管理单位',
    LGTD DECIMAL(10,6) COMMENT '经度',
    LTTD DECIMAL(10,6) COMMENT '纬度',
    STAK VARCHAR(100) COMMENT '站址',
    DT DATETIME COMMENT '启用时间',
    COMMENTS VARCHAR(500) COMMENT '备注',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测站基本属性表';

-- ==========================================
-- 2. 河道水情表 (ST_RIVER_R)
-- ==========================================
DROP TABLE IF EXISTS ST_RIVER_R;
CREATE TABLE ST_RIVER_R (
    STCD VARCHAR(8) NOT NULL COMMENT '测站编码',
    TM DATETIME NOT NULL COMMENT '时间',
    Z DECIMAL(7,3) COMMENT '水位(m)',
    Q DECIMAL(9,3) COMMENT '流量(m³/s)',
    XSA DECIMAL(9,3) COMMENT '断面过水面积(m²)',
    XSAVV DECIMAL(5,3) COMMENT '断面平均流速(m/s)',
    XSMXV DECIMAL(5,3) COMMENT '断面最大流速(m/s)',
    FLWCHRSCD CHAR(1) COMMENT '水情特征码',
    WPTN CHAR(1) COMMENT '水势: 4-涨 5-落 6-平',
    MSQMT CHAR(2) COMMENT '测流方法',
    MSAMT CHAR(1) COMMENT '测积方法',
    MSVMT CHAR(1) COMMENT '测速方法',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (STCD, TM),
    INDEX idx_stcd_tm (STCD, TM DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='河道水情表';

-- ==========================================
-- 3. 河道站防洪指标表 (ST_RVFCCH_B)
-- ==========================================
DROP TABLE IF EXISTS ST_RVFCCH_B;
CREATE TABLE ST_RVFCCH_B (
    STCD VARCHAR(8) PRIMARY KEY COMMENT '测站编码',
    LDKELEL DECIMAL(7,3) COMMENT '左堤高程(m)',
    RDKEL DECIMAL(7,3) COMMENT '右堤高程(m)',
    WRZ DECIMAL(7,3) COMMENT '警戒水位(m)',
    WRQ DECIMAL(9,3) COMMENT '警戒流量(m³/s)',
    GRZ DECIMAL(7,3) COMMENT '保证水位(m)',
    GRQ DECIMAL(9,3) COMMENT '保证流量(m³/s)',
    FLPQ DECIMAL(9,3) COMMENT '平滩流量(m³/s)',
    OBHTZ DECIMAL(7,3) COMMENT '实测最高水位(m)',
    OBHTZTM DATETIME COMMENT '实测最高水位出现时间',
    IVHZ DECIMAL(7,3) COMMENT '调查最高水位(m)',
    IVHZTM DATETIME COMMENT '调查最高水位出现时间',
    OBMXQ DECIMAL(9,3) COMMENT '实测最大流量(m³/s)',
    OBMXQTM DATETIME COMMENT '实测最大流量出现时间',
    IVMXQ DECIMAL(9,3) COMMENT '调查最大流量(m³/s)',
    IVMXQTM DATETIME COMMENT '调查最大流量出现时间',
    HMXS DECIMAL(9,3) COMMENT '历史最大含沙量(kg/m³)',
    HMXSTM DATETIME COMMENT '历史最大含沙量出现时间',
    HMXAVV DECIMAL(9,3) COMMENT '历史最大断面平均流速(m/s)',
    HMXAVVTM DATETIME COMMENT '历史最大断面平均流速出现时间',
    HLZ DECIMAL(7,3) COMMENT '历史最低水位(m)',
    HLZTM DATETIME COMMENT '历史最低水位出现时间',
    HMNQ DECIMAL(9,3) COMMENT '历史最小流量(m³/s)',
    HMNQTM DATETIME COMMENT '历史最小流量出现时间',
    TAZ DECIMAL(7,3) COMMENT '高水位告警值(m)',
    TAQ DECIMAL(9,3) COMMENT '大流量告警值(m³/s)',
    LAZ DECIMAL(7,3) COMMENT '低水位告警值(m)',
    LAQ DECIMAL(9,3) COMMENT '小流量告警值(m³/s)',
    SFZ DECIMAL(7,3) COMMENT '启动预报水位标准(m)',
    SFQ DECIMAL(9,3) COMMENT '启动预报流量标准(m³/s)',
    MODITIME DATETIME COMMENT '时间戳',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='河道站防洪指标表';

-- ==========================================
-- 4. 预警信息表 (ST_WARN_INFO)
-- ==========================================
DROP TABLE IF EXISTS ST_WARN_INFO;
CREATE TABLE ST_WARN_INFO (
    WARN_ID BIGINT PRIMARY KEY COMMENT '预警ID',
    STCD VARCHAR(8) COMMENT '测站编码',
    STNM VARCHAR(50) COMMENT '测站名称',
    WARN_TM DATETIME COMMENT '预警时间',
    CURR_Z DECIMAL(7,3) COMMENT '当前水位(m)',
    WRZ DECIMAL(7,3) COMMENT '警戒水位(m)',
    GRZ DECIMAL(7,3) COMMENT '保证水位(m)',
    WARN_LV TINYINT COMMENT '预警级别: 1-警戒 2-保证',
    PROC_STAT TINYINT DEFAULT 0 COMMENT '处理状态: 0-未处理 1-处理中 2-已完成',
    HANDLER VARCHAR(50) COMMENT '处理人',
    HANDLE_TIME DATETIME COMMENT '处理时间',
    HANDLE_REMARK VARCHAR(500) COMMENT '处理备注',
    CONTENT VARCHAR(500) COMMENT '预警内容',
    RESOLVE_TM DATETIME COMMENT '预警解除时间',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_stcd (STCD),
    INDEX idx_warn_tm (WARN_TM DESC),
    INDEX idx_proc_stat (PROC_STAT),
    INDEX idx_warn_lv (WARN_LV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警信息表';

-- ==========================================
-- 5. 预警通知记录表 (ST_WARN_NOTIFY)
-- ==========================================
DROP TABLE IF EXISTS ST_WARN_NOTIFY;
CREATE TABLE ST_WARN_NOTIFY (
    NOTIFY_ID BIGINT PRIMARY KEY COMMENT '通知ID',
    WARN_ID BIGINT COMMENT '预警ID',
    UNIT_TYPE TINYINT COMMENT '接收单位类型: 1-所属单位 2-上级单位',
    RECV_UNIT VARCHAR(20) COMMENT '接收单位编码',
    RECV_UNIT_NAME VARCHAR(100) COMMENT '接收单位名称',
    RECEIVER VARCHAR(50) COMMENT '接收人',
    RECEIVER_PHONE VARCHAR(20) COMMENT '接收人电话',
    NOTIFY_WAY TINYINT COMMENT '通知方式: 1-短信 2-邮件 3-系统通知',
    NOTIFY_STAT TINYINT DEFAULT 0 COMMENT '通知状态: 0-待发送 1-已发送 2-失败',
    SEND_TM DATETIME COMMENT '发送时间',
    FAIL_REASON VARCHAR(500) COMMENT '发送失败原因',
    CONTENT TEXT COMMENT '通知内容',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_warn_id (WARN_ID),
    INDEX idx_notify_stat (NOTIFY_STAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警通知记录表';

-- ==========================================
-- 6. 管理单位关联表 (ST_STATION_UNIT)
-- ==========================================
DROP TABLE IF EXISTS ST_STATION_UNIT;
CREATE TABLE ST_STATION_UNIT (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    STCD VARCHAR(8) COMMENT '测站编码',
    OWN_UNIT VARCHAR(20) COMMENT '所属单位编码',
    PARENT_UNIT VARCHAR(20) COMMENT '上级单位编码',
    UNIT_NAME VARCHAR(100) COMMENT '单位名称',
    CONTACT VARCHAR(50) COMMENT '单位联系人',
    PHONE VARCHAR(20) COMMENT '联系电话',
    NOTIFY_WAY CHAR(1) DEFAULT '3' COMMENT '通知方式: 1-短信 2-邮件 3-系统通知',
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_stcd_unit (STCD, OWN_UNIT),
    INDEX idx_stcd (STCD)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理单位关联表';

-- ==========================================
-- 插入测试数据
-- ==========================================

-- 插入测站数据
INSERT INTO ST_STBPRP_B (STCD, STNM, BASIN, RIVER, STLC, STTP, ADMAUTH) VALUES
('10010001', '长江武汉段', '长江', '长江', '武汉市江汉区', 'ZZ', '武汉市水利局'),
('10010002', '汉江武汉段', '长江', '汉江', '武汉市硚口区', 'ZZ', '武汉市水利局'),
('10010003', '荆江河段', '长江', '荆江', '荆州市沙市区', 'ZZ', '荆州市水利局'),
('10010004', '黄石港', '长江', '长江', '黄石市黄石港区', 'ZZ', '黄石市水利局'),
('10010005', '宜昌水文站', '长江', '长江', '宜昌市西陵区', 'ZZ', '宜昌市水利局');

-- 插入防洪指标数据
INSERT INTO ST_RVFCCH_B (STCD, WRZ, GRZ, WRQ, GRQ) VALUES
('10010001', 25.000, 27.000, 50000, 60000),
('10010002', 26.000, 28.000, 8000, 10000),
('10010003', 42.000, 45.000, 30000, 40000),
('10010004', 24.000, 26.000, 40000, 50000),
('10010005', 50.000, 55.000, 50000, 60000);

-- 插入管理单位数据
INSERT INTO ST_STATION_UNIT (STCD, OWN_UNIT, PARENT_UNIT, UNIT_NAME, CONTACT, PHONE, NOTIFY_WAY) VALUES
('10010001', 'UNIT001', 'UNIT00101', '武汉市水利局', '张局长', '13800138001', '1'),
('10010001', 'UNIT00101', 'UNIT00102', '湖北省水利厅', '李厅长', '13800138002', '1'),
('10010002', 'UNIT001', 'UNIT00101', '武汉市水利局', '张局长', '13800138001', '1'),
('10010002', 'UNIT00101', 'UNIT00102', '湖北省水利厅', '李厅长', '13800138002', '1'),
('10010003', 'UNIT002', 'UNIT00101', '荆州市水利局', '王局长', '13800138003', '1'),
('10010003', 'UNIT00101', 'UNIT00102', '湖北省水利厅', '李厅长', '13800138002', '1'),
('10010004', 'UNIT003', 'UNIT00101', '黄石市水利局', '刘局长', '13800138004', '1'),
('10010004', 'UNIT00101', 'UNIT00102', '湖北省水利厅', '李厅长', '13800138002', '1'),
('10010005', 'UNIT004', 'UNIT00101', '宜昌市水利局', '陈局长', '13800138005', '1'),
('10010005', 'UNIT00101', 'UNIT00102', '湖北省水利厅', '李厅长', '13800138002', '1');

-- 插入水位测试数据
INSERT INTO ST_RIVER_R (STCD, TM, Z, Q, WPTN) VALUES
('10010001', NOW(), 23.500, 45000, '4'),
('10010002', NOW(), 24.800, 7500, '4'),
('10010003', NOW(), 40.500, 28000, '4'),
('10010004', NOW(), 22.300, 38000, '5'),
('10010005', NOW(), 48.200, 48000, '4');

-- ==========================================
-- 创建用户和授权
-- ==========================================
-- 注意：实际生产环境中请使用更安全的密码
-- CREATE USER 'river_user'@'%' IDENTIFIED BY 'river_pass';
-- GRANT ALL PRIVILEGES ON river_warning.* TO 'river_user'@'%';
-- FLUSH PRIVILEGES;
