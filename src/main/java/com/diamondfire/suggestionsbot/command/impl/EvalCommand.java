package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.argument.impl.types.MessageArgument;
import com.diamondfire.suggestionsbot.command.help.HelpContext;
import com.diamondfire.suggestionsbot.command.help.HelpContextArgument;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;


public class EvalCommand extends Command {

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Executes given code.")
                .addArgument(
                        new HelpContextArgument()
                                .name("Code")
                );
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArgument("code", new MessageArgument());
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event) {

        // Red is a bad boy, sometimes he decides he wants to open 500 tabs on my computer! This is here to stop Red, nothing else.
        if (!System.getProperty("os.name").contains("Linux")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("No.");
            builder.setColor(Color.red);

            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
        engine.put("jda", event.getJDA());
        engine.put("event", event);

        String code = event.getArgument("code");
        code = code.replaceAll("([^(]+?)\\s*->", "function($1)");

        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("Code", String.format("```js\n%s```", code), true);

        try {
            Object object = engine.eval(code); // Returns an object of the eval

            builder.setTitle("Eval Result");
            builder.addField("Object Returned:", String.format("```js\n%s```", StringUtil.fieldSafe(object)), false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();

        } catch (Throwable e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String sStackTrace = sw.toString();

            builder.setTitle("Eval failed!");
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            event.getChannel().sendMessage(String.format("```%s```", sStackTrace.length() >= 1500 ? sStackTrace.substring(0, 1500) : sStackTrace)).queue();

        }

    }

}
