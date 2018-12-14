package com.webapp;

import com.webapp.dto.MenuEntryDto;

@SuppressWarnings("all")
public class MenuCreator {
    public static void initMenu(){
        GridUtils.clearMenus();
        MenuEntryDto root = new MenuEntryDto("/root", "Root entry");
        root = GridUtils.createOrUpdateMenuEntry(root, null);
        MenuEntryDto firstLevel = new MenuEntryDto("/level1", "First level");
        firstLevel = GridUtils.createOrUpdateMenuEntry(firstLevel, root);
        MenuEntryDto secondLevelOne = new MenuEntryDto("/level2", "Second level 1");
        secondLevelOne = GridUtils.createOrUpdateMenuEntry(secondLevelOne, firstLevel);
        MenuEntryDto secondLevelTwo = new MenuEntryDto("/emptyPage", "Second level 2");
        secondLevelTwo = GridUtils.createOrUpdateMenuEntry(secondLevelTwo, firstLevel);
        MenuEntryDto thirdLevel = new MenuEntryDto("/level3", "Third level");
        thirdLevel = GridUtils.createOrUpdateMenuEntry(thirdLevel, secondLevelOne);
        MenuEntryDto lastLevel = new MenuEntryDto("/lastLevel", "Last level");
        GridUtils.createOrUpdateMenuEntry(lastLevel, thirdLevel);
    }
}
