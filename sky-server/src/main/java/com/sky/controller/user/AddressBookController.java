package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿相关接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @ApiOperation("新增地址")
    @PostMapping
    public Result add(@RequestBody AddressBook addressBook){
        log.info("新增地址:{}",addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }

    /**
     * 查询当前用户的所有地址信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户的所有地址信息")
    public Result<List<AddressBook>> list(){
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = AddressBook.builder()
                .userId(userId)
                .build();

        log.info("查询当前用户所有地址信息:{}",addressBook);

        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 根据id查询地址详情
     * @param id
     * @return
     */
    @ApiOperation("根据id查询地址详情")
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable Long id){
        log.info("根据id查询地址:{}", id);
        AddressBook addressBook = addressBookService.getAddressById(id);
        return Result.success(addressBook);
    }

    /**
     * 更新地址
     * @param addressBook
     * @return
     */
    @ApiOperation("更新地址")
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        log.info("更新地址:{}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }


}
