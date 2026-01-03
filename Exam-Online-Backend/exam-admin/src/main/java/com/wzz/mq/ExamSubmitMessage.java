package com.wzz.mq;

import com.wzz.entity.ExamRecord;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExamSubmitMessage implements Serializable {

    private String username;

    private ExamRecord examRecord;
}
