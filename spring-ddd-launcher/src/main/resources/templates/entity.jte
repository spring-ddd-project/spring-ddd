@params String packageName, String tableName, String className
package @packageName.infrastructure.persistence.entity;

import @packageName.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("@tableName")
public class @className {

}
