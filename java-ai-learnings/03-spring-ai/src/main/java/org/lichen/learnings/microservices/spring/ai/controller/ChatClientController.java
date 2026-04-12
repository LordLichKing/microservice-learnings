package org.lichen.learnings.microservices.spring.ai.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 相比ChatModel, ChatClient能做的事情更多。可以集成外部工具，微调调参，还能增强prompt和response。
 */

@RestController
@RequestMapping("/client")
public class ChatClientController implements InitializingBean {

    @Autowired
    private ChatModel chatModel;

    private ChatClient chatClient;

    @GetMapping("/call")
    public String call(String message) {
        return chatClient.prompt(message).call().content();
    }

    @GetMapping("/callOverwrite")
    public String callOverwrite(String message) {
        return chatClient.prompt(message).system("请使用日文回答问题").call().content();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /*
         *  这里对ChatClient做默认配置。
         *
         *  defaultSystem: 默认系统提示词
         *  defaultAdvisor: 默认增强切面，类似spring AOP, 对prompt和response进行拦截
         *  defaultToolXXX: 默认工具，替代原来的Function Call
         *  defaultUser: 默认用户提示词
         *  defaultOptions: 默认配置，微调相关
         */
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultSystem("请使用英文回答问题")
                .defaultOptions(DashScopeChatOptions.builder().topP(0.7d).build())
                .build();
    }
}
