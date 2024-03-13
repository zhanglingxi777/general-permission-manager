package top.zhanglingxi.admin.service;


import top.zhanglingxi.admin.domain.Permission;
import top.zhanglingxi.admin.domain.vo.RouterVO;

import java.util.List;

public interface IMenuTree {
    /**
     * 生成路由
     * @param menuList 菜单列表
     * @param pid      父菜单Id
     * @return         路由
     */
    List<RouterVO> makeRouter(List<Permission> menuList, Long pid);

    /**
     * 生成菜单树
     * @param menuList 菜单列表
     * @param pid      父菜单ID
     * @return         菜单树
     */
    List<Permission> makeMenuTree(List<Permission> menuList, Long pid);
}
