package com.sky.service;
import com.sky.entity.AddressBook;
import org.apache.poi.ss.formula.functions.Address;

import java.util.List;



public interface AddressBookService {

    List<AddressBook> getAddress(Long id);

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);

    void update(AddressBook addressBook);

    List<AddressBook> getDefaultAddress(Long id);
}
