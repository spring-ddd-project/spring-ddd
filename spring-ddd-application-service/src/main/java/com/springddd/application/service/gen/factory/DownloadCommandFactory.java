package com.springddd.application.service.gen.factory;

import com.springddd.application.service.gen.command.DownloadCommand;
import com.springddd.application.service.gen.command.GenerateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownloadCommandFactory implements GenerateCommandFactory {

    private final DownloadCommand downloadCommand;

    @Override
    public GenerateCommand createCommand() {
        return downloadCommand;
    }
}

