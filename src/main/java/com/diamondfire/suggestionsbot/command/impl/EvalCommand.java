package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import com.diamondfire.suggestionsbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.PrintWriter;
import java.io.StringWriter;


public class EvalCommand extends BotCommand {

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String getDescription() {
        return "Executes given code.";
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "code", "The code to execute.", true);
    }

    @Override
    public Permission getPermission() {
        return Permissions.DEVELOPER;
    }

    @Override
    public void run(SlashCommandEvent event) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
        engine.put("jda", event.getJDA());
        engine.put("event", event);

        String code = event.getOption("code").getAsString();
        code = code.replaceAll("([^(]+?)\\s*->", "function($1)");

        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("Code", String.format("```js\n%s```", code), true);

        try {
            Object object = engine.eval(code); // Returns an object of the eval

            builder.setTitle("Eval Result");
            builder.addField("Object Returned:", String.format("```js\n%s```", StringUtil.fieldSafe(object)), false);
            event.replyEmbeds(builder.build()).queue();

        } catch (Throwable e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String sStackTrace = sw.toString();

            builder.setTitle("Eval failed!");
            event.replyEmbeds(builder.build()).queue();
            event.getChannel().sendMessage(String.format("```%s```", sStackTrace.length() >= 1500 ? sStackTrace.substring(0, 1500) : sStackTrace)).queue();
        }
    }
}
