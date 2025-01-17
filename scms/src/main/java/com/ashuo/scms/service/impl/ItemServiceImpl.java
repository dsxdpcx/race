package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Item;
import com.ashuo.scms.mapper.ItemMapper;
import com.ashuo.scms.service.ItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author AShuo
 * @since 2021-04-01
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper,Item> implements ItemService {

    @Autowired
    ItemMapper itemMapper;

    @Override
    public IPage<Item> getItemByItemCondition(Page<Item> page, Item item) {
        IPage<Item> itemList = itemMapper.queryItemByItemCondition(page, item);
        if (itemList.getRecords() != null && itemList.getRecords().size() > 0) ;
        //项目名称加上性别
        itemList.setRecords(itemList.getRecords().stream().map(i -> i.setItemName(i.getItemName() + " (" + i.getItemSex() + ")")).collect(Collectors.toList()));
        itemList.setRecords(itemList.getRecords().stream().map(i -> {String process =i.getProcess();if("finals".equals(process)){return i.setItemName(i.getItemName() + " (" + "决赛" + ")");}else if("semifinals".equals(process)){return i.setItemName(i.getItemName()+"(半决赛)");}else{
            return i.setItemName(i.getItemName() + " (" + "预赛" + ")");}})
        .collect(Collectors.toList()));
        return itemList;
    }


    @Override
    public Item getOneItemByCondition(Item itemCondition) {
        if (itemCondition == null) {
            return null;
        }
        Item item = itemMapper.queryOneItemByItemCondition(itemCondition);
        if (item != null) {
            item.setItemName(item.getItemName() + " (" + item.getItemSex() + ")");
            item.setItemName(item.getItemName() + " (" + item.getProcess() + ")");
        }
        return item;
    }


    @Override
    public int addItem(Item item) {
        if (item == null) {
            return 0;
        } else {
            int effNum = itemMapper.insertItem(item);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int modifyItem(Item item) {
        if (item == null) {
            return 0;
        } else {
            int effNum = 0;
            effNum = itemMapper.updateItem(item);

            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int removeItem(int itemId) {
        if (itemId == 0) {
            return 0;
        } else {
            int effNum = itemMapper.deleteItem(itemId);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public List<Item> getItemTemplateList() {
        List<Item> itemList = itemMapper.queryItemTemplateList();
        return itemList;
    }

    @Override
    public Item getItemTemplateDetail(Item itemCondition) {
        if (itemCondition == null) {
            return null;
        }
        Item item = itemMapper.queryItemTemplateDetail(itemCondition);
        return item;
    }

    @Override
    public IPage<Item> getItemByExclude(Page<Item> page, Item item) {

        IPage<Item> itemList = itemMapper.queryItemByExclude(page, item);
        if (itemList.getRecords() != null && itemList.getRecords().size() > 0) ;
        //项目名称加上性别
        itemList.setRecords(itemList.getRecords().stream().map(i -> i.setItemName(i.getItemName() + " (" + i.getItemSex() + ")")).collect(Collectors.toList()));
        itemList.setRecords(itemList.getRecords().stream().map(i -> {String process =i.getProcess();if("finals".equals(process)){return i.setItemName(i.getItemName() + " (" + "决赛" + ")");}else if("semifinals".equals(process)){return i.setItemName(i.getItemName()+"(半决赛)");}else{
                    return i.setItemName(i.getItemName() + " (" + "预赛" + ")");}})
                .collect(Collectors.toList()));
        return itemList;

    }

    @Override
    public IPage<Item> getItemBysignCondition(Page<Item> page, Item item) {

        IPage<Item> itemList = itemMapper.queryItemBysignCondition(page, item);
        if (itemList.getRecords() != null && itemList.getRecords().size() > 0) ;
        //项目名称加上性别
        itemList.setRecords(itemList.getRecords().stream().map(i -> i.setItemName(i.getItemName() + " (" + i.getItemSex() + ")")).collect(Collectors.toList()));
        itemList.setRecords(itemList.getRecords().stream().map(i -> {String process =i.getProcess();if("finals".equals(process)){return i.setItemName(i.getItemName() + " (" + "决赛" + ")");}else if("semifinals".equals(process)){return i.setItemName(i.getItemName()+"(半决赛)");}else{
                    return i.setItemName(i.getItemName() + " (" + "预赛" + ")");}})
                .collect(Collectors.toList()));
        return itemList;
    }

}
