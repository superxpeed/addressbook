package com.addressbook.ignite

import com.addressbook.dto.MenuEntryDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class MenuCreator {

    @Autowired
    var igniteDao: IgniteClient? = null

    @PostConstruct
    fun initMenu() {
        igniteDao?.clearMenus()
        var root = MenuEntryDto("/root", "Root entry", Collections.singletonList("USER"))
        root = igniteDao?.createOrUpdateMenuEntry(root, null)!!
        var firstLevel = MenuEntryDto("/level1", "First level", Collections.singletonList("USER"))
        firstLevel = igniteDao?.createOrUpdateMenuEntry(firstLevel, root.id)!!
        var secondLevelOne = MenuEntryDto("/level2", "Second level 1", Collections.singletonList("USER"))
        secondLevelOne = igniteDao?.createOrUpdateMenuEntry(secondLevelOne, firstLevel.id)!!
        val secondLevelTwo = MenuEntryDto("/adminPage", "Admin page", Collections.singletonList("ADMIN"))
        var thirdLevel = MenuEntryDto("/level3", "Third level", Collections.singletonList("USER"))
        thirdLevel = igniteDao?.createOrUpdateMenuEntry(thirdLevel, secondLevelOne.id)!!
        val lastLevel = MenuEntryDto("/lastLevel", "Last level", Collections.singletonList("USER"))
        igniteDao?.createOrUpdateMenuEntry(secondLevelTwo, firstLevel.id)
        igniteDao?.createOrUpdateMenuEntry(lastLevel, thirdLevel.id)
    }
}
