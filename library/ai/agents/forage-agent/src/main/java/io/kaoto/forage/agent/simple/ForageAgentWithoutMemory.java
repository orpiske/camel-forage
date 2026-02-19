package io.kaoto.forage.agent.simple;

import java.util.List;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ForageAgentWithoutMemory {

    /**
     * Simple AI service interface without memory support
     */
    String chat(@UserMessage String userMessage);

    @SystemMessage("{{prompt}}")
    String chat(@UserMessage String userMessage, @V("prompt") String systemMessage);

    String chat(@UserMessage String userMessage, @UserMessage List<Content> contents);
}
