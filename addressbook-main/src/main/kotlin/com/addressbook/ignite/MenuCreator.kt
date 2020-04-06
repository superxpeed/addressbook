package com.addressbook.ignite;

import com.addressbook.dto.MenuEntryDto;
import java.util.Collections;

object MenuCreator {
    fun initMenu() {
        GridDAO.clearMenus();
        var root               = MenuEntryDto("/root",         "Root entry",       Collections.singletonList("USER"));
        root = GridDAO.createOrUpdateMenuEntry(root, null);
        var firstLevel         = MenuEntryDto("/level1",       "First level",      Collections.singletonList("USER"));
        firstLevel = GridDAO.createOrUpdateMenuEntry(firstLevel, root);
        var secondLevelOne     = MenuEntryDto("/level2",       "Second level 1",   Collections.singletonList("USER"));
        secondLevelOne = GridDAO.createOrUpdateMenuEntry(secondLevelOne, firstLevel);
        val secondLevelTwo     = MenuEntryDto("/adminPage",    "Admin page",       Collections.singletonList("ADMIN"));
        var thirdLevel         = MenuEntryDto("/level3",       "Third level",      Collections.singletonList("USER"));
        thirdLevel = GridDAO.createOrUpdateMenuEntry(thirdLevel, secondLevelOne);
        val lastLevel          = MenuEntryDto("/lastLevel",    "Last level",       Collections.singletonList("USER"));
        GridDAO.createOrUpdateMenuEntry(secondLevelTwo, firstLevel);
        GridDAO.createOrUpdateMenuEntry(lastLevel, thirdLevel);
    }
}
