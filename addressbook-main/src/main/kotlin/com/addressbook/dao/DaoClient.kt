package com.addressbook.dao

import com.addressbook.AddressBookDAO
import org.springframework.cloud.openfeign.FeignClient

@FeignClient("\${dao.implementation}")
interface DaoClient : AddressBookDAO