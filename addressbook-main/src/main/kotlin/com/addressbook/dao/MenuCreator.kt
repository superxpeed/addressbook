package com.addressbook.dao

import com.addressbook.dto.MenuEntryDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class MenuCreator {

    @Autowired
    lateinit var dao: DaoClient

    @PostConstruct
    fun initMenu() {
        dao.clearMenus()
        var root = MenuEntryDto("/root", "Home", Collections.singletonList("USER"))
        root = dao.createOrUpdateMenuEntry(root, null)
        var firstLevel = MenuEntryDto("/level1", "Level 1", Collections.singletonList("USER"))
        firstLevel = dao.createOrUpdateMenuEntry(firstLevel, root.id)
        var secondLevelOne = MenuEntryDto("/level2", "Level 2", Collections.singletonList("USER"))
        secondLevelOne = dao.createOrUpdateMenuEntry(secondLevelOne, firstLevel.id)
        val secondLevelTwo = MenuEntryDto("/adminPage", "Admin page", Collections.singletonList("ADMIN"))
        var thirdLevel = MenuEntryDto("/level3", "Level 3", Collections.singletonList("USER"))
        thirdLevel = dao.createOrUpdateMenuEntry(thirdLevel, secondLevelOne.id)
        val lastLevel = MenuEntryDto("/lastLevel", "Last level", Collections.singletonList("USER"))
        dao.createOrUpdateMenuEntry(secondLevelTwo, firstLevel.id)
        dao.createOrUpdateMenuEntry(lastLevel, thirdLevel.id)
    }
}
