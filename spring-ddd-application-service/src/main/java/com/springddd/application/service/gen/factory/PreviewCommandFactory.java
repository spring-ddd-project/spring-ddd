package com.springddd.application.service.gen.factory;

import com.springddd.application.service.gen.command.GenerateCommand;
import com.springddd.application.service.gen.command.PreviewCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PreviewCommandFactory implements GenerateCommandFactory {

    private final PreviewCommand previewCommand;

    @Override
    public GenerateCommand createCommand() {
        return previewCommand;
    }
}




