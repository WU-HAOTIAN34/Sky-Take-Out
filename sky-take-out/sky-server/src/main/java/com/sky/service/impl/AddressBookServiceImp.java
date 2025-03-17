package com.sky.service.impl;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;



@Service
public class AddressBookServiceImp implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;



    public List<AddressBook> getAddress(Long id){
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(id);
        return addressBookMapper.query(addressBook);
    }

    public List<AddressBook> getDefaultAddress(Long id){
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(id);
        addressBook.setIsDefault(1);
        return addressBookMapper.query(addressBook);
    }


    public void save(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.save(addressBook);
    }

    public AddressBook getById(Long id){
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        return addressBookMapper.query(addressBook).get(0);
    }


    public void setDefault(AddressBook addressBook) {

        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

}
