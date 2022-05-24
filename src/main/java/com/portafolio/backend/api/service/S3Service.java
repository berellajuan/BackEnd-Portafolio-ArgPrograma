package com.portafolio.backend.api.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.portafolio.backend.api.models.vm.Asset;

@Service
public class S3Service {
	private final static String BUCKET = "portafolio-app-argprograma";
	
	@Autowired
	private AmazonS3Client s3Client;
	
	public String putObject(MultipartFile archivo) throws IOException {
		String extencion = StringUtils.getFilenameExtension(archivo.getOriginalFilename().replace(" ", ""));
				
		String key = String.format("%s.%s",UUID.randomUUID(),extencion);
		
		ObjectMetadata metadata = new ObjectMetadata();

		metadata.setContentType(archivo.getContentType());

			PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET,key,archivo.getInputStream(),metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead);
			s3Client.putObject(putObjectRequest);
			return key;		
	}
	
	public Asset getObject(String key) throws IOException {

		S3Object s3Object = s3Client.getObject(BUCKET,key);

		ObjectMetadata metadata = s3Object.getObjectMetadata();
		
			S3ObjectInputStream inputStream = s3Object.getObjectContent();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			
			return new Asset(bytes,metadata.getContentType());
	}
	
	public void deleteObject(String key) {
		s3Client.deleteObject(BUCKET,key);
	}
	
	public String getObjectUrl(String key) {
		return String.format("https://%s.s3.amazonaws.com/%s",BUCKET,key);
	}
	
	

}
