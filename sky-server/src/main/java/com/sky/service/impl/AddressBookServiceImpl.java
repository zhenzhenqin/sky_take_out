package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询当前用户的所有地址信息
     * @param addressBook
     * @return
     */
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        List<AddressBook> addressBookList = addressBookMapper.list(addressBook);
        return addressBookList;
    }

    /**
     * 根据id查询地址详情
     * @param id
     * @return
     */
    @Override
    public AddressBook getAddressById(Long id) {
        AddressBook addressBook = addressBookMapper.getAddressById(id);
        return addressBook;
    }

    /**
     * 修改地址
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        //将当前用户所有地址设置为非默认地址  !!!!!!!!!!!!!!!!!!!!!!!!!!
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.setIsDefaultByUserId(addressBook);

        //将当前用户选中地址设置为默认地址
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId()); //放入当前用户id
        addressBookMapper.update(addressBook);
    }

    /**
     * 获取默认地址
     * @param currentId
     * @return
     */
    @Override
    public AddressBook getDefault(Long currentId) {
        AddressBook addressBook = AddressBook.builder()
                .userId(currentId)
                .isDefault(1)
                .build();
        AddressBook address = addressBookMapper.getAddressByUserIdAndIsDefault(addressBook);
        return address;
    }

    /**
     * 根据id删除地址
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }
}
