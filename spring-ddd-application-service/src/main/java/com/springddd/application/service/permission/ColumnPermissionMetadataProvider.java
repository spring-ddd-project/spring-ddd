package com.springddd.application.service.permission;

import com.springddd.domain.permission.EntityColumnMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColumnPermissionMetadataProvider {

    public List<EntityColumnMetadata> getAllMetadata() {
        return List.of(
                new EntityColumnMetadata("sys_user", "用户管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("username", "用户名"),
                        new EntityColumnMetadata.ColumnInfo("password", "密码"),
                        new EntityColumnMetadata.ColumnInfo("phone", "手机号"),
                        new EntityColumnMetadata.ColumnInfo("avatar", "头像"),
                        new EntityColumnMetadata.ColumnInfo("email", "邮箱"),
                        new EntityColumnMetadata.ColumnInfo("sex", "性别"),
                        new EntityColumnMetadata.ColumnInfo("lockStatus", "锁定状态"),
                        new EntityColumnMetadata.ColumnInfo("deptId", "部门ID"),
                        new EntityColumnMetadata.ColumnInfo("createBy", "创建人"),
                        new EntityColumnMetadata.ColumnInfo("updateBy", "更新人"),
                        new EntityColumnMetadata.ColumnInfo("createTime", "创建时间"),
                        new EntityColumnMetadata.ColumnInfo("updateTime", "更新时间")
                )),
                new EntityColumnMetadata("sys_dept", "部门管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("parentId", "上级部门"),
                        new EntityColumnMetadata.ColumnInfo("deptName", "部门名称"),
                        new EntityColumnMetadata.ColumnInfo("leader", "联系人"),
                        new EntityColumnMetadata.ColumnInfo("phone", "电话"),
                        new EntityColumnMetadata.ColumnInfo("sortOrder", "排序"),
                        new EntityColumnMetadata.ColumnInfo("deptStatus", "状态"),
                        new EntityColumnMetadata.ColumnInfo("createBy", "创建人"),
                        new EntityColumnMetadata.ColumnInfo("updateBy", "更新人"),
                        new EntityColumnMetadata.ColumnInfo("createTime", "创建时间"),
                        new EntityColumnMetadata.ColumnInfo("updateTime", "更新时间")
                )),
                new EntityColumnMetadata("sys_role", "角色管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("roleName", "角色名称"),
                        new EntityColumnMetadata.ColumnInfo("roleCode", "角色编码"),
                        new EntityColumnMetadata.ColumnInfo("roleDesc", "角色描述"),
                        new EntityColumnMetadata.ColumnInfo("dataScope", "数据范围"),
                        new EntityColumnMetadata.ColumnInfo("dataPermission", "数据权限"),
                        new EntityColumnMetadata.ColumnInfo("roleStatus", "状态"),
                        new EntityColumnMetadata.ColumnInfo("ownerStatus", "负责人状态"),
                        new EntityColumnMetadata.ColumnInfo("deptId", "部门ID"),
                        new EntityColumnMetadata.ColumnInfo("createBy", "创建人"),
                        new EntityColumnMetadata.ColumnInfo("updateBy", "更新人"),
                        new EntityColumnMetadata.ColumnInfo("createTime", "创建时间"),
                        new EntityColumnMetadata.ColumnInfo("updateTime", "更新时间")
                )),
                new EntityColumnMetadata("sys_dict", "字典管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("dictName", "字典名称"),
                        new EntityColumnMetadata.ColumnInfo("dictCode", "字典编码"),
                        new EntityColumnMetadata.ColumnInfo("dictDesc", "字典描述"),
                        new EntityColumnMetadata.ColumnInfo("dictStatus", "状态"),
                        new EntityColumnMetadata.ColumnInfo("createBy", "创建人"),
                        new EntityColumnMetadata.ColumnInfo("updateBy", "更新人"),
                        new EntityColumnMetadata.ColumnInfo("createTime", "创建时间"),
                        new EntityColumnMetadata.ColumnInfo("updateTime", "更新时间")
                )),
                new EntityColumnMetadata("sys_dict_item", "字典项管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("dictId", "字典ID"),
                        new EntityColumnMetadata.ColumnInfo("itemLabel", "标签"),
                        new EntityColumnMetadata.ColumnInfo("itemValue", "值"),
                        new EntityColumnMetadata.ColumnInfo("sortOrder", "排序"),
                        new EntityColumnMetadata.ColumnInfo("itemStatus", "状态"),
                        new EntityColumnMetadata.ColumnInfo("createBy", "创建人"),
                        new EntityColumnMetadata.ColumnInfo("updateBy", "更新人"),
                        new EntityColumnMetadata.ColumnInfo("createTime", "创建时间"),
                        new EntityColumnMetadata.ColumnInfo("updateTime", "更新时间")
                )),
                new EntityColumnMetadata("sys_menu", "菜单管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("parentId", "上级菜单"),
                        new EntityColumnMetadata.ColumnInfo("menuName", "菜单名称"),
                        new EntityColumnMetadata.ColumnInfo("menuType", "菜单类型"),
                        new EntityColumnMetadata.ColumnInfo("path", "路径"),
                        new EntityColumnMetadata.ColumnInfo("component", "组件"),
                        new EntityColumnMetadata.ColumnInfo("permission", "权限标识"),
                        new EntityColumnMetadata.ColumnInfo("icon", "图标"),
                        new EntityColumnMetadata.ColumnInfo("sortOrder", "排序"),
                        new EntityColumnMetadata.ColumnInfo("menuStatus", "状态"),
                        new EntityColumnMetadata.ColumnInfo("createBy", "创建人"),
                        new EntityColumnMetadata.ColumnInfo("updateBy", "更新人"),
                        new EntityColumnMetadata.ColumnInfo("createTime", "创建时间"),
                        new EntityColumnMetadata.ColumnInfo("updateTime", "更新时间")
                ))
        );
    }
}
