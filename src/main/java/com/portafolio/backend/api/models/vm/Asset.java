package com.portafolio.backend.api.models.vm;

import java.util.Arrays;

public class Asset {
	private byte[] content;
	private String contentType;

	public Asset(byte[] content, String contentType) {
		this.content = content;
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "Asset [content=" + Arrays.toString(content) + ", contentType=" + contentType + "]";
	}

	

}
