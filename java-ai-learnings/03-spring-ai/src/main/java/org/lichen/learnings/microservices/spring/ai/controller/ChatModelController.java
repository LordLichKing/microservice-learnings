package org.lichen.learnings.microservices.spring.ai.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 基础的chat model的调用。
 */

@RestController
@RequestMapping("/model")
public class ChatModelController {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    /**
     * 最简单的对话。
     */
    @RequestMapping("/call/string")
    public String callString(String message) {
        return dashScopeChatModel.call(message);
    }

    /**
     * 带系统提示词模板的调用。
     */
    @RequestMapping("/call/messages")
    public String callMessages(String message) {
        SystemMessage systemMessage = new SystemMessage("你是一个翻译工具，请把用户的消息翻译成英文");
        Message userMsg = new UserMessage(message);
        return dashScopeChatModel.call(systemMessage, userMsg);
    }

    /**
     * 带有ChatOptions的调用，可以修改一些参数，比如模型，温度等等。
     */
    @RequestMapping("/call/prompt")
    public String callPrompt(String message) {
        SystemMessage systemMessage = new SystemMessage("请如实回答我的问题");
        Message userMsg = new UserMessage(message);

        ChatOptions chatOptions = ChatOptions.builder().model("deepseek-v3").build();
        Prompt prompt=new Prompt.Builder().messages(systemMessage, userMsg).chatOptions(chatOptions).build();
        return dashScopeChatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * 流式响应调用。
     * 除webflux之外，还可以通过SSE, StreamingResponseBody等
     */
    @RequestMapping("/stream/string")
    public Flux<String> callStreamString(String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return dashScopeChatModel.stream(message);
    }
}
