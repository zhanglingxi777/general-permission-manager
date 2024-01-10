package top.zhouyang.admin.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.zhouyang.admin.domain.Permission;
import top.zhouyang.admin.domain.vo.RouterVO;
import top.zhouyang.admin.service.IMenuTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class MenuTreeImpl implements IMenuTree {
    @Override
    public List<RouterVO> makeRouter(List<Permission> menuList, Long pid) {
        ArrayList<RouterVO> routerVOList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream().filter(item -> Objects.nonNull(item) && Objects.equals(item.getParentId(), pid))
                .forEach(item -> {
                    RouterVO routerVO = new RouterVO();
                    routerVO.setName(item.getName());
                    routerVO.setPath(item.getPath());
                    if (item.getParentId() == 0L) {
                        routerVO.setComponent("Layout");
                        routerVO.setAlwaysShow(true);
                    } else {
                        routerVO.setComponent(item.getUrl());
                        routerVO.setAlwaysShow(false);
                    }
                    routerVO.setMeta(new RouterVO.Meta(item.getLabel(), item.getIcon(), item.getCode().split(",")));
                    List<RouterVO> children = makeRouter(menuList, item.getId());
                    routerVO.setChildren(children);
                    routerVOList.add(routerVO);
                });
        return routerVOList;
    }

    @Override
    public List<Permission> makeMenuTree(List<Permission> menuList, Long pid) {
        List<Permission> permissionList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream().filter(item -> Objects.nonNull(item) && Objects.equals(item.getParentId(), pid))
                .forEach(item -> {
                    Permission permission = new Permission();
                    BeanUtils.copyProperties(item, permission);
                    List<Permission> children = makeMenuTree(menuList, item.getId());
                    permission.setChildren(children);
                    permissionList.add(permission);
                });
        return permissionList;
    }
}
