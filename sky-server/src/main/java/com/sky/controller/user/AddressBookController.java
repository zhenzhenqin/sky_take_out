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

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @ApiOperation("设置默认地址")
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址:{}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     *  查询默认地址
     * @return
     */
    @ApiOperation("查询默认地址")
    @GetMapping("/default")
    public Result getDefault(){
        log.info("查询到的默认地址的用户id:{}", BaseContext.getCurrentId());
        AddressBook addressBook = addressBookService.getDefault(BaseContext.getCurrentId());
        return Result.success(addressBook);
    }

    /**
     * 根据id删除地址簿
     * @param id
     * @return
     */
    @ApiOperation("根据id删除地址簿")
    @DeleteMapping
    public Result deleteById(Long id){
        log.info("根据id删除地址簿:{}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }


}
