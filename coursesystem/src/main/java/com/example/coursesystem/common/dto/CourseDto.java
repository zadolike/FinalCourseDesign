package com.example.coursesystem.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CourseDto implements Serializable {
    private String cno;

    private String cname;

}
