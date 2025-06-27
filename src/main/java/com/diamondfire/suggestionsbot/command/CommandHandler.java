package com.diamondfire.suggestionsbot.command;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.checkerframework.checker.units.qual.C;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.parser.Parser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.discord.jda5.JDA5CommandManager;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.incendo.cloud.discord.jda5.annotation.ReplySettingBuilderModifier;
import org.incendo.cloud.discord.slash.annotation.CommandScopeBuilderModifier;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);
    private final JDA5CommandManager<JDAInteraction> manager;
    private final AnnotationParser<JDAInteraction> annotationParser;
    private final List<BotCommand> registry = new ArrayList<>();

    public CommandHandler() {
        this.manager = new JDA5CommandManager<>(
                ExecutionCoordinator.simpleCoordinator(),
                JDAInteraction.InteractionMapper.identity()
        );

        this.annotationParser = new AnnotationParser<>(
                this.manager,
                JDAInteraction.class
        );

        ReplySettingBuilderModifier.install(this.annotationParser);
        CommandScopeBuilderModifier.install(this.annotationParser);

    }

    public JDA5CommandManager<JDAInteraction> getManager() {
        return this.manager;
    }

    public void register(BotCommand... commands) {
        for (final BotCommand command : commands) {
            LOGGER.info("Registring command {}", command.getClass().getSimpleName());
            this.annotationParser.parse(command);
            this.registry.add(command);
        }
    }

    public List<BotCommand> getRegistry() {
        return Collections.unmodifiableList(this.registry);
    }

}
