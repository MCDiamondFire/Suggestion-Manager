package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.help.*;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class WhenCommand extends Command {

    @Override
    public String getName() {
        return "when";
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("When is my suggestion coming?")
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet();
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("When is it coming?");
        builder.addField("When the devs get to it.", "The devs are busy, they have a life and they decide when to add things as they please. Do not expect them to instantly add your cool suggestion.", true);
        event.getChannel().sendMessage(builder.build()).queue();

    }

}
