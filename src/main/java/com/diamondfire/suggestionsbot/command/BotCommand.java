package com.diamondfire.suggestionsbot.command;

import com.diamondfire.suggestionsbot.command.permissions.Permission;

public interface BotCommand {

    Permission getPermission();

}
