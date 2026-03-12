---
AIGC:
    ContentProducer: Minimax Agent AI
    ContentPropagator: Minimax Agent AI
    Label: AIGC
    ProduceID: "00000000000000000000000000000000"
    PropagateID: "00000000000000000000000000000000"
    ReservedCode1: 3045022033db164e70579ab2f23ff4a1a41482ca44fbb710a1558f1245870f5a2f0e3291022100c8b4d3b849fdbda98018c7c2b14a63fc9643957eb95c94e142777d9ac9630e23
    ReservedCode2: 304502210088c88f2a3bf17d0ffa93811ae94337217bcfe8c15f4454072b33037918f186460220054a7809072f74379b778f3f487f6a08ee471e8f1f12529800edab57f7732cdc
---

# 河道水位预警系统

> Java企业级项目 - Spring Boot 3.2 + MyBatis Plus + MySQL

## 项目介绍

本系统是根据水利信息化标准设计的河道水位预警管理系统，实现了对河道测站水位的实时监测、自动预警和分级通知功能。

## 功能特性

- **实时监测**：每分钟采集河道水位数据
- **智能预警**：根据警戒水位和保证水位自动判断预警级别
- **分级通知**：根据预警级别通知所属单位或上级单位
- **预警跟踪**：支持预警处理状态的全流程管理

## 技术栈

- **后端框架**：Spring Boot 3.2
- **ORM框架**：MyBatis Plus 3.5
- **数据库**：MySQL 8.0
- **缓存**：Redis
- **API文档**：Knife4j (Swagger 3.0)
- **任务调度**：Spring @Scheduled

## 项目结构

```
com.water.warning
├── common              # 公共模块
│   ├── result         # 统一返回结果封装
│   └── exception      # 全局异常处理
├── config             # 配置模块
└── modules
    ├── base           # 测站管理模块
    │   ├── entity      # 实体类
    │   ├── mapper      # 数据访问层
    │   └── controller  # 接口控制器
    ├── monitor         # 监测数据模块
    │   ├── entity
    │   ├── mapper
    │   ├── service    # 业务逻辑
    │   ├── dto        # 数据传输对象
    │   └── controller
    ├── warning         # 预警管理模块
    │   ├── entity
    │   ├── mapper
    │   ├── service
    │   ├── dto
    │   └── controller
    └── notify          # 通知管理模块
        ├── entity
        ├── mapper
        └── service
└── job                # 定时任务
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis (可选，用于缓存)

### 2. 数据库初始化

```bash
# 执行SQL脚本创建数据库和表
mysql -u root -p < sql/init.sql
```

### 3. 配置修改

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/river_warning
    username: root
    password: your_password
```

### 4. 编译运行

```bash
# 编译项目
mvn clean package -DskipTests

# 运行项目
java -jar target/river-warning-system-1.0.0.jar
```

### 5. 访问系统

- API文档：http://localhost:8080/api/doc.html
- Swagger UI：http://localhost:8080/api/swagger-ui.html

## API接口说明

### 监测数据接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/monitor/water/report` | POST | 水位数据上报 |
| `/api/monitor/realtime` | GET | 获取所有测站实时水位 |
| `/api/monitor/realtime/{stcd}` | GET | 获取指定测站实时水位 |
| `/api/monitor/threshold/{stcd}` | GET | 获取测站防洪指标 |

### 预警管理接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/warning/active` | GET | 获取活动预警 |
| `/api/warning/list` | GET | 获取预警列表 |
| `/api/warning/{warnId}` | GET | 获取预警详情 |
| `/api/warning/handle` | POST | 处理预警 |
| `/api/warning/{warnId}/notifies` | GET | 获取预警通知记录 |

### 测站管理接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/station/list` | GET | 获取所有测站 |
| `/api/station/{stcd}` | GET | 获取测站详情 |
| `/api/station` | POST | 添加测站 |
| `/api/station` | PUT | 更新测站 |
| `/api/station/{stcd}/units` | GET | 获取测站管理单位 |

## 业务流程

```
1. 测站上报水位数据
        ↓
2. 数据校验与存储
        ↓
3. 关联防洪指标表获取警戒水位/保证水位
        ↓
4. 水位分析与预警判断
        ├─ 水位 < 警戒水位 → 正常状态
        ├─ 警戒水位 ≤ 水位 < 保证水位 → 警戒预警 → 通知所属单位
        └─ 水位 ≥ 保证水位 → 保证预警 → 通知所属单位+上级单位
        ↓
5. 生成预警记录和通知记录
        ↓
6. 异步发送通知
```

## 预警级别说明

| 级别 | 水位条件 | 通知对象 |
|------|----------|----------|
| 正常 | 水位 < 警戒水位 | - |
| 警戒预警 | 警戒水位 ≤ 水位 < 保证水位 | 所属管理单位 |
| 保证预警 | 水位 ≥ 保证水位 | 所属管理单位 + 上级管理单位 |

## 通知方式

系统支持多种通知方式：
- 1 - 短信通知
- 2 - 邮件通知
- 3 - 系统通知（App推送）

## 测试用例

### 测试1：正常水位上报

```bash
curl -X POST 'http://localhost:8080/api/monitor/water/report' \
  -H 'Content-Type: application/json' \
  -d '{
    "stcd": "10010001",
    "tm": "2024-03-12 10:00:00",
    "z": 23.500
  }'
```

预期结果：数据保存成功，不触发预警

### 测试2：触发警戒预警

```bash
curl -X POST 'http://localhost:8080/api/monitor/water/report' \
  -H 'Content-Type: application/json' \
  -d '{
    "stcd": "10010001",
    "tm": "2024-03-12 10:05:00",
    "z": 26.000
  }'
```

预期结果：触发警戒预警，生成预警记录和通知记录

### 测试3：触发保证预警

```bash
curl -X POST 'http://localhost:8080/api/monitor/water/report' \
  -H 'Content-Type: application/json' \
  -d '{
    "stcd": "10010001",
    "tm": "2024-03-12 10:10:00",
    "z": 28.000
  }'
```

预期结果：触发保证预警，通知所属单位和上级单位

## 扩展说明

### 1. 接入真实传感器

当前系统通过API接收水位数据，实际项目中可以：
- 集成MQTT协议接收传感器数据
- 接入物联网平台
- 配置定时任务模拟数据

### 2. 通知服务集成

当前通知服务为模拟实现，生产环境可集成：
- 短信服务：阿里云短信、腾讯云短信
- 邮件服务：SendGrid、阿里云邮件
- App推送：极光推送、个推

## 许可证

MIT License
