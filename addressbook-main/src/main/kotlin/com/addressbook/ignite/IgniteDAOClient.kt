package com.addressbook.ignite

import com.addressbook.AddressBookDAO
import org.springframework.cloud.openfeign.FeignClient

@FeignClient("ignite-server")
interface IgniteDAOClient : AddressBookDAO {
}