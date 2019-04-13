package com.addressbook.ignite;

import com.addressbook.dto.MenuEntryDto;

import java.util.Collections;

@SuppressWarnings("all")
public class MenuCreator {
    public static void initMenu() {
        GridDAO.clearMenus();
        MenuEntryDto root = new MenuEntryDto("/root", "Root entry", Collections.singletonList("USER"));
        root = GridDAO.createOrUpdateMenuEntry(root, null);
        MenuEntryDto firstLevel = new MenuEntryDto("/level1", "First level", Collections.singletonList("USER"));
        firstLevel = GridDAO.createOrUpdateMenuEntry(firstLevel, root);
        MenuEntryDto secondLevelOne = new MenuEntryDto("/level2", "Second level 1", Collections.singletonList("USER"));
        secondLevelOne = GridDAO.createOrUpdateMenuEntry(secondLevelOne, firstLevel);
        MenuEntryDto secondLevelTwo = new MenuEntryDto("/adminPage", "Admin page", Collections.singletonList("ADMIN"));
        secondLevelTwo = GridDAO.createOrUpdateMenuEntry(secondLevelTwo, firstLevel);
        MenuEntryDto thirdLevel = new MenuEntryDto("/level3", "Third level", Collections.singletonList("USER"));
        thirdLevel = GridDAO.createOrUpdateMenuEntry(thirdLevel, secondLevelOne);
        MenuEntryDto lastLevel = new MenuEntryDto("/lastLevel", "Last level", Collections.singletonList("USER"));
        GridDAO.createOrUpdateMenuEntry(lastLevel, thirdLevel);
    }
}
