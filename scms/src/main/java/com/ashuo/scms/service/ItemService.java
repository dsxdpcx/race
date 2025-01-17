package com.ashuo.scms.service;

import com.ashuo.scms.entity.Item;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author AShuo
 * @since 2021-04-01
 */
public interface ItemService extends IService<Item> {
    IPage<Item> getItemByItemCondition(Page<Item> page, Item item);

    Item getOneItemByCondition(Item item);

    int addItem(Item item);

    int modifyItem(Item item);

    int removeItem(int itemId);

    List<Item> getItemTemplateList();

    Item getItemTemplateDetail(Item item);

    IPage<Item> getItemByExclude(Page<Item> page, Item item);

    IPage<Item> getItemBysignCondition(Page<Item> page, Item item);
}
