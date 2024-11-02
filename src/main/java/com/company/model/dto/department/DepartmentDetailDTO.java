package com.company.model.dto.department;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DepartmentDetailDTO extends DepartmentDTO {

	// overide
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdDateTime;
}
