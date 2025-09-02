package ${packageName}.infrastructure.persistence;

import ${packageName}.domain.${requestName}.*;
import ${packageName}.infrastructure.persistence.entity.${className}Entity;
import ${packageName}.infrastructure.persistence.r2dbc.${className}Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ${className}DomainRepositoryImpl implements ${className}DomainRepository {

    private final ${className}Repository repository;

            <#assign idParams = []>
    <#list aggregateViews as aggregateView>
        <#if aggregateView.objectType == 1>
            <#assign idParams += [aggregateView.objectName + " " + aggregateView.objectName?uncap_first]>
        </#if>
    </#list>

    @Override
    public Mono<${className}Domain> load(${idParams?join(", ")}) {
        return repository.findById(<#list aggregateViews as aggregateView><#if aggregateView.objectType == 1>${aggregateView.objectName?uncap_first}.id()<#break></#if></#list>).map(e -> {
            ${className}Domain domain = new ${className}Domain();

                    <#list aggregateViews as aggregateView>
                <#if aggregateView.objectType == 1>
                    domain.set${aggregateView.objectName}(new ${aggregateView.objectName}(e.getId()));
                </#if>
            </#list>

                    <#list aggregateViews as aggregateView>
                <#if aggregateView.objectType != 1>
                    <#assign fieldList = aggregateView.objectValue?replace('"', '')?replace('[', '')?replace(']', '')?split(",")>
                    <#assign constructorParams = []>
                    <#list fieldList as field>
                        <#assign fieldTrimmed = field?trim>
                    <#list columnsViews as col>
                            <#if col.propJavaEntity == fieldTrimmed>
                    <#assign constructorParams += ["e.get" + fieldTrimmed?cap_first + "()"]>
                            </#if>
                        </#list>
                    </#list>
                    domain.set${aggregateView.objectName}(new ${aggregateView.objectName}(${constructorParams?join(", ")}));
                </#if>
            </#list>

                    domain.setCreateBy(e.getCreateBy());
            domain.setCreateTime(e.getCreateTime());
            domain.setUpdateBy(e.getUpdateBy());
            domain.setUpdateTime(e.getUpdateTime());
            domain.setVersion(e.getVersion());

            return domain;
        });
    }

    @Override
    public Mono<Long> save(${className}Domain domain) {
        ${className}Entity entity = new ${className}Entity();

                <#list aggregateViews as aggregateView>
            <#if aggregateView.objectType == 1>
                entity.set${aggregateView.objectName?replace("Id", "")}(Optional.ofNullable(domain.get${aggregateView.objectName}()).map(${aggregateView.objectName}::value).orElse(null));
            </#if>
        </#list>

                <#list aggregateViews as aggregateView>
            <#if aggregateView.objectType != 1>
                <#assign fieldList = aggregateView.objectValue?replace('"', '')?replace('[', '')?replace(']', '')?split(",")>
                <#list fieldList as field>
                    <#assign fieldTrimmed = field?trim>
                entity.set${fieldTrimmed?cap_first}(domain.get${aggregateView.objectName}().${fieldTrimmed}());
                </#list>
                </#if>
        </#list>

        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());

        return repository.save(entity).map(${className}Entity::getId);
    }
}
