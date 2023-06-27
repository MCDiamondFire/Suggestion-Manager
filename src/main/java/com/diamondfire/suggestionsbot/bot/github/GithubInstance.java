package com.diamondfire.suggestionsbot.bot.github;

import org.kohsuke.github.*;

import javax.security.auth.login.LoginException;

public class GithubInstance {
    
    public static void initialize() throws LoginException {
        GitHub github = new GitHubBuilder().withJwtToken().build();
        github.getRepository().getIssue().getComments().get(0).update();
    }
    
}
