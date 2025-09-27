# Commit 规范

## 作者信息

- 作者: henrikabel25 <henrikabel@foxmail.com>
- 起始日期: 基准 commit 的后一天，10:00:00
- 每日数量: 1-10 个 commits（随机分布）
- 时分秒: 统一使用 10:00:00

## Commit 消息格式

```
test(<module>): <task-id> <short-description>

详细描述测试内容。

Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>
```

## 格式说明

- `<module>`: 模块名称 (dept, dict, dict-item, menu, role, user, gen, domain, auth, cache, infra-persistence, infra-cache, launcher, integration)
- `<task-id>`: 任务编号 (A-01 到 F-05)
- `<short-description>`: 简短描述，使用英文或中文均可

## 示例

```
test(dept): A-01 Dept aggregate root tests

Added comprehensive tests for SysDeptDomain including:
- Domain creation and lifecycle methods
- DeptId value object validation
- Domain event behavior

Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>
```

## 任务分类

### Category A: 领域层测试（35 任务）
- A-01 到 A-35: dept, dict, dict-item, menu, role, user, gen 模块

### Category B: 领域服务/工具类测试（15 任务）
- B-01 到 B-15: gen, role-menu, user-role, domain 基础类/工具类

### Category C: 应用服务测试（25 任务）
- C-01 到 C-25: 各模块 CommandService, QueryService, FactoryImpl

### Category D: Controller 测试（10 任务）
- D-01 到 D-10: 各模块 Controller

### Category E: 基础设施测试（15 任务）
- E-01 到 E-12: RepositoryImpl, Cache, Entity, Launcher

### Category F: 集成与覆盖率验证（5 任务）
- F-01 到 F-05: 跨模块集成, JaCoCo 验证
