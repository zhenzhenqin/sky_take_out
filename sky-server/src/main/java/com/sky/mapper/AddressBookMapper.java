package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book (user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label)\n" +
            "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label})")
    void insert(AddressBook addressBook);


    /**
     * 条件查询地址簿
     * @param addressBook
     * @return 地址簿列表
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 根据id查询地址详情
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getAddressById(Long id);
}
