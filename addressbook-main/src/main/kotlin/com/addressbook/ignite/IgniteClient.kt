package com.addressbook.ignite

import com.addressbook.AddressBookDAO
import org.springframework.cloud.openfeign.FeignClient

@FeignClient("postgre-server")
interface IgniteClient : AddressBookDAO