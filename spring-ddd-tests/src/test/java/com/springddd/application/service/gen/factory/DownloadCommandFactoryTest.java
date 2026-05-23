package com.springddd.application.service.gen.factory;

import com.springddd.application.service.gen.command.DownloadCommand;
import com.springddd.application.service.gen.command.GenerateCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DownloadCommandFactoryTest {

    @Mock
    private DownloadCommand downloadCommand;

    @InjectMocks
    private DownloadCommandFactory factory;

    @Test
    @DisplayName("createCommand 应返回注入的 DownloadCommand")
    void createCommand_shouldReturnDownloadCommand() {
        GenerateCommand result = factory.createCommand();
        assertThat(result).isSameAs(downloadCommand);
    }
}
