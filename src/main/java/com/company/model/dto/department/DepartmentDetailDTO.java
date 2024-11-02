package com.company.model.dto.department;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DepartmentDetailDTO extends DepartmentDTO {

	private Integer memberSize;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdDateTime;

	// update
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date getCreatedDate() {
		return createdDateTime;
	}
}
