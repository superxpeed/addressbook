package com.webapp;

import com.webapp.dto.MenuEntryDto;

@SuppressWarnings("all")
public class MenuCreator {
    public static void initMenu(){
        MenuEntryDto root = new MenuEntryDto("/root", "Root entry");
        root = GridUtils.createOrUpdateMenuEntry(root, null);
        MenuEntryDto firstLevel = new MenuEntryDto("/level1", "First level");
        firstLevel = GridUtils.createOrUpdateMenuEntry(firstLevel, root);
        MenuEntryDto secondLevel = new MenuEntryDto("/level2", "Second level");
        secondLevel = GridUtils.createOrUpdateMenuEntry(secondLevel, firstLevel);
        MenuEntryDto thirdLevel = new MenuEntryDto("/level3", "Third level");
        thirdLevel = GridUtils.createOrUpdateMenuEntry(thirdLevel, secondLevel);
        MenuEntryDto lastLevel = new MenuEntryDto("/lastLevel", "Last level");
        GridUtils.createOrUpdateMenuEntry(lastLevel, thirdLevel);
    }
}
