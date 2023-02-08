package com.devmatch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	public String uploadFile(String uploadPath, String oriFileName, byte[] fileData) throws IOException {
		UUID uuid = UUID.randomUUID();

		String ext = oriFileName.substring(oriFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + ext;
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

		fos.write(fileData);
		fos.flush();
		fos.close();

		return savedFileName;
	}

//		thorws exeption
	public void deleteFile(String fileUrl) {
		File file = new File(fileUrl);
		if (file.exists()) {
			file.delete();
		}
	}
}
