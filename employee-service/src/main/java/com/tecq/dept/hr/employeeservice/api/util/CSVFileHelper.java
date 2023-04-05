package com.tecq.dept.hr.employeeservice.api.util;


import com.tecq.dept.hr.employeeservice.scb.dto.SCBMessage;
import com.tecq.dept.hr.employeeservice.scb.exception.SCBClientException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class CSVFileHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static Iterable<CSVRecord> getAllCsvRecords(MultipartFile csvFile, String... headers) {
        String fileName=null;
        Iterable<CSVRecord> csvRecords = null;
        try{
            InputStream csvInputStream=null;
            if(csvFile==null || csvFile.getInputStream() == null){
                throw  new SCBClientException(new SCBMessage("MAPI10001E", "null"));
            }else{
                csvInputStream=csvFile.getInputStream();
                fileName=csvFile.getOriginalFilename();
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.builder()
                                .setHeader(headers)
                                .setSkipHeaderRecord(false)
                                .build());
                csvRecords = csvParser.getRecords();
            }
            return csvRecords;
        } catch (IOException e) {
            throw  new SCBClientException(new SCBMessage("MAPI10002E", fileName));
        }catch (SCBClientException e) {
            throw e;
        }catch (IllegalArgumentException e) {
            throw new SCBClientException(new SCBMessage("MAPI10003E", fileName));
        }
    }

    public static String getExtensionOfFileName(String filename) {
        Optional<String> fileExtension= Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        return fileExtension.isEmpty()?"":fileExtension.get();
    }
}
