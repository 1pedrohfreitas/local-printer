package dev.pedrohfreitas.localprinter.dtos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseDTO<T> {

	private T data;

	private List<ResponseMessagemDTO> msgs;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<ResponseMessagemDTO> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<ResponseMessagemDTO> msgs) {
		this.msgs = msgs;
	}

	public ResponseEntity<T> setResponse(T data, HttpStatus httpStatus) {
		ResponseEntity<T> responseEntity = new ResponseEntity<T>(data, httpStatus);
		return responseEntity;
	}

	public ResponseEntity<ResponseDTO<T>> ok(T data, List<ResponseMessagemDTO> msgs) {
		ResponseDTO<T> responseDTO = new ResponseDTO<>();
		responseDTO.setData(data);
		responseDTO.setMsgs(msgs);
		ResponseEntity<ResponseDTO<T>> responseEntity = new ResponseEntity<ResponseDTO<T>>(responseDTO, HttpStatus.OK);

		return responseEntity;
	}

	public ResponseEntity<ResponseDTO<T>> ok(T data, HttpHeaders headers, List<ResponseMessagemDTO> msgs) {
		ResponseDTO<T> responseDTO = new ResponseDTO<>();
		ResponseEntity<ResponseDTO<T>> responseEntity = new ResponseEntity<ResponseDTO<T>>(responseDTO, headers,
				HttpStatus.OK);

		return responseEntity;
	}

	public ResponseEntity<ResponseDTO<Page<T>>> ok(Page<T> data, List<ResponseMessagemDTO> msgs) {
		ResponseDTO<Page<T>> responseDTO = new ResponseDTO<>();
		responseDTO.setData(data);
		responseDTO.setMsgs(msgs);
		ResponseEntity<ResponseDTO<Page<T>>> responseEntity = new ResponseEntity<ResponseDTO<Page<T>>>(responseDTO,
				HttpStatus.OK);

		return responseEntity;
	}

	public ResponseEntity<ResponseDTO<Page<T>>> ok(Page<T> data, HttpHeaders headers, List<ResponseMessagemDTO> msgs) {
		ResponseDTO<Page<T>> responseDTO = new ResponseDTO<>();
		responseDTO.setData(data);
		responseDTO.setMsgs(msgs);
		ResponseEntity<ResponseDTO<Page<T>>> responseEntity = new ResponseEntity<ResponseDTO<Page<T>>>(responseDTO,
				headers, HttpStatus.OK);

		return responseEntity;
	}

	public ResponseEntity<ResponseDTO<T>> created(T data, List<ResponseMessagemDTO> msgs) {
		ResponseDTO<T> responseDTO = new ResponseDTO<>();
		responseDTO.setData(data);
		responseDTO.setMsgs(msgs);
		ResponseEntity<ResponseDTO<T>> responseEntity = new ResponseEntity<ResponseDTO<T>>(responseDTO,
				HttpStatus.CREATED);
		return responseEntity;
	}

	public ResponseEntity<ResponseDTO<T>> created(T data, HttpHeaders headers, List<ResponseMessagemDTO> msgs) {
		ResponseDTO<T> responseDTO = new ResponseDTO<>();
		responseDTO.setData(data);
		responseDTO.setMsgs(msgs);
		ResponseEntity<ResponseDTO<T>> responseEntity = new ResponseEntity<ResponseDTO<T>>(responseDTO, headers,
				HttpStatus.CREATED);
		return responseEntity;
	}
}
