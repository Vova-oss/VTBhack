package com.example.securityframe.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LimitHistory {

    @ApiModelProperty(notes = ":id", name = "id", required = true, example = "1")
    private Long id;

    @ApiModelProperty(notes = ":id карты, на которой изменяются лимиты", name = "worker_id", required = true, example = "1")
    private Long card_id;

    @ApiModelProperty(notes = "Сумма, на которую ограничиваются расходы", name = "limit", example = "95000")
    private Long limit;

    @ApiModelProperty(notes = "Срок, на который ограничиваются расходы (в днях)", name = "term", example = "20")
    private Long term;

}
