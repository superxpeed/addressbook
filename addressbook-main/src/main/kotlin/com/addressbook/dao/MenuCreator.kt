package com.addressbook.dao

import com.addressbook.dto.MenuEntryDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class MenuCreator {

    @Autowired
    lateinit var daoDao: DaoClient

    @PostConstruct
    fun initMenu() {
        daoDao.clearMenus()
        var root = MenuEntryDto("/root", "Root entry", Collections.singletonList("USER"))
        root = daoDao.createOrUpdateMenuEntry(root, null)
        var firstLevel = MenuEntryDto("/level1", "First level", Collections.singletonList("USER"))
        firstLevel = daoDao.createOrUpdateMenuEntry(firstLevel, root.id)
        var secondLevelOne = MenuEntryDto("/level2", "Second level 1", Collections.singletonList("USER"))
        secondLevelOne = daoDao.createOrUpdateMenuEntry(secondLevelOne, firstLevel.id)
        val secondLevelTwo = MenuEntryDto("/adminPage", "Admin page", Collections.singletonList("ADMIN"))
        var thirdLevel = MenuEntryDto("/level3", "Third level", Collections.singletonList("USER"))
        thirdLevel = daoDao.createOrUpdateMenuEntry(thirdLevel, secondLevelOne.id)
        val lastLevel = MenuEntryDto("/lastLevel", "Last level", Collections.singletonList("USER"))
        daoDao.createOrUpdateMenuEntry(secondLevelTwo, firstLevel.id)
        daoDao.createOrUpdateMenuEntry(lastLevel, thirdLevel.id)
    }
}
