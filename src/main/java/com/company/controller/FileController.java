package com.company.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/api/v1/files")
@Validated
@Log4j2
public class FileController {

	@GetMapping(value = "/excel/templates/ImportAccountIntoDepartmentTemplateFile")
	public void getImportAccountIntoDepartmentTemplateFile(HttpServletResponse response) throws IOException {
		File file = new File("src/main/resources/templates/ImportAccountIntoDepartmentTemplate.xlsx");

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

		ServletOutputStream outputStream = response.getOutputStream();
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		byte[] buffer = new byte[8192]; // 8KB
		int bytesRead = -1;

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outputStream.close();
	}

}
