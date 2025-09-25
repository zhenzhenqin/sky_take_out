package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 新增地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

    /**
     * 查询当前用户的所有地址信息
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 根据id查询地址详情
     * @param id
     * @return
     */
    AddressBook getAddressById(Long id);

    /**
     * 更新地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * 获取默认地址
     * @param currentId
     * @return
     */
    AddressBook getDefault(Long currentId);

    /**
     * 删除地址根据id
     * @param id
     */
    void deleteById(Long id);
}
