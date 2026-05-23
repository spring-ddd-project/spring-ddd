package com.springddd.application.service.gen.factory;

import com.springddd.application.service.gen.adapter.FreemarkerTemplateAdapter;
import com.springddd.application.service.gen.adapter.TemplateEngineAdapter;
import freemarker.template.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FreemarkerEngineFactoryTest {

    @Mock
    private Configuration configuration;

    @InjectMocks
    private FreemarkerEngineFactory factory;

    @Test
    @DisplayName("createEngineAdapter 应返回 FreemarkerTemplateAdapter 实例")
    void createEngineAdapter_shouldReturnFreemarkerTemplateAdapter() {
        TemplateEngineAdapter result = factory.createEngineAdapter();
        assertThat(result).isInstanceOf(FreemarkerTemplateAdapter.class);
    }
}
