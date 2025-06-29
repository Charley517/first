# 物流订单跟踪系统

## 项目简介
本项目是一个基于 Spring Boot + Vue3 的物流订单跟踪系统，支持快递下单、物流状态更新、配送员管理及实时位置追踪等功能。

## 技术栈
### 后端
- Spring Boot 2.7.0
- Spring Data MongoDB
- Spring Data Redis
- RabbitMQ
- Spring AOP

### 前端
- Vue 3
- Vite
- Element Plus
- 高德地图 API
- ECharts

## 核心功能
- 订单管理：寄件人/收件人信息录入、运费计算、订单状态跟踪
- 物流轨迹：使用 MongoDB 存储物流状态变更记录
- 地图集成：调用高德地图 API 展示实时配送路径
- 短信通知：物流状态变更时触发通知
- 配送员绩效统计
- 常用地址缓存：使用 Redis 保存用户常用地址

## 项目结构
```
logistics-system/
├── backend/                          # Spring Boot 后端
├── frontend/                         # Vue3 前端
└── doc/                             # 项目文档
```
项目相关文档位于 `doc/` 目录，后续会在其中补充更详细的说明。

## 开发环境要求
- JDK 11+
- Node.js 16+
- MongoDB 4.4+
- Redis 6.0+
- RabbitMQ 3.8+

## Development Setup

- Ensure [Maven](https://maven.apache.org/) is installed or include the Maven wrapper script (`mvnw`).
- Run tests with `mvn test` from the `backend` directory.

## 快速开始

### 后端启动
1. 进入 backend 目录
2. 执行 `mvn spring-boot:run`

### 前端启动
1. 进入 frontend 目录
2. 执行 `npm install`
3. 执行 `npm run dev`

## 配置说明
1. 在 `backend/src/main/resources/application.yml` 中配置数据库连接信息
2. 在 `frontend/.env` 中配置高德地图 API Key

## 注意事项
- 请确保 MongoDB、Redis、RabbitMQ 服务已启动
- 使用前请替换高德地图 API Key
- 建议使用 IDE 进行开发（推荐 IntelliJ IDEA 和 VS Code） 